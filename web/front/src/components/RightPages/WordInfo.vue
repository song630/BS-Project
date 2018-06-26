<template>
  <div>
    <el-form ref="reset_form" :model="form" status-icon :rules="reset_rules" label-width="80px">
      <el-form-item>
        <el-button-group> <!-- 按钮组翻页 -->
          <el-button type="primary" icon="el-icon-arrow-left">上一页</el-button>
          <el-button type="primary">下一页<i class="el-icon-arrow-right el-icon--right"></i></el-button>
        </el-button-group>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'WordInfo'
}
</script>

<style scoped>

</style>
