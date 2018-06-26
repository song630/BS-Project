<template>
  <div class="hello">
    <el-container style="border: 1px solid #eee">

      <el-aside>
        <el-menu>
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-setting"></i>用户信息管理</template>
            <el-menu-item-group>
              <el-menu-item index="1-1" @click="$router.push($router.options.routes[1].children[0].path)">我的信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-document"></i>我的单词书</template>
            <el-menu-item-group>
              <el-menu-item index="2-1" @click="$router.push($router.options.routes[2].children[0].path)">选择单词书</el-menu-item><!-- 包含自定义单词书 -->
              <el-menu-item index="2-2">背单词</el-menu-item>
              <el-menu-item index="2-3" @click="$router.push($router.options.routes[3].children[0].path)">制定计划</el-menu-item>
              <el-menu-item index="2-4">复习</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title"><i class="el-icon-edit"></i>测试</template>
            <el-menu-item-group>
              <el-menu-item index="3-1">测试</el-menu-item>
              <el-menu-item index="3-2">我的进度</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="float: left; margin-top: 22px;">
            <el-breadcrumb-item :to="$router.options.routes[0].children[0].path">
              <b style="cursor: pointer">首页</b>
            </el-breadcrumb-item>
          </el-breadcrumb>
          <h2 class="header-title" style="margin-left: 50px;">背单词网站</h2>
          <LoginDialog class="login-dialog" @login_success="change_login_name"></LoginDialog><!-- 登录页面 -->
          <SignUpDialog class="signup-dialog"></SignUpDialog><!-- 注册页面 -->
          <LogoutDialog class="logout-dialog" @logout_success="on_logout"></LogoutDialog><!-- 退出页面 -->
          <span class="header-others">{{ username }}</span>
        </el-header>

        <el-main>
          <router-view>
          </router-view>
        </el-main>

        <el-footer>
          <div>Designed by Yizhi Song, using Vue.js and ElementUI</div>
          <div>{{ this.date }}</div>
        </el-footer>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import LoginDialog from './small/LoginDialog'
import SignUpDialog from './small/SignUpDialog'
import LogoutDialog from './small/LogoutDialog'
import { delCookie, setCookie, getCookie } from '../util.js'
export default {
  name: 'Index',
  components: { LoginDialog, SignUpDialog, LogoutDialog },
  data () {
    return {
      username: 'Guest',
      isLogin: false,
      tableData: Array(10).fill({date: '2016-05-02', name: 'ABC', address: 'Address'}),
      date: ''
    }
  },
  methods: {
    change_login_name (param) { // 把guest改为子组件传过来的用户名
      this.username = param
      this.isLogin = true
    },
    on_logout () { // 用户已经登出
      this.username = 'guest'
      this.isLogin = false
      delCookie('username', 1)
      setCookie('isLogin', 'false', 1)
      console.log('after logout, cookies: ', document.cookie)
    }
  },
  mounted: function () {
    let nowDate = new Date()
    let year = nowDate.getFullYear()
    let month = nowDate.getMonth() + 1
    let day = nowDate.getDate()
    this.date = (year + '-') + (month + '-') + day
    // setCookie('isLogin', 'false', 1)
    if (getCookie('username') === null || getCookie('username') === 'null') {
      this.isLogin = false
    }
    console.log('Index mounted, cookies: ', document.cookie)
  }
}
</script>

<style scoped>
.el-container {
  height: 100%;
  width: 100%;
}
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}
.header-title {
  float: left;
  font-size: 20px;
  font-weight: normal;
  height: 40px;
  margin-top: 0;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.header-others {
  float: right;
  font-size: 12px;
  margin-right: 8px;
}
.login-dialog, .signup-dialog, .logout-dialog {
  float: right;
  cursor: pointer;
}
.el-aside {
  color: #333;
  width: 200px;
  background-color: rgb(238, 241, 246);
}
.el-main {
  height: 500px;
}
.el-footer {
  border-top: solid 1px #eee;
  padding-top: 10px;
  color: #888888;
  font-size: 0.8em;
}
</style>
