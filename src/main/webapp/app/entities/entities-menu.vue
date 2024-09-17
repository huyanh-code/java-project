<template>
  <div>
    <b-dropdown-item to="/author">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.author')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/other-entity">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.otherEntity')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/book">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.book')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
