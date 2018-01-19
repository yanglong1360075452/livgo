<template>

  <div class="container-wrap">
    <div class="container-box" >
      <div class="container-content">
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple-dark">
              <el-tag style="font-size: large" color="#20A0FF">直播详情</el-tag>
            </div>
          </el-col>
        </el-row>
        <div style="margin: 20px"></div>
        <el-row>
          <el-col :span="12">
            <div style="margin: 20px;"></div>
            <div class="grid-content">
              <el-form :inline="true" :model="live" class="demo-form-inline" label-width="100px">
                <el-form-item label="直播ID">
                  <el-input v-model="live.liveId" placeholder="直播ID" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="主播ID">
                  <el-input v-model="live.userId" placeholder="主播ID" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="主播人">
                  <el-input v-model="live.name" placeholder="主播人" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>

                <el-form-item label="观众人数">
                  <el-input v-model="live.audiences" placeholder="观众人数" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="开始时间">
                  <el-input v-model="live.startTime"  placeholder="开始时间" :disabled="true" style="width: 200px">
                    <template scope="scope">
                      {{scope.row.startTime | date}}
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item label="结束时间">
                  <el-input v-model="live.endTime" placeholder="结束时间" :disabled="readOnly" style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="观众人次">
                  <el-input v-model="live.audienceTime" placeholder="观众人次" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>

                <el-form-item label="平均驻留时间">
                  <el-input v-model="live.avgTime" placeholder="平均驻留时间" :disabled="readOnly" style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="直播类型">
                  <el-input v-model="live.typeDesc" placeholder="直播类型" :disabled="readOnly" style="width: 200px"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <img class="head-img" src="../../assets/images/header-img.jpg" alt="直播缩略图" style="height: 270px">
            </div>
          </el-col>
        </el-row>
        <div style="margin: 20px"></div>
        <el-row style="margin: 20px">
          <el-col :span="24">
            <div class="grid-content bg-purple-dark">
              <el-tabs type="card">
                <el-tab-pane label="当前直播观众">
                  <el-table :data="audience" border stripe style="width: 100%">
                    <el-table-column prop="userId" label="观众ID" align="center"></el-table-column>
                    <el-table-column prop="username" label="观众名字" align="center"></el-table-column>
                    <el-table-column prop="inviteTime" label="受邀请时间" show-overflow-tooltip
                                     align="center">
                      <template scope="scope">
                        {{scope.row.inviteTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="inTime" label="进入直播时间" align="center">
                      <template scope="scope">
                        {{scope.row.inTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="stayTime" label="驻留时间(s)" align="center"></el-table-column>
                  </el-table>
                  <div style="margin: 2% 42%">
                    <el-pagination
                      @current-change=" handleliveAuData"
                      :current-page="liveAuData.currentPage"
                      :page-size="liveAuData.pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="totalCount">
                    </el-pagination>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="历史直播观众">
                  <el-table :data="historyAudience" border stripe tooltip-effect="dark" style="width: 100%">
                    <el-table-column prop="userId" label="观众ID" align="center"></el-table-column>
                    <el-table-column prop="username" label="观众名字" align="center"></el-table-column>
                    <el-table-column prop="inviteTime" label="受邀请时间" show-overflow-tooltip
                                     align="center">
                      <template scope="scope">
                        {{scope.row.inviteTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="inTime" label="进入直播时间" align="center">
                      <template scope="scope">
                        {{scope.row.inTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="outTime" label="离开直播时间" align="center">
                      <template scope="scope">
                        {{scope.row.outTime | date}}
                      </template>
                    </el-table-column>

                    <el-table-column prop="stayTime" label="驻留时间(s)" align="center"></el-table-column>
                  </el-table>
                  <div style="margin: 2% 42%">
                    <el-pagination
                      @current-change=" handleliveHistoryAuData"
                      :current-page="liveHistoryAuData.currentPage"
                      :page-size="liveHistoryAuData.pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="totalCount">
                    </el-pagination>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
  import LivingService from '../../service/living';
  import Util from '../../assets/lib/util';

  export default {
    data() {
      return{
        currentPage:1,
        totalPage: 0,
        pageSize:18,
        liveId:0,
        totalCount: 0,
        readOnly: true,
        live: {},
        audience:[],
        historyAudience:[],
        liveAuData: {
          currentPage: 1,
          pageSize: 10,
          liveId: null
        },
        liveHistoryAuData: {
          currentPage: 1,
          pageSize: 10,
          liveId: null
        },
      }
    },
    created(){
      this.live.id = this.$route.params.liveId;
      this.readOnly = this.$route.params.readOnly;
      this.liveHistoryAuData.liveId=this.live.id;
      this.liveAuData.liveId=this.live.id;
      this.liveId=this.live.id;
      this.getLive(this.liveId);
      this.getLiveAudience(this.liveAuData);
      this.getHistoryAudience(this.liveHistoryAuData);
    },
    methods:{
      getLive(liveId){
          let that = this;
          LivingService.get(liveId).then(function (response) {
            if(response.code == 0) {
              that.live = response.data;
              that.live.startTime=Util.msToDate(response.data.startTime);
              that.live.endTime=Util.msToDate(response.data.endTime);
            }else{
              that.$message({type: 'error', message: response.reason});
            }
          })

      },
      handleliveAuData (val) {
        this.liveAuData.currentPage = val;
        this.getLiveAudience(this.liveAuData);

      },
      handleliveHistoryAuData (val) {
        this.liveHistoryAuData.currentPage = val;
        this.getHistoryAudience(this.liveHistoryAuData);

      },
      getLiveAudience(data){
        let that = this;
        LivingService.getAudience(data).then(function (response) {
          if(response.code == 0) {
            that.totalPage = response.totalPage;
            that.totalCount = response.data.totalCount;
            that.audience = response.data.rows;
          }else{
            that.$message({type: 'error', message: response.reason});
          }
        })
      },
      getHistoryAudience(data){
        let that = this;
        LivingService.getHistoryAudience(data).then(function (response) {
          if(response.code == 0) {
            that.totalPage = response.totalPage;
            that.totalCount = response.data.totalCount;
            that.historyAudience = response.data.rows;
            console.log("*****************");
            console.log(that.historyAudience);
          }else{
            that.$message({type: 'error', message: response.reason});
          }
        })
      }

    }
  }
</script>
