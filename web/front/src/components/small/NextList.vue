<template>
  <div style="cursor: default">
    <el-button type="success" @click="dialogVisible = true">
      提交学习
    </el-button>
    <el-dialog title="提交学习" :visible.sync="dialogVisible" width="30%">
      <span style="color: red">点击确定记录已经完成的单词清单</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="nextClicked">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'NextList',
  data () {
    return { dialogVisible: false }
  },
  methods: {
    nextClicked: function () {
      this.dialogVisible = false
      this.$emit('nextList')
    }
  }
}
</script>

<style scoped>

</style>
