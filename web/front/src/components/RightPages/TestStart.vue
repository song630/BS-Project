<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ msg }}</span>
      </div>
      <div><h2 style="font-weight: normal; color: #56bfef;">单词量：30</h2></div>
      <div>
        <el-button @click="$router.push($router.options.routes[7].children[1].path)" :disabled="!canTest" type="success">
          开始测试
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import $ from 'jquery'
import { getCookie, setCookie } from '../../util.js'
export default {
  name: 'TestStart',
  data () {
    return {
      title: '',
      num: 0,
      msg: '',
      canTest: false // ===== 已学习的单词数小于50时不能测试
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
          this.canTest = parseInt(result.studied) >= 30 // 已经学习的单词数大于30才能测试
          this.msg = '书名：' + this.title + '  单词数：' + this.num
          if (!this.canTest) {
            alert('已学习的单词数过少，暂时不能测试')
          }
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

</style>
