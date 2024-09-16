<template>
  <div>
    <h2 id="page-heading" data-cy="OtherEntityHeading">
      <span v-text="t$('bookstoreApp.otherEntity.home.title')" id="other-entity-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('bookstoreApp.otherEntity.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'OtherEntityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-other-entity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('bookstoreApp.otherEntity.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && otherEntities && otherEntities.length === 0">
      <span v-text="t$('bookstoreApp.otherEntity.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="otherEntities && otherEntities.length > 0">
      <table class="table table-striped" aria-describedby="otherEntities">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('bookstoreApp.otherEntity.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('birthDate')">
              <span v-text="t$('bookstoreApp.otherEntity.birthDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'birthDate'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="otherEntity in otherEntities" :key="otherEntity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OtherEntityView', params: { otherEntityId: otherEntity.id } }">{{ otherEntity.id }}</router-link>
            </td>
            <td>{{ otherEntity.name }}</td>
            <td>{{ otherEntity.birthDate }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'OtherEntityView', params: { otherEntityId: otherEntity.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'OtherEntityEdit', params: { otherEntityId: otherEntity.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(otherEntity)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="bookstoreApp.otherEntity.delete.question"
          data-cy="otherEntityDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-otherEntity-heading" v-text="t$('bookstoreApp.otherEntity.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-otherEntity"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeOtherEntity()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./other-entity.component.ts"></script>
