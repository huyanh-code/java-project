<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="otherEntity">
        <h2 class="jh-entity-heading" data-cy="otherEntityDetailsHeading">
          <span v-text="t$('bookstoreApp.otherEntity.detail.title')"></span> {{ otherEntity.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('bookstoreApp.otherEntity.name')"></span>
          </dt>
          <dd>
            <span>{{ otherEntity.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('bookstoreApp.otherEntity.birthDate')"></span>
          </dt>
          <dd>
            <span>{{ otherEntity.birthDate }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="otherEntity.id"
          :to="{ name: 'OtherEntityEdit', params: { otherEntityId: otherEntity.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./other-entity-details.component.ts"></script>
