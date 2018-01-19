<template>
  <div class="container-wrap">
    <div class="container-box">
      <div class="container-content">
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple-dark">
              <el-tag style="font-size: large" color="#20A0FF">设备详情</el-tag>
            </div>
          </el-col>
        </el-row>
        <div style="margin: 20px"></div>
        <el-row>
          <el-col :span="12">
            <div style="margin: 20px;"></div>
            <div class="grid-content">
              <el-form :inline="true" :model="devices" class="demo-form-inline" label-width="100px">
                <el-form-item label="设备ID">
                  <el-input v-model="devices.id" placeholder="设备ID" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="设备标识">
                  <el-input v-model="devices.deviceId" placeholder="设备标识" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="是否激活">
                  <el-input  placeholder="是否激活" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="绑定用户时间">
                  <el-input  v-model="devices.bindUserTime" placeholder="绑定用户时间" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="设备型号">
                  <el-input v-model="devices.deviceModule"  placeholder="设备型号" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                  <el-input v-model="devices.deviceMemo"  placeholder="备注" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </el-col>

          <el-col :span="12">
            <div style="margin: 20px;"></div>
            <div class="grid-content">
              <el-form :inline="true" :model="devices" class="demo-form-inline" label-width="100px">
                <el-form-item label="设备名称">
                  <el-input v-model="devices.deviceName" placeholder="设备名称" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="绑定用户" v-if="user.id!=''">
                  <router-link :to="{ name: 'user', params:  { id: user.id,readOnly:true}}" style="color: #20A0FF">{{user.username}}
                  <!--<el-input v-model="user.username" placeholder="设备是否绑定"style="width: 200px"></el-input>-->

                   </router-link>

                </el-form-item>
                <el-form-item label="绑定用户" v-else>
                  <el-input  v-model="devices.userName" :disabled="true" style="width: 200px" > </el-input>
                </el-form-item>
                <br>
                <el-form-item label="激活时间">
                  <el-input placeholder="激活时间" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="解绑时间">
                  <el-input v-model="devices.relieveBindTime" placeholder="解绑时间" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
                <el-form-item label="设备状态">
                  <el-input placeholder="设备状态" :disabled="true" style="width: 200px"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </el-col>
        </el-row>


      </div>
    </div>
  </div>
</template>
<script>
  import DeviceService from '../../service/device';
  import UserService from '../../service/user';
  import Util from '../../assets/lib/util';
  export default {
    data() {
      return {
        readOnly: true,
        devices: {},
        user:{
            id:''
        }
      }
    },
    created () {
      this.devices.id = this.$route.params.id;
      this.readOnly = this.$route.params.readOnly;
      this.getDevices(this.devices.id);
    },

    methods: {
      getDevices(id){
          let that = this;
          DeviceService.get(id).then(function (response) {
            if(response.code == 0) {
              that.devices = response.data;
              that.devices.bindUserTime=Util.msToDate(response.data.bindUserTime);
              that.devices.relieveBindTime= Util.msToDate(response.data.relieveBindTime);
              if(that.devices.user!=null) {
                that.getUser(that.devices.user.id);
              }
            } else {
              that.$message({type: 'error', message: response.reason});
            }
          })
      },
      getUser(id){
          let that = this;
        UserService.get(id).then(function (response) {
          if(response.code == 0) {
            that.user = response.data;
          } else {
            that.$message({type: 'error', message: response.reason});
          }
        })
      }
    }
  }
</script>
