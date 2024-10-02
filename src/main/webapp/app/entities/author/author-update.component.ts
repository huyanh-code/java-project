import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AuthorService from './author.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Author, type IAuthor } from '@/shared/model/author.model';

import ImageUploader from '@/components/ImageUploader.vue';

// import Datepicker from '@vuepic/vue-datepicker';
// import '@vuepic/vue-datepicker/dist/main.css';
// import DatetimePicker from '@/components/DatetimePicker.vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AuthorUpdate',
  components: {
    ImageUploader: ImageUploader,
    Multiselect: Multiselect,
  },
  setup() {
    const authorService = inject('authorService', () => new AuthorService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const selectedFile: Ref<File> = ref(null);
    const author: Ref<IAuthor> = ref(new Author());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAuthor = async authorId => {
      try {
        const res = await authorService().find(authorId);
        author.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.authorId) {
      retrieveAuthor(route.params.authorId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      birthDate: {},
      authors: {},
      books: {},
    };
    const v$ = useVuelidate(validationRules, author as any);
    v$.value.$validate();

    return {
      authorService,
      alertService,
      author,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
      ImageUploader,
      selectedFile,
      // Datepicker,
      // DatetimePicker
    };
  },
  created(): void {},
  methods: {
    onFileAdded(file: File): void {
      console.log('file da them vao', file);
      this.selectedFile = file;
    },
    onFileRemoved(): void {
      this.selectedFile = null;
    },
    save(): void {
      this.isSaving = true;
      if (this.author.id) {
        this.authorService()
          .update(this.author, this.selectedFile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('bookstoreApp.author.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.authorService()
          .create(this.author, this.selectedFile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('bookstoreApp.author.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
