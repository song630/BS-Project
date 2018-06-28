<template>
  <div>
    <el-form label-width="80px">
      <el-form-item>
        <h3>{{ word }}</h3>
        <p>{{ phonetic }}</p>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button type="success" @click="clicked = true" :disabled="clicked" icon="el-icon-success">
            认识
          </el-button>
          <el-button type="warning" @click="clicked = true" :disabled="clicked">
            不认识<i class="el-icon-error el-icon--right"></i>
          </el-button>
        </el-button-group>
      </el-form-item>
      <el-form-item>
        <el-collapse accordion>
          <el-collapse-item title="释义">
            <div v-for="(item0, i0) in poses" :key="i0" style="text-align: left;">
              <div>{{ item0.pos }}</div>
              <div v-for="(item1, i1) in item0.exps" :key="i1">{{ item1 }}</div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getNextOne" :disabled="!clicked">下一个
          <i class="el-icon-arrow-right el-icon--right"></i>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'WordCard',
  data () {
    return {
      totalNum: 20, // 实际在这个list中要遍历显示的单词数量
      clicked: false,
      word: '',
      phonetic: '',
      poses: null, // 释义
      reachEnd: false, // 到达了这个list的结尾
      studying: '',
      submit: {
        studying: '',
        user: '',
        num: 0,
        indexOfTotal: 0 // 当前的单词是这20个中的第几个(0-19)
      }
    }
  },
  methods: {
    getNextOne: function () {
      this.clicked = false
      if (this.reachEnd === true) { // 重新回到这个页面 开始学习下一个list
        // alert('当前list已经完成学习')
        this.$router.push({path: this.$router.options.routes[4].children[0].path, query: {num: this.submit.num}})
        console.log('next list')
      } else {
        $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/Hello/memorize',
          crossDomain: true,
          xhrFields: {
            withCredentials: true
          },
          dataType: 'json',
          data: {obj: JSON.stringify(this.submit)},
          success: (result) => {
            if (result.info === 'success') {
              this.totalNum = result.totalNum
              this.word = result.word
              this.phonetic = result.phonetic
              this.submit.indexOfTotal++
              this.poses = []
              let arr1 = result.poses.split('**{')
              for (let i = 0; i < arr1.length; i++) {
                let pair = arr1[i].split('}++')
                if (i === 0) {
                  pair[0] = pair[0].replace('{', '')
                }
                let exps = pair[1].split('**++')
                exps[0] = exps[0].replace('++', '')
                exps[exps.length - 1] = exps[exps.length - 1].replace('**', '')
                let temp = {pos: pair[0], exps: exps}
                this.poses.push(temp)
              }
            } else if (result.info === 'end') {
              this.reachEnd = true
              this.submit.indexOfTotal = 0
            } else {
              alert('获取失败')
            }
          },
          error: function () {
            alert('获取失败')
          }
        })
      }
    }
  },
  mounted: function () {
    this.reachEnd = false
    this.submit.user = getCookie('username')
    this.submit.studying = getCookie('studying')
    this.submit.num = this.$route.query.num
    this.getNextOne()
  }
}
</script>

<style scoped>

</style>
