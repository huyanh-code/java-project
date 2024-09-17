import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import BookService from './book.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import AuthorService from '@/entities/author/author.service';
import { type IAuthor } from '@/shared/model/author.model';
import { Book, type IBook } from '@/shared/model/book.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BookUpdate',
  setup() {
    const bookService = inject('bookService', () => new BookService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const book: Ref<IBook> = ref(new Book());

    const authorService = inject('authorService', () => new AuthorService());

    const authors: Ref<IAuthor[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveBook = async bookId => {
      try {
        const res = await bookService().find(bookId);
        book.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.bookId) {
      retrieveBook(route.params.bookId);
    }

    const initRelationships = () => {
      authorService()
        .retrieve()
        .then(res => {
          authors.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      title: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 5 }).toString(), 5),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 1000 }).toString(), 1000),
      },
      description: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      publicationDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      price: {
        required: validations.required(t$('entity.validation.required').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 5 }).toString(), 5),
        max: validations.maxValue(t$('entity.validation.max', { max: 1000 }).toString(), 1000),
      },
      author: {},
    };
    const v$ = useVuelidate(validationRules, book as any);
    v$.value.$validate();

    return {
      bookService,
      alertService,
      book,
      previousState,
      isSaving,
      currentLanguage,
      authors,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.book.id) {
        this.bookService()
          .update(this.book)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('bookstoreApp.book.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.bookService()
          .create(this.book)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('bookstoreApp.book.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
