<template>
  <div style="cursor: default">
    <el-button type="danger" icon="el-icon-delete" @click="dialogVisible = true" style="float: left; margin-left: 10px;">
      注销用户
    </el-button>
    <el-dialog title="注销用户" :visible.sync="dialogVisible" width="30%">
      <span style="color: red">注销用户会删除一切相关信息</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCookie } from '../../util.js'
import $ from 'jquery'
export default {
  name: 'DeleteDialog',
  data () {
    return { dialogVisible: false }
  },
  methods: {
    deleteUser () {
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/delete_user/' + getCookie('username'),
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {},
        success: (result) => {
          console.log('deleteUser, result:', result)
          if (result.info === 'success') {
            alert('注销成功')
            this.$emit('delete_success')
          } else {
            alert('注销失败')
          }
        },
        error: function () {
          alert('注销失败')
        }
      })
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>

</style>
