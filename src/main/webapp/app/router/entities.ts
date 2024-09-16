import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Author = () => import('@/entities/author/author.vue');
const AuthorUpdate = () => import('@/entities/author/author-update.vue');
const AuthorDetails = () => import('@/entities/author/author-details.vue');

const OtherEntity = () => import('@/entities/other-entity/other-entity.vue');
const OtherEntityUpdate = () => import('@/entities/other-entity/other-entity-update.vue');
const OtherEntityDetails = () => import('@/entities/other-entity/other-entity-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'author',
      name: 'Author',
      component: Author,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'author/new',
      name: 'AuthorCreate',
      component: AuthorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'author/:authorId/edit',
      name: 'AuthorEdit',
      component: AuthorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'author/:authorId/view',
      name: 'AuthorView',
      component: AuthorDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'other-entity',
      name: 'OtherEntity',
      component: OtherEntity,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'other-entity/new',
      name: 'OtherEntityCreate',
      component: OtherEntityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'other-entity/:otherEntityId/edit',
      name: 'OtherEntityEdit',
      component: OtherEntityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'other-entity/:otherEntityId/view',
      name: 'OtherEntityView',
      component: OtherEntityDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
