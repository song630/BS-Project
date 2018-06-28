<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ msg }}</span>
      </div>
      <div><h2 style="font-weight: normal; color: #56bfef;">单词量：{{ plan }}</h2></div>
      <div>
        <!-- 把总单词数作为路由参数传过去 -->
        <el-button @click="$router.push({path: $router.options.routes[4].children[1].path, query: {num: num}})" type="success">
          开始学习
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import $ from 'jquery'
import { getCookie, setCookie } from '../../util.js'
export default {
  name: 'Start',
  data () {
    return {
      title: '',
      num: 0,
      plan: 0,
      msg: ''
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/start_info/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => {
        if (result.info === 'no_book') {
          alert('未选择学习的单词书')
        } else if (result.info === 'success') {
          this.title = result.studying // 正在学习的单词书
          setCookie('studying', this.title, 1)
          this.num = parseInt(result.num)
          this.plan = parseInt(result.plan)
          this.msg = '书名：' + this.title + '  单词数：' + this.num
        } else {
          alert('失败')
        }
      },
      error: function () { alert('失败') }
    })
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
}
.item {
  margin-bottom: 18px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.box-card {
  width: 480px;
}
</style>
