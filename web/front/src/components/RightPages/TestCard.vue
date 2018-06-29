<template>
  <div>
    <el-form label-width="80px">
      <el-form-item>
        <h3>{{ word }}</h3>
        <p>{{ phonetic }}</p>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button type="success" @click="clickYes" :disabled="clicked" icon="el-icon-success">
            认识
          </el-button>
          <el-button type="warning" @click="clickNo" :disabled="clicked">
            不认识<i class="el-icon-error el-icon--right"></i>
          </el-button>
        </el-button-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getNextOne" :disabled="!clicked">下一个
          <i class="el-icon-arrow-right el-icon--right"></i>
        </el-button>
      </el-form-item>
      <el-form-item> <!-- 进度条 -->
        <el-progress :percentage="Math.round((indexOfTotal + 1) / total * 100)"></el-progress>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'TestCard',
  data () {
    return {
      total: 30,
      indexOfTotal: 0,
      word: '',
      phonetic: '',
      words: [],
      phonetics: [],
      get: 0, // 掌握的单词数
      clicked: false
    }
  },
  methods: {
    clickYes: function () {
      this.get++
      this.clicked = true
    },
    clickNo: function () {
      this.clicked = true
    },
    getNextOne: function () {
      if (this.indexOfTotal === this.total - 1) { // 完成测试
        this.$router.push({path: this.$router.options.routes[7].children[2].path, query: {num: this.get}})
        console.log('test done.')
      } else {
        this.indexOfTotal++
        this.word = this.words[this.indexOfTotal]
        this.phonetic = this.phonetics[this.indexOfTotal]
        this.clicked = false
      }
    }
  },
  mounted: function () {
    $.ajax({
      ype: 'GET',
      url: 'http://localhost:8080/Hello/get_test/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => {
        if (result === null) {
          alert('获取测试数据失败')
        } else {
          for (let i = 0; i < result.length; i += 2) {
            this.words.push(result[i])
            this.phonetics.push(result[i + 1])
          }
        }
        this.word = this.words[0]
        this.phonetic = this.phonetics[0]
        console.log('TestCard, words:', this.words)
      },
      error: function () {
        alert('获取测试数据失败')
      }
    })
  }
}
</script>

<style scoped>

</style>
