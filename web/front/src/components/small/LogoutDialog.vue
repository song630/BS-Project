<template>
  <div style="cursor: default">
    <el-button type="text" @click="buttonClicked">登出</el-button>
    <el-dialog title="退出用户" :visible.sync="dialogVisible" width="30%">
      <span>确认退出当前用户？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="log_out">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCookie } from '../../util.js'
export default {
  name: 'LogoutDialog',
  data () {
    return {
      dialogVisible: false
    }
  },
  methods: {
    buttonClicked () {
      let status = getCookie('isLogin')
      console.log('status: ', status)
      if (status === 'false') {
        alert('用户未登录')
      } else {
        this.dialogVisible = true
      }
    },
    log_out () {
      this.$emit('logout_success')
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>

</style>
