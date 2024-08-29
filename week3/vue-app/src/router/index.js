import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/list',
    name: 'List',
    component: () => import(/* webpackChunkName: "about" */ '../views/List.vue')
  },
  {
    path: '/view',
    name: 'View',
    component: () => import(/* webpackChunkName: "about" */ '../views/View.vue')
  },
  {
    path: '/modify',
    name: 'Modify',
    component: () => import(/* webpackChunkName: "about" */ '../views/Modify.vue')
  },
  {
    path: '/write',
    name: 'Write',
    component: () => import(/* webpackChunkName: "about" */ '../views/Write.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
