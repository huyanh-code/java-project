<template>
  <!-- <div v-show="authors && authors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination pills :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
      </div>
  <div> -->
  <h2 id="page-heading" data-cy="AuthorHeading">
    <span v-text="t$('bookstoreApp.author.home.title')" id="author-heading"></span>
    <div class="d-flex justify-content-end">
      <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
        <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
        <span v-text="t$('bookstoreApp.author.home.refreshListLabel')"></span>
      </button>
      <router-link :to="{ name: 'AuthorCreate' }" custom v-slot="{ navigate }">
        <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-author">
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span v-text="t$('bookstoreApp.author.home.createLabel')"></span>
        </button>
      </router-link>
    </div>
  </h2>
  <br />
  <div class="alert alert-warning" v-if="!isFetching && authors && authors.length === 0">
    <span v-text="t$('bookstoreApp.author.home.notFound')"></span>
  </div>

  <div v-show="authors && authors.length > 0">
    <div class="row justify-content-center">
      <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
    </div>
    <div class="row justify-content-center">
      <b-pagination pills :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
    </div>
  </div>

  <div class="table-responsive" v-if="authors && authors.length > 0">
    <table class="table table-striped" aria-describedby="authors">
      <thead>
        <tr>
          <!-- <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th> -->
          <th scope="row" @click="changeOrder('name')">
            <span v-text="t$('bookstoreApp.author.name')"></span>
            <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
          </th>
          <th scope="row" @click="changeOrder('birthDate')">
            <span v-text="t$('bookstoreApp.author.birthDate')"></span>
            <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'birthDate'"></jhi-sort-indicator>
          </th>
          <th scope="row"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="author in authors" :key="author.id" data-cy="entityTable">
          <!-- <td>
              <router-link :to="{ name: 'AuthorView', params: { authorId: author.id } }">{{ author.id }}</router-link>
            </td> -->
          <td>
            <img
              :src="`${SERVER_URL}content/img/authors/${author.imageUrl ?? 'no-image.png'}`"
              width="40"
              height="40"
              class="rounded-circle"
              :alt="author.name"
            />
            <span class="font-weight-bolder ml-2">{{ author.name }}</span>
          </td>
          <td>{{ author.birthDate ? dayjs(author.birthDate).format(DATE_FORMAT2) : 'Khong ro ngay thang nam sinh' }}</td>
          <td class="text-right">
            <div class="btn-group">
              <router-link :to="{ name: 'AuthorView', params: { authorId: author.id } }" custom v-slot="{ navigate }">
                <button @click="navigate" class="btn btn-info btn-sm mr-2 details" data-cy="entityDetailsButton">
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </button>
              </router-link>
              <router-link :to="{ name: 'AuthorEdit', params: { authorId: author.id } }" custom v-slot="{ navigate }">
                <button @click="navigate" class="btn btn-primary btn-sm mr-2 edit" data-cy="entityEditButton">
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </button>
              </router-link>
              <b-button
                @click="prepareRemove(author)"
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
    </table>
  </div>
  <b-modal ref="removeEntity" id="removeEntity">
    <template #modal-title>
      <span id="bookstoreApp.author.delete.question" data-cy="authorDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
    </template>
    <div class="modal-body">
      <p id="jhi-delete-author-heading" v-text="t$('bookstoreApp.author.delete.question', { id: removeId })"></p>
    </div>
    <template #modal-footer>
      <div>
        <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-author"
          data-cy="entityConfirmDeleteButton"
          v-text="t$('entity.action.delete')"
          @click="removeAuthor()"
        ></button>
      </div>
    </template>
  </b-modal>
  <div v-show="authors && authors.length > 0">
    <div class="row justify-content-center">
      <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
    </div>
    <div class="row justify-content-center">
      <b-pagination pills :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
    </div>
  </div>
</template>

<script lang="ts" src="./author.component.ts"></script>
