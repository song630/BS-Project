<template>
  <div>
    <schart :canvasId="canvasId" :type="type" :width="width" :height="height" :data="data" :options="options">
    </schart>
    <br>
    <el-progress type="circle" :percentage="percent" color="#8e71c7"></el-progress>
  </div>
</template>

<script>
import Schart from 'vue-schart'
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'Progress',
  components: { Schart },
  data () {
    return {
      percent: 0,
      canvasId: 'myCanvas',
      type: 'bar',
      width: 250,
      height: 130,
      data: [ // 格式: { name: '', value: '' }
        { name: 'day1', value: 10 },
        { name: 'day2', value: 20 },
        { name: 'day3', value: 0 },
        { name: 'day4', value: 0 },
        { name: 'day5', value: 0 },
        { name: 'day6', value: 0 },
        { name: 'day7', value: 0 }
      ],
      options: { title: '学习进度图表' }
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_progress/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => { // Map<> 格式
        if (result.info === 'success') {
          this.$set(this.data, 0, { name: 'day1', value: parseInt(result.day1) })
          this.$set(this.data, 1, { name: 'day2', value: parseInt(result.day2) })
          this.$set(this.data, 2, { name: 'day3', value: parseInt(result.day3) })
          this.$set(this.data, 3, { name: 'day4', value: parseInt(result.day4) })
          this.$set(this.data, 4, { name: 'day5', value: parseInt(result.day5) })
          this.$set(this.data, 5, { name: 'day6', value: parseInt(result.day6) })
          this.$set(this.data, 6, { name: 'day7', value: parseInt(result.day7) })
          console.log('data:', this.data[6])
          this.percent = parseInt(result.percent)
        } else {
          alert('获取进度信息失败')
        }
      },
      error: function () {
        alert('获取进度信息失败')
      }
    })
  }
}
</script>

<style scoped>

</style>
