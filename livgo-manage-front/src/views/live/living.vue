<template>
  <div class="container-wrap">
    <div class="container-box">
      <div class="search-box" style="width:1300px">
        <el-input placeholder="请输入主播名"  v-model="liveUsername"  style="width: 400px">
        </el-input>
        <el-input placeholder="请输入设备序列号"  v-model="deviceNumber"  style="width: 400px">
        </el-input>
        <el-button @click="handleIconClick" type="primary">查询</el-button>
        <!--<el-input placeholder="请输入关键字" icon="search" v-model="key_word" :on-icon-click="handleIconClick">-->
        <!--</el-input>-->
      </div>
      <div class="container-content">
        <el-table :data="living" border stripe tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="id" label="直播ID" width="150" align="center">
          <template scope="scope">
            <router-link :to="{ name: 'liveDetail', params:  { liveId: scope.row.id,readOnly:true}}" style="color: #20A0FF">{{scope.row.id}}</router-link>
          </template>
          </el-table-column>
          <el-table-column prop="liveName" label="主播" width="200" align="center">
          </el-table-column>
          <el-table-column prop="deviceNumber" label="设备序列号" width="200" align="center">
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="200" show-overflow-tooltip align="center">
            <template scope="scope">
              {{scope.row.startTime | date}}
            </template>
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间" width="180" align="center">
            <template scope="scope">
              {{scope.row.endTime | date}}
            </template>
          </el-table-column>

          <el-table-column prop="location" label="地理位置" width="250" align="center"></el-table-column>
          <el-table-column prop="typeDesc"  label="直播类型" width="180" show-overflow-tooltip align="center">
            <!--<template scope="scope">-->
              <!--{{scope.row.registerTime | date}}-->
            <!--</template>-->
          </el-table-column>
          <el-table-column prop="count" label="观众/受邀人数" width="180" align="center"></el-table-column>
          <el-table-column prop="audienceName" label="直播观众" width="360" align="center"></el-table-column>
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
  import LivingService from '../../service/living';
  import types from '../../store/mutation-types';
  export default {
    data() {

      return {
        currentPage:1,
        totalPage: 0,
        pageSize:18,
        totalCount: 0,
        key_word: '',
        living: [],
        statue:0,
        liveUsername:null,
        deviceNumber:null
      }
    },
    created () {
      this.fetchData();

    },
    watch: {
      '$route': 'fetchData'
    },
    computed: {
      keyWordUsers () {
        var arr = [];
        if(!this.key_word)
          return this.living;
        else{
          this.living.forEach((item,index) => {
            if(item.username.indexOf(this.key_word) > -1)
              arr.push(item);
          });
          return arr;
        }
      }
    },
    methods: {
      handleIconClick () {
        this.fetchData();
      },
      handleCurrentChange (val) {
        this.currentPage = val;
        this.fetchData();
      },
      /**获取当前直播列表数据*/
      fetchData () {
          let that = this;
        setTimeout( () => {
          LivingService.getList({
            page: that.currentPage-1,
            length: that.pageSize,
            filter: that.key_word,
            status:that.statue,
            liveUsername:that.liveUsername,
            deviceNumber:that.deviceNumber,
          }).then(function (response) {
            if(response.code == 0) {
              that.totalPage = response.totalPage;
              that.totalCount = response.data.totalCount;
              that.living = response.data.rows;
            }
            else that.$message({type: 'error', message: response.reason});
          });
        },300);
      }
    }
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
