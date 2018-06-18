<template>
  <div style="cursor: default">
    <el-button type="text" @click="dialogFormVisible = true">注册</el-button>
    <el-dialog title="注册新用户" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="signup_form" status-icon :rules="signup_rules">
        <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
          <el-input v-model="form.email" placeholder="输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
          <el-input v-model="form.password" placeholder="输入密码" type="password">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="reenter">
          <el-input v-model="form.reenter" placeholder="再次输入密码" type="password">
          </el-input>
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="form.username" placeholder="输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" :label-width="formLabelWidth" prop="phone">
          <el-input v-model="form.phone" placeholder="输入电话号码"></el-input>
        </el-form-item>
        <el-select v-model="form.education" placeholder="请选择学历"><!-- 下拉框选择器 -->
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit('signup_form')">注 册</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'SignUpDialog',
  data () {
    let emailCheck = (rule, value, callback) => {
      // 验证邮箱地址是否合法的另一种正则表达式
      // var reg=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
      let emailRE = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/
      if (emailRE.test(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    let passwordCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      }
      // 也可以6-20个数字 字母 下划线 /^(\w){6,20}$/
      let pwdRE = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/
      if (pwdRE.test(value)) {
        callback()
      } else {
        callback(new Error('密码格式错误：最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符'))
      }
    }
    let reenterCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    let usernameCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else {
        let usernameRE = /^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){4,19}$/
        if (usernameRE.test(value)) {
          callback()
        } else {
          callback(new Error('用户名格式错误：5-20个以字母开头，可包含数字和下划线的字符'))
        }
      }
    }
    let phoneCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入电话号码'))
      } else {
        let phoneRE = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/
        if (phoneRE.test(value)) {
          callback()
        } else {
          callback(new Error('电话号码格式错误'))
        }
      }
    }
    return {
      dialogFormVisible: false,
      formLabelWidth: '120px',
      options: [
        { value: 'primary', label: '小学' },
        { value: 'junior', label: '初中' },
        { value: 'senior', label: '高中' },
        { value: 'undergraduate', label: '本科' },
        { value: 'graduate', label: '本科以上' }
      ],
      form: {
        email: '',
        password: '',
        reenter: '',
        username: '',
        phone: '',
        education: ''
      },
      signup_rules: {
        email: [{ validator: emailCheck, trigger: 'blur' }],
        password: [{ validator: passwordCheck, trigger: 'blur' }],
        reenter: [{ validator: reenterCheck, trigger: 'blur' }],
        username: [{ validator: usernameCheck, trigger: 'blur' }],
        phone: [{ validator: phoneCheck, trigger: 'blur' }]
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
