import { defineComponent, provide } from 'vue';

import AuthorService from './author/author.service';
import OtherEntityService from './other-entity/other-entity.service';
import BookService from './book/book.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('authorService', () => new AuthorService());
    provide('otherEntityService', () => new OtherEntityService());
    provide('bookService', () => new BookService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
