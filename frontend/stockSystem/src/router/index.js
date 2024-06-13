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
    },
    {
      path: '/tradeHistory',
      component: ()=> import("../views/TradeHistory.vue")
    },
    {
      path: '/accountManage',
      component: ()=> import("../views/AccountManage.vue")
    },
    {
      path: '/purchasePage',
      component: ()=> import("../views/PurchasePage.vue")
    },
    {
      path:'/classificationList',
      component: () => import("../views/ClassificationList.vue")
    },
    {
      path:'/StockInfo/dataCatalog_basicInfo',
      component: () => import("../components/dataCatalog_basicInfo.vue")
    },
    {
      path:'/StockInfo/dataCatalog_navHistory',
      component: () => import("../components/dataCatalog_navHistory.vue")
    },
    {
      path:'/StockInfo/RankInfo',
      component: () => import("../components/RankInfo.vue")
    },
    {
      path:'/StockInfo/AI_analysis',
      component: () => import("../components/AI_analysis.vue")
    },
    {
      path:'/StockInfo/UserFeedback',
      component: () => import("../components/UserFeedback.vue")
    },
    {
      path:'/comparison',
      component: ()=>import("../views/Comparison.vue")
    }
  ]
})

export default router
