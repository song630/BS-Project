<template>
  <div>
    <div>
      <el-progress type="circle" :percentage="percent" color="#8e71c7"></el-progress>
    </div>
    <h2 style="font-weight: normal; color: #2ca8e6">测试结果</h2>
  </div>
</template>

<script>
export default {
  name: 'TestResult',
  data () {
    return { percent: 0 }
  },
  mounted: function () {
    this.percent = Math.round(this.$route.query.num * 100 / 30)
  }
}
</script>

<style scoped>

</style>
