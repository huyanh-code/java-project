<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="bookstoreApp.book.home.createOrEditLabel"
          data-cy="BookCreateUpdateHeading"
          v-text="t$('bookstoreApp.book.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="book.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="book.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('bookstoreApp.book.title')" for="book-title"></label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="book-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
              required
            />
            <div v-if="v$.title.$anyDirty && v$.title.$invalid">
              <small class="form-text text-danger" v-for="error of v$.title.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('bookstoreApp.book.description')" for="book-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="book-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
              required
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('bookstoreApp.book.publicationDate')" for="book-publicationDate"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="book-publicationDate"
                  v-model="v$.publicationDate.$model"
                  name="publicationDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="book-publicationDate"
                data-cy="publicationDate"
                type="text"
                class="form-control"
                name="publicationDate"
                :class="{ valid: !v$.publicationDate.$invalid, invalid: v$.publicationDate.$invalid }"
                v-model="v$.publicationDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.publicationDate.$anyDirty && v$.publicationDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.publicationDate.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('bookstoreApp.book.price')" for="book-price"></label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="book-price"
              data-cy="price"
              :class="{ valid: !v$.price.$invalid, invalid: v$.price.$invalid }"
              v-model.number="v$.price.$model"
              required
            />
            <div v-if="v$.price.$anyDirty && v$.price.$invalid">
              <small class="form-text text-danger" v-for="error of v$.price.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('bookstoreApp.book.author')" for="book-author"></label>
            <select class="form-control" id="book-author" data-cy="author" name="author" v-model="book.author">
              <option :value="null"></option>
              <option
                :value="book.author && authorOption.id === book.author.authorName ? book.author : authorOption"
                v-for="authorOption in authors"
                :key="authorOption.id"
              >
                {{ authorOption.name }}
              </option>
            </select>

            <Multiselect
              :modelValue="selectedAuthor"
              :options="authors"
              label="name"
              @select="onAuthorSelected"
              @update:modelValue="updateValueAction"
            >
              <!-- <template slot="singleLabel" slot-scope="{ option }">
                <strong>{{ option.name }}</strong> born in <strong>
                {{ option.birthDate }}</strong>
              </template> -->
            </Multiselect>

            <pre class="language-json"><code>{{ selectedAuthor }}</code></pre>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<script lang="ts" src="./book-update.component.ts"></script>
