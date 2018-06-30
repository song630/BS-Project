<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane label="自定义单词书列表">
        <h2 v-if="tableData.length===0" style="font-weight: normal; color: #39c0dc">没有任何单词</h2>
        <el-table v-else :data="tableData" stripe style="width: 100%; text-align: left;">
          <el-table-column prop="word" label="单词" width="100">
          </el-table-column>
          <el-table-column prop="poses" label="释义">
          </el-table-column>
          <el-table-column label="删除单词">
            <template slot-scope="scope">
              <el-button type="warning" @click="deletePrivate(scope.row.word, scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="单词卡片复习">
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'PrivateTable',
  methods: {
    deletePrivate: function (theWord, index) {
      this.tableData.splice(index, 1) // 删除数组中的元素
      let submit = {
        word: theWord,
        username: getCookie('username')
      }
      $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/Hello/delete_private/',
        crossDomain: true,
        xhrFields: {
          withCredentials: true
        },
        dataType: 'json',
        data: {obj: JSON.stringify(submit)},
        success: (result) => { // 收到的是Map<>
          if (result.info === 'success') {
            alert('删除成功')
          } else {
            alert('删除单词失败')
          }
        },
        error: function () {
          alert('删除单词失败')
        }
      })
    }
  },
  data () {
    return {
      tableData: []
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_private/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => { // 收到的是List<>
        if (result === null) {
          alert('获取单词失败')
        } else {
          for (let i = 0; i < result.length; i++) {
            let p = result[i].poses
            p = p.replace(/\+\+/g, '')
            p = p.replace(/\*\*/g, '')
            this.tableData.push({ word: result[i].word, poses: p })
          }
        }
      },
      error: function () {
        alert('获取单词失败')
      }
    })
  }
}
</script>

<style scoped>
.el-table .warning-row {
  background: oldlace;
}
.el-table .success-row {
  background: #f0f9eb;
}
</style>
