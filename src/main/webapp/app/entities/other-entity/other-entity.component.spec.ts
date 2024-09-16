/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import OtherEntity from './other-entity.vue';
import OtherEntityService from './other-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OtherEntityComponentType = InstanceType<typeof OtherEntity>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('OtherEntity Management Component', () => {
    let otherEntityServiceStub: SinonStubbedInstance<OtherEntityService>;
    let mountOptions: MountingOptions<OtherEntityComponentType>['global'];

    beforeEach(() => {
      otherEntityServiceStub = sinon.createStubInstance<OtherEntityService>(OtherEntityService);
      otherEntityServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          otherEntityService: () => otherEntityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        otherEntityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(OtherEntity, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.otherEntities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(OtherEntity, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: OtherEntityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(OtherEntity, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        otherEntityServiceStub.retrieve.reset();
        otherEntityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        otherEntityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.retrieve.called).toBeTruthy();
        expect(comp.otherEntities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        otherEntityServiceStub.retrieve.reset();
        otherEntityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(otherEntityServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.otherEntities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(otherEntityServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        otherEntityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeOtherEntity();
        await comp.$nextTick(); // clear components

        // THEN
        expect(otherEntityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(otherEntityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
