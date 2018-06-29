<template>
  <div>
    <el-form label-width="80px">
      <el-form-item>
        <h3>{{ word }}</h3>
        <p>{{ phonetic }}</p>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button type="success" @click="addToYes" :disabled="clicked" icon="el-icon-success">
            认识
          </el-button>
          <el-button type="warning" @click="addToNo" :disabled="clicked">
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
        <el-button type="primary" @click="getNextOne" :disabled="!clicked || reachEnd">下一个
          <i class="el-icon-arrow-right el-icon--right"></i>
        </el-button>
      </el-form-item>
      <el-form-item> <!-- 进度条 -->
        <el-progress :percentage="Math.round(submit.indexOfTotal / totalNum * 100)"></el-progress>
      </el-form-item>
      <el-form-item>
        <NextList v-if="reachEnd && clicked" @nextList="getNextList"></NextList>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import NextList from '../small/NextList'
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'WordCard',
  components: { NextList },
  data () {
    return {
      totalNum: 20, // 实际在这个list中要遍历显示的单词数量
      clicked: false,
      word: '',
      id: 0, // 单词书中的index
      phonetic: '',
      poses: null, // 释义
      reachEnd: false, // 到达了这个list的结尾
      studying: '',
      submit: {
        studying: '',
        user: '',
        num: 0,
        indexOfTotal: 0 // 当前的单词是这20个中的第几个(0-19)
      },
      list: {
        yes: [], // 掌握的单词
        no: [] // 未掌握的单词
      }
    }
  },
  methods: {
    addToYes: function () {
      this.clicked = true
      this.list.yes.push({ username: this.submit.user, status: 'yes', word: this.word, id: this.id })
    },
    addToNo: function () {
      this.clicked = true
      this.list.no.push({ username: this.submit.user, status: 'no', word: this.word, id: this.id })
    },
    getNextList: function () {
      $.ajax({
        ype: 'GET',
        // === 后端做4件事:
        // 1.更新用户的day1-day7
        // 2.更新用户的最后一次登录日期
        // 3.将单词加入到daily表中
        // 4.更新studied字段(在getNextOne函数中已经实现)
        url: 'http://localhost:8080/Hello/submit_list',
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {obj: JSON.stringify(this.list)},
        success: (result) => { // Map<>类型
          if (result.info === 'success') {
            // 重新回到这个页面 开始学习下一个list
            this.$router.push({path: this.$router.options.routes[4].children[0].path, query: {num: this.submit.num}})
            console.log('next list')
          } else {
            alert('提交清单失败')
          }
        },
        error: function () {
          alert('提交清单失败')
        }
      })
    },
    getNextOne: function () {
      this.clicked = false
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
            this.totalNum = parseInt(result.totalNum)
            this.word = result.word
            this.phonetic = result.phonetic
            this.id = parseInt(result.id)
            this.submit.indexOfTotal++
            this.poses = []
            let arr1 = result.poses.split('**{')
            for (let i = 0; i < arr1.length; i++) {
              let pair = arr1[i].split('}++')
              if (i === 0) {
                pair[0] = pair[0].replace('{', '') // 词性
              }
              let exps = pair[1].split('**++') // 分离一个词性对应的所有释义
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
