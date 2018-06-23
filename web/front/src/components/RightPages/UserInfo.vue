<template>
    <div>
      <el-form ref="reset_form" :model="form" status-icon :rules="reset_rules" label-width="80px">
        <el-form-item label="用户名" prop="user">
          <el-input v-model="form.name" style="width: 300px; float: left;" disabled :placeholder="form.username"></el-input>
        </el-form-item>
        <template v-if="isModifyClicked">
        <el-form-item label="旧密码" prop="old_pwd">
          <el-input v-model="form.old_pwd" placeholder="输入旧密码" type="password">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="new_pwd">
          <el-input v-model="form.new_pwd" placeholder="输入新密码" type="password">
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="new_pwd_reenter">
          <el-input v-model="form.new_pwd_reenter" placeholder="再次输入新密码" type="password">
          </el-input>
        </el-form-item>
        </template>
        <el-form-item label="学历" prop="education">
          <el-select v-model="form.education" style="float: left;" :disabled="!isModifyClicked">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" :disabled="!isModifyClicked"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" :disabled="!isModifyClicked"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitReset('reset_form')">
            {{ isModifyClicked ? msg[1] : msg[0] }}
          </el-button>
          <DeleteDialog @delete_success="on_delete"></DeleteDialog>
          <el-button :disabled="!isModifyClicked" @click="isModifyClicked = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
import DeleteDialog from '../small/DeleteDialog'
import $ from 'jquery'
import { getCookie, delCookie } from '../../util.js'
export default {
  name: 'UserInfo',
  components: { DeleteDialog },
  data () {
    let emailCheck = (rule, value, callback) => {
      let emailRE = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
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
      let pwdRE = /^(\w){6,20}$/
      if (pwdRE.test(value)) {
        callback()
      } else {
        callback(new Error('密码格式错误：6-20位，可以包含数字、字母、下划线'))
      }
    }
    let reenterCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.new_pwd) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    let phoneCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号码'))
      } else {
        let phoneRE = /^[1][34578][0-9]{9}$/
        if (phoneRE.test(value)) {
          callback()
        } else {
          callback(new Error('手机号码格式错误'))
        }
      }
    }
    return {
      isModifyClicked: false,
      msg: ['修改信息', '提交修改'],
      options: [
        { value: 'primary', label: '小学' },
        { value: 'junior', label: '初中' },
        { value: 'senior', label: '高中' },
        { value: 'undergraduate', label: '本科' },
        { value: 'graduate', label: '本科以上' }
      ],
      formLabelWidth: '120px',
      form: {
        username: '',
        email: 'default',
        education: 'senior',
        phone: 'default',
        old_pwd: '',
        new_pwd: '',
        new_pwd_reenter: ''
      },
      reset_rules: {
        email: [{ validator: emailCheck, trigger: 'blur' }],
        new_pwd: [{ validator: passwordCheck, trigger: 'blur' }],
        new_pwd_reenter: [{ validator: reenterCheck, trigger: 'blur' }],
        phone: [{ validator: phoneCheck, trigger: 'blur' }]
      }
    }
  },
  methods: {
    on_delete: function () {
      delCookie('username')
      delCookie('isLogin')
      this.$router.replace('/') // 删除账号后自动回到首页
    },
    submitReset: function (formName) {
      if (this.isModifyClicked) { // 若按钮变为'提交修改'之后再按
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
            url: 'http://localhost:8080/Hello/submit_reset',
            crossDomain: true,
            xhrFields: {
              withCredentials: true
            },
            dataType: 'json',
            data: {obj: JSON.stringify(this.form)},
            success: (result) => {
              console.log('result:', result)
              if (result.info === 'success') {
                alert('修改成功')
              } else if (result.info === 'email_existed') {
                alert('邮箱已被注册')
              } else if (result.info === 'wrong_pwd') {
                alert('密码错误')
              } else {
                alert('更改失败')
              }
            },
            error: function () {
              alert('注册失败')
            }
          })
        }
      }
      this.isModifyClicked = true // ===
    }
  },
  mounted: function () {
    console.log('UserInfo mounted, cookies: ', document.cookie)
    console.log('UserInfo mounted, username: ', getCookie('username'))
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_userInfo/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => {
        console.log('UserInfo mounted, result:', result)
        this.form.username = result.username
        this.form.email = result.email
        this.form.education = result.education
        this.form.phone = result.phone
      },
      error: function () {
        alert('获取用户信息失败')
      }
    })
  }
}
</script>

<style scoped>

</style>
