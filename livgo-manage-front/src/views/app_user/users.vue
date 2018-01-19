<template>
  <div class="container-wrap">
    <div class="container-box">
      <div class="search-box" style="width:1300px">
        <el-input placeholder="请输入用户名"  v-model="username"  style="width: 400px">
          </el-input>
           <el-input placeholder="请输入昵称"  v-model="nickname"  style="width: 400px">
                    </el-input>
                     <el-input placeholder="请输入电话号"  v-model="phone"  style="width: 400px">
                                        </el-input>
                                        <el-button @click="handleIconClick" type="primary">查询</el-button>
      </div>
      <div class="container-content">
        <el-table :data="users" border stripe tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="id" label="用户ID" width="150" align="center"></el-table-column>
          <el-table-column prop="username" label="用户名" width="200" align="center">
            <template scope="scope">
              <router-link :to="{ name: 'user', params:  { id: scope.row.id,readOnly:true}}" style="color: #20A0FF">
                {{scope.row.username}}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="nickname" label="昵称" width="200" show-overflow-tooltip
                           align="center"></el-table-column>
          <el-table-column prop="sexDesc" label="性别" width="180" align="center"></el-table-column>
          <el-table-column prop="phone" label="手机号码" width="250" align="center"></el-table-column>
          <el-table-column prop="registerTime" label="注册时间" width="180" show-overflow-tooltip align="center">
            <template scope="scope">
              {{scope.row.registerTime | date}}
            </template>
          </el-table-column>
          <el-table-column prop="statusDesc" label="状态" width="180" align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template scope="scope">
              <el-button @click="updateUserStatus(scope.row)" type="info" size="small">
                {{scope.row.status == 1 ? '禁用' : '激活'}}
              </el-button>

              <router-link :to="{ name: 'user', params:  { id: scope.row.id,readOnly:false}}" style="color: white">
                <el-button type="info" size="small">编辑</el-button>
              </router-link>

              <!--<el-button @click="deleteUser(scope.row)" type="danger" size="small">删除</el-button>-->
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
  import UserService from '../../service/user';
  import types from '../../store/mutation-types';
  export default {
    name: 'list',
    data() {
      return {
        currentPage: 1,
        totalPage: 0,
        pageSize: 18,
        totalCount: 0,
        key_word: '',
        users: [],
        username:null,
        nickname:null,
        phone:null

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
        if (!this.key_word)
          return this.users;
        else {
          this.users.forEach((item) => {
            if (item.username.indexOf(this.key_word) > -1)
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
      /**更新用户状态*/
      updateUserStatus (user) {
        var title = '';
        if (user.status == 1) {
          title = "禁用";
        } else {
          title = "激活";
        }
        this.$confirm('是否' + title + user.username + '?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (user.status == 1) {
            user.status = 2;
          } else {
            user.status = 1;
          }
          let that = this;
          UserService.update(user).then(function (response) {
              if (response.code == 0) {
                that.$message({type: 'success', message: "操作成功"});
                that.fetchData();
              } else {
                that.$message({type: 'fail', message: response.reason});
              }
          });
        }).catch(() => {
          this.$message({type: 'info', message: '已取消操作'});
        });
      },
      /**获取用户列表数据*/
      fetchData (route) {
        var tab = route ? route.query.tab : this.$route.query.tab;
        let that = this;
        setTimeout(() => {
          UserService.getList({
            tab: tab,
            page: that.currentPage - 1,
            length: that.pageSize,
            username:that.username,
            nickname:that.nickname,
            phone:that.phone
          }).then(function (response) {
            if (response.code == 0) {
              that.users = response.data.rows;
              that.totalPage = response.totalPage;
              that.totalCount = response.data.totalCount;
            }
            else that.$message({type: 'error', message: response.reason});
          });
        }, 300);
      }
    }
  }
</script>
<style lang="scss">
  @import "../../assets/scss/define";

  .container-content {
    @extend %pa;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 70px 20px;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .search-box {
    @extend %pa;
    top: 20px;
    right: 40px;
    z-index: 1;
    width: 360px;
  }


  .container-box {
    @extend %h100;
  }

  .page-wrap {
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
