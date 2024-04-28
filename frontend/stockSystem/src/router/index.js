import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: ()=> import("../views/HomeView.vue")
    },
    {
      path: '/stockInfo',
      component: ()=> import("../views/StockInfo.vue")
    },
    {
      path: '/investList',
      component: ()=> import("../views/InvestList.vue")
    },
    {
      path: '/userInfo',
      component: ()=> import("../views/UserInfo.vue")
    },
    {
      path: '/calculator',
      component: ()=> import("../views/Calculator.vue")
    }
  ]
})

export default router
