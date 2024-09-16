/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OtherEntityUpdate from './other-entity-update.vue';
import OtherEntityService from './other-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OtherEntityUpdateComponentType = InstanceType<typeof OtherEntityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const otherEntitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<OtherEntityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('OtherEntity Management Update Component', () => {
    let comp: OtherEntityUpdateComponentType;
    let otherEntityServiceStub: SinonStubbedInstance<OtherEntityService>;

    beforeEach(() => {
      route = {};
      otherEntityServiceStub = sinon.createStubInstance<OtherEntityService>(OtherEntityService);
      otherEntityServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          otherEntityService: () => otherEntityServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(OtherEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.otherEntity = otherEntitySample;
        otherEntityServiceStub.update.resolves(otherEntitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.update.calledWith(otherEntitySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        otherEntityServiceStub.create.resolves(entity);
        const wrapper = shallowMount(OtherEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.otherEntity = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        otherEntityServiceStub.find.resolves(otherEntitySample);
        otherEntityServiceStub.retrieve.resolves([otherEntitySample]);

        // WHEN
        route = {
          params: {
            otherEntityId: `${otherEntitySample.id}`,
          },
        };
        const wrapper = shallowMount(OtherEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.otherEntity).toMatchObject(otherEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        otherEntityServiceStub.find.resolves(otherEntitySample);
        const wrapper = shallowMount(OtherEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
