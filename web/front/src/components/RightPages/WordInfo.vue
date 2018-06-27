<template>
  <div>
    <el-alert :title="msg" type="info" show-icon style="margin-top: 10px; margin-bottom: 10px;"></el-alert>
    <el-form ref="reset_form" label-width="80px">
      <el-form-item>
        <h3>{{ word }}</h3>
        <p>{{ phonetic }}<a :href="pron" class="el-icon-service" style="margin-left: 10px;"></a></p>
      </el-form-item>
      <el-form-item>
        <h3>释义</h3>
        <div v-for="(item0, i0) in poses" :key="i0">
          <p>{{ item0.pos }}</p>
          <li v-for="(item1, i1) in item0.exps" :key="i1">{{ item1 }}</li>
        </div>
      </el-form-item>
      <el-form-item>
        <h3>例句</h3>
        <li v-for="(item, index) in sentences" :key="index">{{ item }}</li>
      </el-form-item>
      <el-form-item>
        <el-button-group> <!-- 按钮组翻页 -->
          <el-button type="primary" @click="getLastOne" :disabled="bookInfo.id === 0" icon="el-icon-arrow-left">
            上一页
          </el-button>
          <el-button type="primary" @click="getNextOne" :disabled="bookInfo.id === (bookInfo.num - 1)">
            下一页<i class="el-icon-arrow-right el-icon--right"></i>
          </el-button>
        </el-button-group>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'WordInfo',
  data () {
    return {
      word: '',
      phonetic: '',
      pron: '',
      poses: null,
      sentences: null,
      // =====
      bookInfo: {
        id: 0,
        title: '',
        num: 0
      },
      msg: ''
    }
  },
  mounted: function () {
    this.bookInfo.title = this.$route.query.title
    this.bookInfo.num = this.$route.query.num // 获取通过路由传递的参数
    this.msg = '书名：' + this.bookInfo.title + '  单词数：' + this.bookInfo.num
    this.getWordInfo()
  },
  methods: {
    getLastOne: function () {
      this.bookInfo.id--
      this.getWordInfo()
    },
    getNextOne: function () {
      this.bookInfo.id++
      this.getWordInfo()
    },
    getWordInfo: function () {
      console.log('getWordInfo, toSubmit:', this.bookInfo)
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/get_wordInfo',
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {obj: JSON.stringify(this.bookInfo)},
        success: (result) => {
          if (result === null) {
            alert('获取单词信息失败')
          } else {
            this.word = result.word
            this.phonetic = result.phonetic
            this.pron = result.pron
            // 分离例句
            let arr = result.sentences.split('**++')
            arr[0] = arr[0].replace('++', '')
            arr[arr.length - 1] = arr[arr.length - 1].replace('**', '')
            this.sentences = Array(0).concat(arr)
            // 分离词性和释义
            this.poses = []
            let arr1 = result.poses.split('**{') // 一个词性和若干释义为一个数组中的元素
            for (let i = 0; i < arr1.length; i++) {
              let pair = arr1[i].split('}++')
              if (i === 0) {
                pair[0] = pair[0].replace('{', '')
              }
              console.log('WordInfo, pair[0]:', pair[0])
              let exps = pair[1].split('**++') // 一个词性对应的若干释义
              console.log('WordInfo, exps:', exps)
              exps[0] = exps[0].replace('++', '')
              exps[exps.length - 1] = exps[exps.length - 1].replace('**', '')
              let temp = { pos: pair[0], exps: exps }
              this.poses.push(temp)
            }
          }
        },
        error: function () { alert('获取单词信息失败') }
      })
    }
  }
}
</script>

<style scoped>
.el-form-item {
  text-align: left;
}
</style>
