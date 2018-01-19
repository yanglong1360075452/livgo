<template>

  <div class="container-wrap">
    <div class="container-box">
      <div class="container-content">
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple-dark">
              <el-tag style="font-size: large" color="#20A0FF">用户详情</el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-dark" align="right">
              <el-button type="info" @click="edit">{{readOnly ? '编辑' : '保存'}}</el-button>
              <el-button type="info" @click="resetPassword = true">重置密码</el-button>

              <el-dialog :visible.sync="resetPassword" size="tiny" title="重置密码">
                <el-form :model="reset" :rules="resetRule" ref="reset">
                  <el-form-item label="新密码" prop="pass">
                    <el-input type="password" v-model="reset.pass" auto-complete="off" placeholder="请输入密码"></el-input>
                  </el-form-item>
                  <el-form-item label="确认密码" prop="checkPass">
                    <el-input type="password" v-model="reset.checkPass" auto-complete="off"
                              placeholder="请再次输入密码"></el-input>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">

                  <el-button type="primary" @click="submitForm('reset')">提交</el-button>
                  <el-button @click="resetForm('reset')">取消</el-button>
                </div>
              </el-dialog>
            </div>
          </el-col>
        </el-row>
        <div style="margin: 20px"></div>
        <el-row>
          <el-col :span="12">
            <div style="margin: 20px;"></div>
            <div class="grid-content">
              <el-form :inline="true" :model="user" class="demo-form-inline" label-width="100px">
                <el-form-item label="用户ID">
                  <el-input v-model="user.id" placeholder="用户ID" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="受邀时间">
                  <el-input v-model="user.beInviteTime" placeholder="受邀时间" :disabled="true"
                            style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="用户名">
                  <el-input v-model="user.username" placeholder="用户名" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="注册时间">
                  <el-input v-model="user.registerTime" placeholder="注册时间" :disabled="true"
                            style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="昵称">
                  <el-input v-model="user.nickname" placeholder="昵称" :disabled="readOnly"
                            style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="好友数量">
                  <el-input v-model="friendListCount" placeholder="好友数量" :disabled="true"
                            style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="性别">
                  <el-select v-model="user.sex" placeholder="请选择" :disabled="readOnly">
                    <el-option v-for="item in sexList" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="手机号码">
                  <el-input v-model="user.phone" placeholder="手机号码" :disabled="readOnly"
                            :rules="[
                            { required: true, message: '请输入手机号码', trigger: 'blur' },
                            { type: 'phone', message: '请输入正确的手机号码', trigger: 'blur,change' }
                            ]"
                            style="width: 200px"
                  ></el-input>
                </el-form-item>
                <br>
                <el-form-item label="状态">
                  <el-select v-model="user.status" placeholder="请选择" :disabled="readOnly">
                    <el-option v-for="item in statusList" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="绑定设备">
                  <el-input v-model="device" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple-light">
              <img class="head-img" src="../../assets/images/header-img.jpg" alt="头像" style="height: 270px">
            </div>
          </el-col>
        </el-row>
        <div style="margin: 20px"></div>
        <el-row style="margin: 20px">
          <el-col :span="24">
            <div class="grid-content bg-purple-dark">
              <el-tabs type="card">
                <el-tab-pane label="好友列表">
                  <el-table :data="friendList" border stripe style="width: 100%">
                    <el-table-column prop="id" label="好友ID" align="center"></el-table-column>
                    <el-table-column prop="username" label="好友用户名" align="center">
                      <template scope="scope">
                        <router-link :to="{ name: 'user', params:  { id: scope.row.id,readOnly:true}}"
                                     style="color: #20A0FF">
                          {{scope.row.username}}
                        </router-link>
                      </template>
                    </el-table-column>
                    <el-table-column prop="nickname" label="好友昵称" show-overflow-tooltip
                                     align="center"></el-table-column>
                    <el-table-column prop="sexDesc" label="性别" align="center"></el-table-column>
                    <el-table-column prop="phone" label="手机号码" align="center"></el-table-column>
                  </el-table>
                  <div style="margin: 2% 42%">
                    <el-pagination
                      @current-change="handleCurrentChange"
                      :current-page="pageData.currentPage"
                      :page-size="pageData.pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="totalCount">
                    </el-pagination>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="直播列表">
                  <el-table :data="liveList" border stripe tooltip-effect="dark" style="width: 100%">
                    <el-table-column prop="liveId" label="直播id" align="center"></el-table-column>
                    <el-table-column prop="name" label="直播用户名" align="center">
                    </el-table-column>
                    <el-table-column prop="typeDesc" label="直播类型" show-overflow-tooltip
                                     align="center"></el-table-column>
                    <el-table-column prop="fCounts" label="好友数量" align="center"></el-table-column>
                    <el-table-column prop="audiences" label="观众数量" align="center"></el-table-column>
                    <el-table-column prop="startTime" label="直播开始时间" align="center">
                      <template scope="scope">
                        {{scope.row.startTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="直播结束时间" align="center">
                      <template scope="scope">
                        {{scope.row.endTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="直播地理位置" align="center"></el-table-column>
                  </el-table>
                  <div style="margin: 2% 42%">
                    <el-pagination
                      @current-change="handleCurrentLivesChange"
                      :current-page="pageLivesData.currentPage"
                      :page-size="pageLivesData.pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="totalLivesCount">
                    </el-pagination>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="观看历史">

                  <el-table :data="liveHistory" border stripe tooltip-effect="dark" style="width: 100%">
                    <el-table-column prop="liveId" label="直播id" align="center"></el-table-column>
                    <el-table-column prop="username" label="直播用户名" align="center">
                    </el-table-column>
                    <el-table-column prop="typeDesc" label="直播类型" show-overflow-tooltip
                                     align="center"></el-table-column>
                    <el-table-column prop="inTime" label="进入时间" align="center">
                      <template scope="scope">
                        {{scope.row.inTime | date}}
                      </template>
                    </el-table-column>
                    <el-table-column prop="outTime" label="离开时间" align="center">
                      <template scope="scope">
                        {{scope.row.outTime | date}}
                      </template>
                    </el-table-column>
                  </el-table>
                  <div style="margin: 2% 42%">
                    <el-pagination
                      @current-change=" handleCurrentLivesHistoryChange"
                      :current-page="pageHistoryData.currentPage"
                      :page-size="pageHistoryData.pageSize"
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
  import UserService from '../../service/user';
  import Util from '../../assets/lib/util';
  import ElInput from "../../../node_modules/element-ui/packages/input/src/input";

  export default {
    components: {ElInput},
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.reset.checkPass !== '') {
            this.$refs.reset.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.reset.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        friendListCount:0,
        liveHistory:[],
        friendList:[],
        liveList:[],
        device: null,
        resetPassword: false,
        totalPage: 0,
        totalCount: 0,

        totalLivesPage: 0,
        totalLivesCount: 0,
        readOnly: true,
        user: {},
        statusList: this.GLOBAL.userStatusList,
        sexList: this.GLOBAL.userSexList,
        pageData: {
          currentPage: 1,
          pageSize: 10,
          id: null
        },

        pageLivesData: {
          currentPage: 1,
          pageSize: 10,
          userId: null
        },

        pageHistoryData: {
          currentPage: 1,
          pageSize: 10,
          userId: null
        },
        reset: {
          pass: '',
          checkPass: ''
        },
        resetRule: {
          pass: [
            {validator: validatePass, trigger: 'blur'}
          ],
          checkPass: [
            {validator: validatePass2, trigger: 'blur'}
          ]
        }
      }
    },
    //      this.getSeeLives(this.pageData);
    created () {
      this.user.id = this.$route.params.id;
      this.readOnly = this.$route.params.readOnly;
      this.getUser(this.user.id);
      this.pageData.id = this.user.id;
      this.pageLivesData.userId= this.user.id;
      this.pageHistoryData.userId=this.user.id;
      this.getUserFriends(this.pageData);
      this.getLives(this.pageLivesData);
      this.getSeeLives(this.pageHistoryData);

    },
//    watch: {
//      '$route' (to) {
//        this.getUser(to.params.id);
//        this.pageData.id = to.params.id;
//        this.getUserFriends(this.pageData);
//       this.getLives(this.pageDatalives);
////        this.getSeeLives(this.pageData);
//      }
//    },
    methods: {

      getUser(id){
        let that = this;
        UserService.get(id)
          .then(function (response) {
            if (response.code == 0) {
              that.user = response.data;
              if (that.user.devices != undefined && that.user.devices[0] != null) {
                var bind = that.user.devices[0];
                if (bind != undefined) {
                  that.device = bind.deviceName;
                }
              } else {
                that.device = "无";
              }
              that.user.registerTime = Util.msToDate(response.data.registerTime);
              if (response.data.friends != null) {
                that.friendListCount = response.data.friends.length;
              } else {
                that.friendListCount = 0;
              }
            } else {
              that.$message({type: 'error', message: response.reason});
            }
          })
      },
      handleCurrentChange (val) {
        this.pageData.currentPage = val;
        this.getUserFriends(this.pageData);
      },
      handleCurrentLivesChange (val) {
        this.pageLivesData.currentPage = val;
        this.getLives(this.pageLivesData);

      },
      handleCurrentLivesHistoryChange (val) {
        this.pageHistoryData.currentPage = val;
        this.getSeeLives(this.pageHistoryData);

      },
      getUserFriends(data){
        var that = this;
        UserService.getFriends(data)
          .then(function (response) {
            if (response.code == 0) {
              if(response.data != null){
                that.friendList = response.data.rows;
                that.totalPage = response.data.totalPage;
                that.totalCount = response.data.totalCount;
              }
            } else {
              that.$message({type: 'error', message: response.reason});
            }

          })
      },
      getLives(data){
        var that = this;
        UserService.getLives(data).then(function (response) {
            if (response.code == 0) {
              if(response.data != null){
                that.liveList = response.data.rows;
                that.totalLivesPage = response.data.totalPage;
                that.totalLivesCount = response.data.totalCount;
              }
            } else {
              that.$message({type: 'error', message: response.reason});
            }

          })
      },
      getSeeLives(data){
        console.log("历史观众id"+data.userId)
        var that = this;
        UserService.getSeeLives(data).then(function (response) {
            if (response.code == 0) {
              if(response.data != null){
                that.liveHistory = response.data.rows;
                that.totalPage = response.data.totalPage;
                that.totalCount = response.data.totalCount;
              }
            } else {
              that.$message({type: 'error', message: response.reason});
            }

          })
      },
      edit(){
          let that = this;
        if (that.readOnly) {
          that.readOnly = false;
        } else {
          that.user.registerTime = Util.dateToMs(that.user.registerTime);
          UserService.update(that.user)
            .then(function (response) {
              if (response.code == 0) {
                that.readOnly = true;
                that.user.registerTime = Util.msToDate(that.user.registerTime);
                that.$message({type: 'success', message: "操作成功"});
              } else {
                that.$message({type: 'error', message: response.reason});
              }

            })
        }
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var user = {
              id: this.user.id,
              password: this.reset.pass
            };
            let that = this;
            UserService.resetPassword(user).then(function (response) {
                if (response.code == 0) {
                  that.$message({type: 'success', message: "操作成功"});
                  that.resetPassword = false;
                } else {
                  that.$message({type: 'error', message: response.reason});
                }
            })
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.$message({type: 'info', message: '已取消操作'});
        this.resetPassword = false;
      }
    }
  }
</script>
