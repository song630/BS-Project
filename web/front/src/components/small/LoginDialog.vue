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
import $ from 'jquery'
export default {
  name: 'LoginDialog',
  data () {
    let usernameCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        let usernameRE = /^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){4,19}$/
        if (usernameRE.test(value)) {
          callback()
        } else {
          callback(new Error('用户名格式错误：5-20个以字母开头，可包含数字和下划线的字符'))
        }
      }
    }
    let passwordCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      }
      let pwdRE = /^(\w){6,20}$/
      if (pwdRE.test(value)) {
        callback()
      } else {
        callback(new Error('密码格式错误：6-20位，可以包含数字、字母、下划线'))
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
    submit: function (formName) {
      let canSubmit = false
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          alert('输入有误')
          canSubmit = false
        } else {
          canSubmit = true
        }
      })
      if (canSubmit) {
        $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/Hello/submit_login',
          crossDomain: true,
          xhrFields: {
            withCredentials: true
          },
          dataType: 'json',
          data: {obj: JSON.stringify(this.form)},
          success: (result) => {
            console.log('result:', result)
            if (result.info === 'success') {
              alert('登录成功')
              this.dialogFormVisible = false
              // 改变上层组件显示的username (默认guest)
              this.$emit('login_success', this.form.username)
            } else if (result.info === 'wrong_pwd') {
              alert('密码错误')
              this.dialogFormVisible = true
            } else if (result.info === 'not_found') {
              alert('找不到用户')
              this.dialogFormVisible = true
            } else {
              alert('注册失败')
              this.dialogFormVisible = true
            }
          },
          error: function () {
            alert('登录失败')
            this.dialogFormVisible = true
          }
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
