import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import UserInfo from '@/components/RightPages/UserInfo'
import HomeIndex from '@/components/RightPages/HomeIndex'
import WordBooks from '@/components/RightPages/WordBooks'
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
    name: 'wordBooks,',
    component: Index, // 单词书页面
    children: [
      { path: '/wordBooks', component: WordBooks, name: 'WordBooks', meta: { requireAuth: true } }
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
