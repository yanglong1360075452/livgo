<template>
  <div class="container-wrap">
    <div class="container-box">
      <div class="">

      </div>
      <div class="search-box">
        <el-input placeholder="请输入关键字" icon="search" v-model="key_word" :on-icon-click="handleIconClick">
        </el-input>
      </div>
      <div class="container-content">
        <el-table :data="accessLogs" border stripe tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="createTime" label="短信发送时间" width="180" align="center"></el-table-column>
          <el-table-column prop="createBy" label="发送者" width="200" align="center"></el-table-column>
          <el-table-column prop="createByDesc" label="短信内容" width="500" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="userIp" label="目标手机号" width="180" align="center"></el-table-column>
          <el-table-column prop="phone" label="接受状态" width="250" align="center"></el-table-column>
          <el-table-column prop="appVersion"  label="错误信息"  show-overflow-tooltip align="center"></el-table-column>
        </el-table>
      </div>
      <div class="page-wrap">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="totalCount">
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
  import Util from '../../assets/lib/util';
  import types from '../../store/mutation-types';
  export default {

  }



</script>
<style lang="scss">
  @import "../../assets/scss/define";
  .container-content{
  @extend %pa;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 70px 20px;
    overflow-x: hidden;
    overflow-y: auto;
  }
  .search-box{
  @extend %pa;
    top: 20px;
    right: 40px;
    z-index: 1;
    width: 360px;
  }
  .container-box{
  @extend %h100;
  }
  .page-wrap{
  @extend %pa;
  @extend %tac;
    background-color: #fff;
    z-index: 1;
    left: 0;
    right: 0;
    padding: 10px 0 20px;
    bottom: 0;
  }
</style>
