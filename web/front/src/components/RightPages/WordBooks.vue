<template>
  <div>
  <!-- 若干个卡片 -->
  <el-card class="box-card" v-for="(item, index) in booksInfo" :key="index" style="margin-bottom: 20px;">
    <div slot="header" class="clearfix">
      <span style="float: left">书名：{{ item.title }} 单词量：{{ item.num }}</span>
      <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
    </div>
    <!-- 加入几个按钮 开始学习 开始背单词 制定计划-有推荐计划 -->
    <ChangeStudying v-if="item.title !== studying && item.title !== 'Private'" @changeStudying="change(item.title)"></ChangeStudying>
    <!-- 通过路由传递参数 -->
    <el-button type="primary" style="float: left;" @click="viewWordBook(item)">
      浏览单词书
    </el-button>
    <el-button type="primary" v-if="item.title === studying" :disabled="item.title === 'Private'" style="float: left;" @click="$router.push($router.options.routes[4].children[0].path)">
      开始背单词
    </el-button>
    <el-button type="primary" v-if="item.title === studying" :disabled="item.title === 'Private'" style="float: left;" @click="$router.push($router.options.routes[3].children[0].path)">
      制定计划
    </el-button>
  </el-card>
</div>
</template>

<script>
import ChangeStudying from '../small/ChangeStudying'
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'WordBooks',
  components: { ChangeStudying },
  methods: {
    viewWordBook: function (info) {
      if (info.title !== 'Private') { // 非自定义单词书
        this.$router.push({path: this.$router.options.routes[2].children[1].path, query: {num: info.num, title: info.title}})
      } else { // 自定义单词书
        this.$router.push({path: this.$router.options.routes[2].children[2].path, query: {num: info.num, title: info.title}})
      }
    },
    change: function (bookTitle) { // 后端更改cookie
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/change_book/' + bookTitle,
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {},
        success: (result) => { // result是java中的Map<>
          if (result.info === 'success') {
            this.studying = bookTitle
          } else {
            alert('改变单词书失败')
          } // ===== 注意 后期加上 不能选择自定义单词书 =====
        },
        error: function () {
          alert('改变单词书失败')
        }
      })
    }
  },
  data () {
    return {
      booksInfo: [], // 用后端的数据填充
      numBooks: 0,
      studying: ''
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_books/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => { // 收到一个新cookie表示当前正在学习的单词书
        // 接收格式如下 [{…}, {…}, {…}]
        // {title: "CET6WordBook", num: 2083}
        // {title: "GREWordBook", num: 3063}
        // {title: "TOEFLWordBook", num: 1300}
        console.log('WordBooks mounted, result:', result);
        console.log('WordBooks, cookies:', document.cookie);
        this.numBooks = result.length;
        this.booksInfo = Array(0).concat(result); // 数组拼接
        // ===== 注意 后期加上 登出后要删除相应的cookie =====
        this.studying = getCookie('studying'); // 后端设置了cookie
      },
      error: function () { alert('单词书加载失败'); }
    })
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
}
.item {
  margin-bottom: 18px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.box-card {
  width: 480px;
}
.el-button {
  margin-bottom: 20px;
  margin-right: 10px;
}
</style>
