<template>
    <div class="menu-wrap">
      <el-menu :default-active="tabIndex" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-document"></i>数据查询</template>
          <el-menu-item-group>
            <el-menu-item index="1-1"><a href="#/users">APP用户</a></el-menu-item>
            <el-menu-item index="1-2"><a href="#/device">设备信息</a></el-menu-item>
            <el-submenu index="1-3">
              <template slot="title"></i>直播信息</template>
              <el-menu-item-group>
                <el-menu-item index="1-3-1"><a href="#/living">当前直播</a></el-menu-item>
                <el-menu-item index="1-3-2"><a href="#/historyLiving">直播历史</a></el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-message"></i>日志管理</template>
          <el-menu-item-group>
            <el-menu-item index="2-1"><a href="#/access">访问日志</a></el-menu-item>
            <el-menu-item index="2-2"><a href="#/note">短信日志</a></el-menu-item>
            <el-menu-item index="2-3"><a href="#/system">系统消息日志</a></el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </div>
</template>
<script>
    export default {
        name: 'menu',
        computed: {
            tabIndex(){
                return this.$store.state.tab_index;
            }
        },
        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
</script>
<style lang="scss">
    @import "../assets/scss/define";
    .menu-wrap{
        @extend %pa;
        top: 60px;
        left: 0;
        width: 200px;
        bottom: 0;
        background-color: #EFF2F7;
    }
    .el-menu-item{
        @extend %pr;
        a{
            @extend %pa;
            @extend %db;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            padding: 0 45px;
            color: inherit;
        }
    }
</style>
