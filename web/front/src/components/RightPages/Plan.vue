<template>
  <div>
    <el-alert title="默认计划：每日20词" type="warning" show-icon close-text="知道了"></el-alert>
    <el-alert :title="msg" type="info" show-icon style="margin-top: 10px;"></el-alert>
    <div class="block" style="margin-top: 20px;">
      <el-slider v-model="numWords" show-input :min=5 :max=100 :step=5 style="cursor: pointer">
      </el-slider>
    </div>
    <el-button type="primary" @click="submit_plan" :disabled="studying === 'none'">提交计划</el-button>
  </div>
</template>

<script>
import { getCookie } from '../../util.js'
import $ from 'jquery'
export default {
  name: 'Plan',
  data () {
    return {
      numWords: 5,
      studying: 'none',
      num: 0,
      msg: ''
    }
  },
  methods: {
    submit_plan: function () { // 提交背单词计划
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/submit_plan/' + this.numWords,
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {},
        success: (result) => { // 返回类型为Map<>
          if (result.info === 'success') {
            alert('制定计划成功');
          } else {
            alert('制定计划失败');
          }
        },
        error: function () { alert('制定计划失败'); }
      })
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_book/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => {
        if (result === null) {
          alert('获取单词书信息失败或未选择单词书');
          this.$router.push(this.$router.options.routes[0].children[0].path); // 强制回到首页
        } else {
          this.studying = result.title;
          this.num = result.num;
          this.msg = '书名：' + this.studying + '  单词数：' + this.num;
        }
      },
      error: function () { alert('获取单词书信息失败'); }
    })
  }
}
</script>

<style scoped>

</style>
