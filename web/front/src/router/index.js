import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import UserInfo from '@/components/RightPages/UserInfo'
import HomeIndex from '@/components/RightPages/HomeIndex'
import WordBooks from '@/components/RightPages/WordBooks'
import Plan from '@/components/RightPages/Plan'
import WordInfo from '@/components/RightPages/WordInfo'
import { getCookie } from '../util.js'

Vue.use(Router)

const routes = [
  {
    path: '/',
    name: 'Index', // 首页
    component: Index,
    children: [
      { path: '/', component: HomeIndex, name: 'HomeIndex' }
    ]
  },
  {
    path: '/',
    name: 'userInfo', // 用户信息页
    component: Index,
    children: [
      { path: '/userInfo', component: UserInfo, name: 'UserInfo', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'wordBooks', // 单词书页面
    component: Index,
    children: [
      { path: '/wordBooks', component: WordBooks, name: 'WordBooks', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'plan', // 制定背单词计划页面
    component: Index,
    children: [
      { path: '/plan', component: Plan, name: 'Plan', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'wordInfo',
    component: WordInfo,
    children: [
      { path: '/wordInfo', component: WordInfo, name: 'WordInfo', meta: { requireAuth: true } }
    ]
  }
]

const router = new Router({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    let status = getCookie('isLogin')
    if (status === 'true') {
      next()
    } else {
      console.log('must login')
      next({path: '/'})
    }
  } else {
    next()
  }
})
export default router
