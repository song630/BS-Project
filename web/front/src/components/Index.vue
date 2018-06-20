<template>
  <div class="hello">
    <el-container style="border: 1px solid #eee">

      <el-aside>
        <el-menu>
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-setting"></i>用户信息管理</template>
            <el-menu-item-group>
              <el-menu-item index="1-1">选项1</el-menu-item>
              <el-menu-item index="1-2">选项2</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>我的单词本</template>
            <el-menu-item-group>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header>
          <h2 class="header-title">背单词网站</h2>
          <LoginDialog class="login-dialog" @login_success="change_login_name"></LoginDialog><!-- 登录页面 -->
          <SignUpDialog class="signup-dialog"></SignUpDialog><!-- 注册页面 -->
          <span class="header-others">{{ username }}</span>
        </el-header>

        <el-main>
          <el-table :data="tableData">
            <el-table-column prop="date" label="日期" width="140">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="address" label="地址">
            </el-table-column>
          </el-table>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import LoginDialog from './small/LoginDialog'
import SignUpDialog from './small/SignUpDialog'
export default {
  name: 'Index',
  components: { LoginDialog, SignUpDialog },
  data () {
    return {
      username: 'Guest',
      tableData: Array(10).fill({date: '2016-05-02', name: 'ABC', address: 'Address'})
    }
  },
  methods: {
    change_login_name (param) { // 把guest改为子组件传过来的用户名
      this.username = param
    }
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
.login-dialog, .signup-dialog {
  float: right;
  cursor: pointer;
}
.el-aside {
  color: #333;
  width: 200px;
  background-color: rgb(238, 241, 246);
}
</style>
