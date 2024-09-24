<template>
  <div>
    <h2 id="page-heading" data-cy="BookHeading">
      <span v-text="t$('bookstoreApp.book.home.title')" id="book-heading"></span>

      <form>
        <div class="form-inline">
        <div class="input-group mb-2 mr-sm-2 search-container">
        <input
          v-model="searchCondition.title"
          type="text"
          class="form-control"
          placeholder="Search title or authors..."
        />
        <div class="input-group-append">
      
      <span v-if="searchCondition.title || searchCondition.authorName" 
            class="input-group-text" 
            @click="clearSearch"
            style="cursor: pointer;">
        <font-awesome-icon icon="times" />
      </span>
      <!-- Search Icon: Always Visible -->
      <span class="input-group-text" 
            @click="retrieveBooks" 
            style="cursor: pointer;">
        <font-awesome-icon icon="search" />
      </span>
    </div>
  </div>
</div>

        <!-- <div class="form-inline">
          <div class="search-container">
            <input v-model="searchCondition.title" type="text" class="form-control mb-2 mr-sm-2" placeholder="Search title..." />
          </div>
          <div class="search-container">
            <input v-model="searchCondition.authorName" type="text" class="form-control mb-2 mr-sm-2" placeholder="Search authors..." />
            <button type="button" @click="retrieveBooks" class="btn btn-primary mb-2 mr-2" :disabled="isFetching">
              <font-awesome-icon icon="search"></font-awesome-icon>
              <span v-text="t$('bookstoreApp.book.home.search')"></span>
            </button>
            <button @click="hanldeCharater" class="btn btn-primary mb-2 mr-2" :disabled="isFetching">
              <font-awesome-icon icon="remove"></font-awesome-icon>
              <span v-text="t$('bookstoreApp.book.home.remove')"></span>
            </button>
          </div>
        </div> -->

        <button class="btn btn-info mb-2 mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('bookstoreApp.book.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'BookCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary mb-2 mr-2 jh-create-entity create-book"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('bookstoreApp.book.home.createLabel')"></span>
          </button>
        </router-link>
      </form>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && books && books.length === 0">
      <span v-text="t$('bookstoreApp.book.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="books && books.length > 0">
      <table class="table table-striped" aria-describedby="books">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('title')">
              <span v-text="t$('bookstoreApp.book.title')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('description')">
              <span v-text="t$('bookstoreApp.book.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('publicationDate')">
              <span v-text="t$('bookstoreApp.book.publicationDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publicationDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('price')">
              <span v-text="t$('bookstoreApp.book.price')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('author.id')">
              <span v-text="t$('bookstoreApp.book.author')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'author.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'BookView', params: { bookId: book.id } }">{{ book.id }}</router-link>
            </td>
            <td>{{ book.title }}</td>
            <td>{{ book.description }}</td>
            <td>{{ book.publicationDate }}</td>
            <td>{{ book.price }}</td>
            <td>
              <div v-if="book.author">
                <router-link :to="{ name: 'AuthorView', params: { authorId: book.author.id } }">{{ book.author.name }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'BookView', params: { bookId: book.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm mr-2 details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'BookEdit', params: { bookId: book.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm mr-2 edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(book)"
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
        <span id="bookstoreApp.book.delete.question" data-cy="bookDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-book-heading" v-text="t$('bookstoreApp.book.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-book"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeBook()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
  <div v-show="books && books.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>

</template>

<script lang="ts" src="./book.component.ts"></script>
