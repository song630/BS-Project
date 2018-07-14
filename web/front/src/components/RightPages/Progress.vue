<template>
  <div>
    <!--
    <schart :canvasId="canvasId" :type="type" :width="width" :height="height" :data="data" :options="options">
    </schart>
    -->
    <!-- 为echarts准备一个具备大小的容器dom -->
    <div id="main1" style="width: 600px; height: 400px;"></div> <!-- 饼图 -->
    <br>
    <div id="main2" style="width: 600px; height: 400px;"></div> <!-- 柱状图 -->
  </div>
</template>

<script>
import echarts from 'echarts'
// import Schart from 'vue-schart'
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'Progress',
  // components: { Schart },
  data () {
    return {
      pieChart: '', // 饼图
      barChart: '', // 柱状图
      opinion: ['已学习', '待学习'], // 饼图图例
      opinionData: [ // 饼图的数据
        {value: 0, name: '已学习'}, // studied
        {value: 0, name: '待学习'} // toStudy
      ],
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
  methods: {
    drawPie (id) {
      this.pieChart = echarts.init(document.getElementById(id)) // 初始化
      this.pieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a}<br/>{b}:{c} ({d}%)'
        },
        legend: {
          orient: 'vertical', // 图例竖直排列
          x: 'left', // 放在饼图左侧
          data: this.opinion // 绑定图例
        },
        series: [
          {
            name: '单词书学习进度',
            type: 'pie',
            radius: ['50%', '70%'], // 饼图的环的大小
            avoidLabelOverlap: false,
            label: {
              normal: { show: false, position: 'center' },
              emphasis: {
                show: true,
                textStyle: { fontSize: '30', fontWeight: 'bold' }
              }
            },
            labelLine: { normal: { show: false } },
            data: this.opinionData // 设置绑定数据
          }
        ]
      })
    },
    drawBar (id) {
      this.barChart = echarts.init(document.getElementById(id)) // 初始化
      this.barChart.setOption({
        color: ['#3398DB'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器 坐标轴触发有效
            type: 'shadow' // 默认为直线 可选为'line' | 'shadow'
          }
        },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: [{ // x轴
          type: 'category',
          data: ['day1', 'day2', 'day3', 'day4', 'day5', 'day6', 'day7'],
          axisTick: { alignWithLabel: true }
        }],
        yAxis: [{ type: 'value' }],
        series: [{
          name: '学习进度记录',
          type: 'bar',
          barWidth: '60%',
          data: [
            this.data[0].value, this.data[1].value,
            this.data[2].value, this.data[3].value,
            this.data[4].value, this.data[5].value,
            this.data[6].value
          ]
        }]
      })
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
          this.opinionData[0].value = parseInt(result.studied)
          this.opinionData[1].value = parseInt(result.toStudy)

          this.$nextTick(function () { // 获取数据后画图
            this.drawPie('main1')
            this.drawBar('main2')
          })
        } else {
          alert('获取进度信息失败')
        }
      },
      error: function () {
        alert('获取进度信息失败')
      }
    })
  },
  beforeDestroy: function () {
    if (this.pieChart) {
      this.pieChart.dispose()
      this.pieChart = null
    }
    if (this.barChart) {
      this.barChart.dispose()
      this.barChart = null
    }
  }
}
</script>

<style scoped>

</style>
