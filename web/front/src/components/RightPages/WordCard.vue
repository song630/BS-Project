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
        <el-button type="primary" @click="getNextOne" :disabled="!clicked || submit.indexOfTotal === totalNum + 1">下一个
          <i class="el-icon-arrow-right el-icon--right"></i>
        </el-button>
        <el-button type="success" @click="addPrivate">
          加入自定义单词书
        </el-button>
      </el-form-item>
      <el-form-item> <!-- 进度条 -->
        <el-progress :percentage="Math.round((submit.indexOfTotal - 1) / totalNum * 100)"></el-progress>
      </el-form-item>
      <el-form-item>
        <NextList v-if="submit.indexOfTotal === totalNum + 1 && clicked" @nextList="getNextList"></NextList>
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
        indexOfTotal: 1 // 当前的单词是这20个中的第几个(1-20) === 2018.7.15修改 原为0
      },
      list: {
        yes: [], // 掌握的单词
        no: [] // 未掌握的单词
      }
    }
  },
  methods: {
    addPrivate: function () {
      let submit = {
        word: this.word,
        studying: this.submit.studying,
        username: getCookie('username'),
        id: this.id // 单词书中的index
      };
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/add_private',
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {obj: JSON.stringify(submit)},
        success: (result) => { // Map<>类型
          if (result.info === 'duplicate') {
            alert('单词重复');
          } else if (result.info === 'success') {
            alert('添加成功');
          } else {
            alert('添加失败');
          }
        },
        error: function () {
          alert('添加失败')
        }
      })
    },
    addToYes: function () {
      this.clicked = true;
      this.submit.indexOfTotal++;
      console.log('addToYes, indexOfTotal:', this.submit.indexOfTotal);
      this.list.yes.push({ username: this.submit.user, status: 'yes', word: this.word, id: this.id });
    },
    addToNo: function () {
      this.clicked = true;
      this.submit.indexOfTotal++;
      console.log('addToNo, indexOfTotal:', this.submit.indexOfTotal);
      this.list.no.push({ username: this.submit.user, status: 'no', word: this.word, id: this.id });
    },
    getNextList: function () {
      let toSubmit = {
        yes: this.list.yes,
        no: this.list.no,
        indexOfTotal: this.submit.indexOfTotal,
        totalNum: this.totalNum
      };
      $.ajax({
        type: 'GET',
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
        data: {obj: JSON.stringify(toSubmit)},
        success: (result) => { // Map<>类型
          if (result.info === 'success') {
            // 重新回到这个页面 开始学习下一个list
            this.$router.push({path: this.$router.options.routes[4].children[0].path, query: {num: this.submit.num}});
            console.log('next list');
          } else {
            alert('提交清单失败');
          }
        },
        error: function () {
          alert('提交清单失败');
        }
      })
    },
    getNextOne: function () {
      this.clicked = false;
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
            this.totalNum = parseInt(result.totalNum);
            this.word = result.word;
            this.phonetic = result.phonetic;
            this.id = parseInt(result.id);
            this.poses = [];
            // console.log('poses:', result.poses);
            // console.log('poses.split:', result.poses.split('**{'));
            let arr1 = result.poses.split('**{'); // 分离所有词性
            for (let i = 0; i < arr1.length; i++) { // 遍历每个词性
              let pair = arr1[i].split('}++'); // pair: 一个词性和对应的所有释义
              if (i === 0) {
                pair[0] = pair[0].replace('{', ''); // 词性
              }
              if (pair.length === 1) { // === 如advantageous的poses只有{adj.}
                pair[0] = pair[0].replace('}', '');
                let temp = {pos: pair[0], exps: ['']};
                this.poses.push(temp);
              } else {
                let exps = pair[1].split('**++'); // 分离一个词性对应的所有释义
                exps[0] = exps[0].replace('++', '');
                exps[exps.length - 1] = exps[exps.length - 1].replace('**', '');
                let temp = {pos: pair[0], exps: exps};
                this.poses.push(temp);
              }
            }
            console.log('getNextOne, indexOfTotal:', this.submit.indexOfTotal, ', totalNum:', this.totalNum);
            console.log('getNextOne, id:', this.id);
          } else {
            alert('获取失败');
          }
        },
        error: function () {
          alert('获取失败');
        }
      });
    }
  },
  mounted: function () {
    this.submit.user = getCookie('username');
    this.submit.studying = getCookie('studying');
    this.submit.num = this.$route.query.num;
    this.getNextOne();
  }
}
</script>

<style scoped>

</style>
