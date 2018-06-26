<template>
  <div style="cursor: default">
    <el-button type="success" @click="dialogVisible = true">
      开始学习
    </el-button>
    <el-dialog title="切换单词书" :visible.sync="dialogVisible" width="30%">
      <span style="color: red">改变正在学习的单词书会删除之前的学习计划</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="changeClicked">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ChangeStudying',
  data () {
    return { dialogVisible: false }
  },
  methods: {
    changeClicked: function () {
      this.dialogVisible = false
      this.$emit('changeStudying')
    }
  }
}
</script>

<style scoped>

</style>
