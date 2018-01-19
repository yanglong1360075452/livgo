<template>
  <div class="container-wrap">
    <div class="container-box">
      <div class="container-content">
        <div class="block">
          <span class="demonstration">选择时间范围</span>
          <el-date-picker
            v-model="value"
            type="daterange"
            align="right"
            placeholder="选择日期范围"
            :picker-options="pickerOptions2">
          </el-date-picker>
          <el-input placeholder="请输入用户名"  v-model="username"  style="width: 400px">
          </el-input>
          <el-input placeholder="请输入ip地址"  v-model="ipAddress"  style="width: 400px">
          </el-input>
          <el-button @click="handleIconClick" type="primary">查询</el-button>
        </div>

        <el-table :data="accessLogs" border stripe tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="createTime" label="操作时间" width="180" align="center">
            <template scope="scope">
              {{scope.row.createTime | date}}
            </template>
          </el-table-column>
          <el-table-column prop="createBy" label="用户id" width="200" align="center"></el-table-column>
          <el-table-column prop="createByDesc" label="用户名" width="200" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="userIp" label="访问IP" width="180" align="center"></el-table-column>
          <el-table-column prop="phone" label="手机型号" width="250" align="center"></el-table-column>
          <el-table-column prop="appVersion"  label="APP版本" width="180" show-overflow-tooltip align="center"></el-table-column>
          <el-table-column prop="operationDesc" label="事件" width="180" align="center"></el-table-column>
          <el-table-column prop="note" label="事件内容" align="center">
            <template scope="scope">
              <el-button @click="detail(scope.row)" type="info" size="small">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog title="请求和响应信息" :visible.sync="dialogFormVisible">
        <el-form :model="detailLog" :inline="true" class="demo-form-inline">
          <el-form-item label="请求地址和参数">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              style="width: 800px"
              v-model="detailLog.requestMessage">
            </el-input>
          </el-form-item>
          <br>
          <el-form-item label="响应状态吗">
            <el-input v-model="detailLog.responseStatue"></el-input>
          </el-form-item>
          <br>
          <el-form-item label="响应数据">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入内容"
              style="width: 800px"
              v-model="detailLog.responseMessage">
            </el-input>
          </el-form-item>
        </el-form>

      </el-dialog>
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
  import LogService from '../../service/log';
  import types from '../../store/mutation-types';
  import Util from '../../assets/lib/util';
  export default {

    data() {
      return {
        dialogFormVisible:false,
        currentPage: 1,
        totalPage: 0,
        pageSize: 18,
        totalCount: 0,
        key_word: '',
        key_word1: '',
        accessLogs: [],
        detailLog:{},
        username:null,
        ipAddress:null,
        pickerOptions2: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        value: '',
      }
    },
    created () {
      this.logsData();
    },
    watch: {
      '$route': 'logsData',
      'value' : function (newValue) {
        var createTime= Util.msToDate(newValue[0]);
        var endTime= Util.msToDate(newValue[1]);
        if(createTime==undefined){
                 createTime=null;
        }
        if(endTime==undefined){
          endTime=null;
        }
        console.log("开始时间是");
        console.log(createTime);
        console.log("结束时间是");
        console.log(endTime);
        this.createTime = (new Date(createTime)).getTime();
        this.endTime = (new Date(endTime)).getTime();
      }
    },
    computed: {
      keyWordUsers () {
        var arr = [];
        if(!this.key_word||!this.key_word1)
          return this.accessLogs;
        else{
          this.accessLogs.forEach((item,index) => {
            if(item.username.indexOf(this.key_word) > -1||item.userIp.indexOf(this.key_word1) > -1)
              arr.push(item);
          });
          return arr;
        }
      }
    },
    methods: {
      detail(row){
        console.log(103);
        console.log(row);
        this.dialogFormVisible=true;
        this.detailLog=row;



      },

      handleIconClick () {
        this.logsData();
      },
      handleCurrentChange (val) {
        this.currentPage = val;
        this.logsData();
      },
      /**获取访问日志列表数据*/
      logsData (route) {
          let that = this;
        setTimeout( () => {
          LogService.getAccessLogs({
            page: that.currentPage-1,
            length: that.pageSize,
            username: that.username,
            ipAddress:that.ipAddress,
            createTime: that.createTime,
            endTime: that.endTime
          }).then(function (response) {
            if(response.code == 0) {
              that.accessLogs = response.data.rows;
              console.log("****")
              console.log(that.accessLogs);
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
