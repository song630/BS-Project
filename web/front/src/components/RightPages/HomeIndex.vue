<template>
  <div style="text-align: left;">
    <img src="../../assets/WordBooksLogo.png" style="height: 32px; margin-bottom: 20px;">
    <el-collapse accordion>
      <el-collapse-item title="单词书">
        <div>提供不同类型的单词书供收藏：目前包含大学六级词汇、托福词汇和GRE词汇</div>
        <div>创建个性化单词书：可以选取单词加入自定义的单词书</div>
      </el-collapse-item>
      <el-collapse-item title="背单词">
        <div>系统背单词计划：网站提供科学的背单词方案</div>
        <div>自定义背单词计划：可以自定义背单词方案</div>
      </el-collapse-item>
      <el-collapse-item title="测试">
        <div>检测是否已经掌握记忆过的单词</div>
      </el-collapse-item>
      <el-collapse-item title="记录足迹">
        <div>单词量：根据测试结果计算已经掌握的单词数量</div>
        <div>成长曲线：记录一段时间内每天的学习情况</div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
export default {
  name: 'HomeIndex'
}
</script>

<style scoped>

</style>
