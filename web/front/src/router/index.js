import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import UserInfo from '@/components/RightPages/UserInfo'
import HomeIndex from '@/components/RightPages/HomeIndex'
import WordBooks from '@/components/RightPages/WordBooks'
import Plan from '@/components/RightPages/Plan'
import WordInfo from '@/components/RightPages/WordInfo'
import Start from '@/components/RightPages/Start'
import WordCard from '@/components/RightPages/WordCard'
import Review from '@/components/RightPages/Review'
import Progress from '@/components/RightPages/Progress'
import TestStart from '@/components/RightPages/TestStart'
import TestCard from '@/components/RightPages/TestCard'
import TestResult from '@/components/RightPages/TestResult'
import PrivateTable from '@/components/RightPages/PrivateTable'
import { getCookie } from '../util.js'

Vue.use(Router)

const routes = [
  {
    path: '/',
    name: 'Index', // 首页 [0]
    component: Index,
    children: [
      { path: '/', component: HomeIndex, name: 'HomeIndex' }
    ]
  },
  {
    path: '/',
    name: 'userInfo', // 用户信息页 [1]
    component: Index,
    children: [
      { path: '/userInfo', component: UserInfo, name: 'UserInfo', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'wordBooks', // 单词书页面 [2]
    component: Index,
    children: [
      { path: '/wordBooks', component: WordBooks, name: 'WordBooks', meta: { requireAuth: true } },
      { path: '/wordInfo', component: WordInfo, name: 'WordInfo', meta: { requireAuth: true } },
      { path: '/privateTable', component: PrivateTable, name: 'PrivateTable', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'plan', // 制定背单词计划页面 [3]
    component: Index,
    children: [
      { path: '/plan', component: Plan, name: 'Plan', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'memorize', // 背单词页面 [4]
    component: Index,
    children: [
      { path: '/start', component: Start, name: 'Start', meta: { requireAuth: true } },
      { path: '/wordCard', component: WordCard, name: 'WordCard', meta: { requireAuth: true } }
      // 可能还要加一个单词的详细信息页面
    ]
  },
  {
    path: '/',
    name: 'review', // 复习页面 [5]
    component: Index,
    children: [
      { path: '/review', component: Review, name: 'Review', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'progress', // 进度页面 [6]
    component: Index,
    children: [
      { path: '/progress', component: Progress, name: 'Progress', meta: { requireAuth: true } }
    ]
  },
  {
    path: '/',
    name: 'test', // 测试页面 [7]
    component: Index,
    children: [
      { path: '/testStart', component: TestStart, name: 'TestStart', meta: { requireAuth: true } },
      { path: '/testCard', component: TestCard, name: 'TestCard', meta: { requireAuth: true } },
      { path: '/testResult', component: TestResult, name: 'TestResult', meta: { requireAuth: true } }
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
