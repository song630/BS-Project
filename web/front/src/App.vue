<template>
  <div id="app">
    <router-view/>
    <!-- router-link to=/ 表示就显示在当前页面上 -->
    <!-- <router-link to="/" class="router-link">主页</router-link> -->
    <!-- <el-button type="primary" icon="el-icon-search">搜索</el-button> -->
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 20px;
  margin-bottom: 20px;
}
.router-link {
  float: left;
  font-weight: normal;
  font-family:"Microsoft YaHei", "Microsoft JhengHei Light", Helvetica, sans-serif;
  cursor: pointer;
  background: #abbac3;
  color: #ffffff;
  font-size: 16px;
  padding: 10px 20px 10px 20px;
  border: solid #abbac3 1px;
  text-decoration: none;
  margin: 5px 2px 10px 2px;
}
.router-link:hover {
  color: #ffffff;
  background: #888888;
  border: solid #888888 1px;
  text-decoration: none;
}
</style>
