import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import OtherEntityService from './other-entity.service';
import { type IOtherEntity } from '@/shared/model/other-entity.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OtherEntityDetails',
  setup() {
    const otherEntityService = inject('otherEntityService', () => new OtherEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const otherEntity: Ref<IOtherEntity> = ref({});

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

    return {
      alertService,
      otherEntity,

      previousState,
      t$: useI18n().t,
    };
  },
});
