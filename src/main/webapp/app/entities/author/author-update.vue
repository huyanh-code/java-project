<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="bookstoreApp.author.home.createOrEditLabel"
          data-cy="AuthorCreateUpdateHeading"
          v-text="t$('bookstoreApp.author.home.createOrEditLabel')"
          class="mb-5"
        ></h2>
        <div>
          <div class="form-group" v-if="author.id">
            <!-- <label for="id" v-text="t$('global.field.id')"></label> -->
            <input type="text" class="form-control" id="id" name="id" v-model="author.id" readonly hidden />
          </div>

          <div class="form-group row">
            <div class="col">
              <div class="form-group">
                <label class="form-control-label" v-text="t$('bookstoreApp.author.name')" for="author-name"></label>
                <input
                  type="text"
                  class="form-control"
                  name="name"
                  id="author-name"
                  data-cy="name"
                  :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
                  v-model="v$.name.$model"
                />
              </div>

              <label class="form-control-label" v-text="t$('bookstoreApp.author.birthDate')" for="author-birthDate"></label>
              <b-input-group class="mb-3">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="author-birthDate"
                    v-model="v$.birthDate.$model"
                    name="birthDate"
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
                  id="author-birthDate"
                  data-cy="birthDate"
                  type="text"
                  class="form-control"
                  name="birthDate"
                  :class="{ valid: !v$.birthDate.$invalid, invalid: v$.birthDate.$invalid }"
                  v-model="v$.birthDate.$model"
                />
              </b-input-group>
            </div>

            <div class="col">
              <ImageUploader @fileAdded="onFileAdded" @fileRemoved="onFileRemoved" />
            </div>
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
<script lang="ts" src="./author-update.component.ts"></script>
