<template>
  <div style="cursor: default">
    <el-button type="text" @click="dialogFormVisible = true">登录</el-button>
    <el-dialog title="用户登录" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="login_form" status-icon :rules="login_rules">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="form.username" placeholder="输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
          <el-input v-model="form.password" placeholder="输入密码" type="password">
          </el-input>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth">
          <el-button type="text" style="float: left">Forget your password?</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit('login_form')">登 录</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'LoginDialog',
  data () {
    var usernameCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    var passwordCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      dialogFormVisible: false,
      form: {
        username: '',
        password: ''
      },
      formLabelWidth: '120px',
      login_rules: {
        username: [{ validator: usernameCheck, trigger: 'blur' }],
        password: [{ validator: passwordCheck, trigger: 'blur' }]
      }
    }
  },
  methods: {
    submit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit')
        }
      })
      this.dialogFormVisible = false
    }
  }
}
</script>

<style scoped>

</style>
