import { type Ref, defineComponent, inject, onMounted, ref, watch, watchEffect } from 'vue';
import { useI18n } from 'vue-i18n';
import { useIntersectionObserver } from '@vueuse/core';

import BookService, { BookSearchCondition } from './book.service';
import { type IBook } from '@/shared/model/book.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Book',
  setup() {
    const { t: t$ } = useI18n();
    const dataUtils = useDataUtils();
    const bookService = inject('bookService', () => new BookService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);
    const links: Ref<any> = ref({});

    const books: Ref<IBook[]> = ref([]);
    const searchCondition = ref<BookSearchCondition>({
      combinedSearch: undefined, 
      title: undefined,
      authorName: undefined,
    });
    
    const isFetching = ref(false);
    const titleInputRef = ref<HTMLInputElement | null>(null); // Khai báo ref cho title
    const authorNameInputRef = ref<HTMLInputElement | null>(null); // Khai báo ref cho authorName

    const clear = () => {
      page.value = 1;
      links.value = {};
      books.value = [];
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveBooks = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };

        const [title, authorName] = searchCondition.value.combinedSearch || ''
        .split(':')
        .map((part) => part.trim());

    searchCondition.value.title = title || ''; 
    searchCondition.value.authorName = authorName || ''; 
        
        const res = await bookService().retrieve(searchCondition.value, paginationQuery);
        console.log('got books', res);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        links.value = dataUtils.parseLinks(res.headers?.link);
        books.value = res.data ?? [];
        // books.value.push(...(res.data ?? []));
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      clear();
    };

    onMounted(async () => {
      await retrieveBooks();
    });

    const clearSearch = (): void => {
      searchCondition.value.title = '';
      searchCondition.value.authorName = '';
      retrieveBooks(); // Có thể thực hiện tìm kiếm lại với điều kiện trống
    };

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IBook) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeBook = async () => {
      try {
        await bookService().delete(removeId.value);
        const message = t$('bookstoreApp.book.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        clear();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // Whenever order changes, reset the pagination
    watch([propOrder, reverse], () => {
      clear();
    });

    // // Whenever the data resets or page changes, switch to the new page.
    // watch([books, page], async ([data, page], [_prevData, prevPage]) => {
    //   if (data.length === 0 || page !== prevPage) {
    //     await retrieveBooks();
    //   }
    // });

    const infiniteScrollEl = ref<HTMLElement>(null);
    const intersectionObserver = useIntersectionObserver(
      infiniteScrollEl,
      intersection => {
        if (intersection[0].isIntersecting && !isFetching.value) {
          page.value++;
        }
      },
      {
        threshold: 0.5,
        immediate: false,
      },
    );
    watchEffect(() => {
      if (links.value.next) {
        intersectionObserver.resume();
      } else if (intersectionObserver.isActive) {
        intersectionObserver.pause();
      }
    });

    return {
      books,
      searchCondition,
      handleSyncList,
      clearSearch,
      titleInputRef,
      authorNameInputRef,
      isFetching,
      retrieveBooks,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeBook,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      infiniteScrollEl,
      t$,
      ...dataUtils,
    };
  },
});
