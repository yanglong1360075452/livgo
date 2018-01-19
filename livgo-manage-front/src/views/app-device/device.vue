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
        <el-table :data="device" border stripe tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="id" label="设备id" width="200" align="center">
            <template scope="scope">
              <router-link :to="{ name: 'devices', params:  { id: scope.row.id,readOnly:true}}" style="color: #20A0FF">{{scope.row.id}}</router-link>
            </template>
          </el-table-column>
          <el-table-column prop="deviceId" label="设备标识" width="300" show-overflow-tooltip align="center"> </el-table-column>
          <el-table-column prop="deviceName" label="设备名称" width="320" align="center"> </el-table-column>
          <el-table-column prop="deviceModule"  label="设备型号" width="300" align="center"></el-table-column>
          <el-table-column prop="userName" label="绑定用户" width="255" align="center"></el-table-column>
          <el-table-column prop="bindTime" label="绑定时间" width="300" align="center">
            <template scope="scope">
              {{scope.row.bindTime | date}}
            </template>
          </el-table-column>

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
  import DeviceService from '../../service/device';
  import types from '../../store/mutation-types';
  export default {
    data() {
      return {
        currentPage: 1,
        totalPage: 0,
        pageSize: 18,
        totalCount: 0,
        key_word:'',
        device: []
      }
    },created(){
      this.getDeviceList();
    },
     methods:{
      handleIconClick(){
        this.getDeviceList();
      },
      handleCurrentChange (val) {
        this.currentPage = val;
        this.getDeviceList();
      },
       getDeviceList (route) {
          let that = this;
         var tab = route ? route.query.tab: that.$route.query.tab;
         setTimeout( () => {
           DeviceService.getList({
             tab: tab,
             page: that.currentPage-1,
             length: that.pageSize,
             filter: that.key_word
           }).then(function (response) {
             if(response.code == 0) {
               that.device = response.data.rows;
               that.totalPage = response.totalPage;
               that.totalCount = response.data.totalCount;
             }
             else that.$message({type: 'error', message: response.reason});
           });
         },300);
       }
    }

  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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

