/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OtherEntityDetails from './other-entity-details.vue';
import OtherEntityService from './other-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OtherEntityDetailsComponentType = InstanceType<typeof OtherEntityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const otherEntitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('OtherEntity Management Detail Component', () => {
    let otherEntityServiceStub: SinonStubbedInstance<OtherEntityService>;
    let mountOptions: MountingOptions<OtherEntityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      otherEntityServiceStub = sinon.createStubInstance<OtherEntityService>(OtherEntityService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          otherEntityService: () => otherEntityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        otherEntityServiceStub.find.resolves(otherEntitySample);
        route = {
          params: {
            otherEntityId: `${123}`,
          },
        };
        const wrapper = shallowMount(OtherEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.otherEntity).toMatchObject(otherEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        otherEntityServiceStub.find.resolves(otherEntitySample);
        const wrapper = shallowMount(OtherEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
