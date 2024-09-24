import { type ComputedRef, defineComponent, inject } from 'vue';
import { useI18n } from 'vue-i18n';

import BarChart from '@/components/Charts/BarChart.vue';

import type LoginService from '@/account/login.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  components: {
    'bar-chart': BarChart,
  },
  setup() {
    const loginService = inject<LoginService>('loginService');

    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const openLogin = () => {
      loginService.openLogin();
    };

    return {
      authenticated,
      username,
      openLogin,
      t$: useI18n().t,
    };
  },
});
