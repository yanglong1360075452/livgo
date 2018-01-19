<template>
    <!--登录页-->
    <div class="login-wrap">
        <div class="login-content">
            <h2 class="login-title">金雀LivGo智能视频耳机云平台</h2>
            <h1 class="login-title">后台管理系统</h1>
            <div class="input-wrap">
                <el-form :model="loginForm" :rules="loginRules" ref="loginForm">
                    <el-form-item class="input-box" prop="username">
                        <el-input type="text" placeholder="请输入帐号" v-model="loginForm.username" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" placeholder="请输入密码" v-model="loginForm.password" auto-complete="off"
                                  @keyup.enter.native="submitForm"></el-input>
                    </el-form-item>
                    <el-form-item class="input-button">
                        <el-button type="primary" @click="submitForm()">登录</el-button>
                    </el-form-item>
                      <span>{{version}}</span>
                </el-form>
            </div>
        </div>
  </div>
  <!--/登录页-->
</template>
<script>
    import Util from '../../assets/lib/util';
    import LoginService from '../../service/login';
    import ElInput from "../../../node_modules/element-ui/packages/input/src/input";
    export default {
      components: {ElInput},
      name: 'login',
        data() {
            var validateUserName = (rule, value, callback) => {
                if (value === '')
                    return callback(new Error('请输入帐号'));
                callback();
            };
            var validateUserPassword = (rule, value, callback) => {
                if (value === '')
                    return callback(new Error('请输入密码'));
                callback();
            };
            return {
              version: this.Version.version,
                loginForm: {
                    username: '',
                    password: ''
                },
                loginRules: {
                    username: [
                        { validator: validateUserName, trigger: 'blur' }
                    ],
                    password: [
                        { validator: validateUserPassword, trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            submitForm() {
                this.$refs["loginForm"].validate((valid) => {
                    if (valid) {
                        const login = this;
                      LoginService.login(this.loginForm)
                          .then(function (response) {
                            if (response.code==0){
                              Util.dataToSessionStorageOperate.save('user',login.loginForm);
                              login.$router.push("users");
                            } else {
                              login.$message({
                                showClose: true,
                                message: '帐号或密码错误!',
                                type: 'error'
                              });
                            }
                          })
                          .catch(function (error) {
                          })
            }
            })
        }
    }
    }
</script>
<style lang="scss">
    @import "../../assets/scss/define";
    #app,html,body{
        @extend %oh;
        @extend %h100;
    }
    .login-wrap{
        @extend %db;
        @extend %h100;
        background-color: #1F2D3D;
    }
    .login-content{
        @extend %pa;
        @extend %l50;
        margin-top: 200px;
        margin-left: -210px;
        width: 420px;
    }
    .login-title{
        @extend %cfff;
        @extend %f16;
        @extend %tac;
        line-height: 40px;
    }
    .input-wrap{
        padding: 20px 36px;
        border-radius: 10px;
        background-color: #F9FAFC;
    }
    .input-box,.input-button{
        margin: 16px 0;
    }
    .input-button{
        button{
            @extend %w100;
            @extend %db;
        }
    }
</style>
