import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import OtherEntityService from './other-entity.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IOtherEntity, OtherEntity } from '@/shared/model/other-entity.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OtherEntityUpdate',
  setup() {
    const otherEntityService = inject('otherEntityService', () => new OtherEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const otherEntity: Ref<IOtherEntity> = ref(new OtherEntity());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveOtherEntity = async otherEntityId => {
      try {
        const res = await otherEntityService().find(otherEntityId);
        otherEntity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.otherEntityId) {
      retrieveOtherEntity(route.params.otherEntityId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 5 }).toString(), 5),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      birthDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, otherEntity as any);
    v$.value.$validate();

    return {
      otherEntityService,
      alertService,
      otherEntity,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.otherEntity.id) {
        this.otherEntityService()
          .update(this.otherEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('bookstoreApp.otherEntity.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.otherEntityService()
          .create(this.otherEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('bookstoreApp.otherEntity.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
