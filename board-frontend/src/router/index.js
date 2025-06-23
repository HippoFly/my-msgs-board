import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../components/MessageBoard.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/AdminView.vue'),
    meta: {
      requiresAuth: true,
      requiresAdmin: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = store.getters.isLoggedIn
  const userRole = store.getters.userRole

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn) {
      next('/')
    } else if (to.matched.some(record => record.meta.requiresAdmin)) {
      if (userRole === 'ADMIN') {
        next()
      } else {
        next('/')
      }
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router 