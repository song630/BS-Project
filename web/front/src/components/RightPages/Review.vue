<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane label="列表复习">
        <h2 v-if="tableData.length===0">今天没有要复习的单词</h2>
        <el-table v-else :data="tableData" stripe style="width: 100%; text-align: left;">
          <el-table-column prop="word" label="单词" width="100">
          </el-table-column>
          <el-table-column prop="poses" label="释义">
          </el-table-column>
          <el-table-column prop="status" label="是否掌握" width="80">
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="单词卡片复习">
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
  .el-table .warning-row {
    background: oldlace;
  }
  .el-table .success-row {
    background: #f0f9eb;
  }
</style>

<script>
import $ from 'jquery'
import { getCookie } from '../../util.js'
export default {
  name: 'Review',
  data () {
    return {
      tableData: [] // 其中的元素都是{word: '', poses: '', status: ''}形式
    }
  },
  mounted: function () {
    $.ajax({
      type: 'GET',
      url: 'http://localhost:8080/Hello/get_daily/' + getCookie('username'),
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: 'json',
      data: {},
      success: (result) => { // 收到的是List<>
        if (result === null) {
          alert('获取daily单词失败')
        } else {
          for (let i = 0; i < result.length; i++) {
            let p = result[i].poses;
            p = p.replace(/\+\+/g, '');
            p = p.replace(/\*\*/g, '');
            this.tableData.push({ word: result[i].word, poses: p, status: result[i].status });
          }
        }
      },
      error: function () { alert('获取daily单词失败'); }
    })
  }
}
</script>
