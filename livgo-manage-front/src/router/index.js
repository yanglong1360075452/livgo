import Vue from 'vue';
import Router from 'vue-router';
import Index from '../views/index/index.vue';
import Login from '../views/login/login.vue';
import Users from '../views/app_user/users.vue';
import Living from '../views/live/living.vue';
import HistoryLiving from '../views/live/historyLiving.vue';
import liveDetail from '../views/live/liveDetail.vue';
import User from '../views/app_user/user.vue';
import Util from '../assets/lib/util';
import Access from '../views/log/access.vue';
import Device from '../views/app-device/device.vue';
import Devices from '../views/app-device/devices.vue';
import Note from '../views/log/note.vue';
import System from '../views/log/system.vue';

Vue.use(Router);

let router = new Router({
  routes: [
    /**首页*/
    {
      path: '/',
      name: 'index',
      component: Index,
      children: [
        /**用户列表页*/
        {
          path: 'users',
          component: Users,
          name: 'users',
        },
        /** 用户详情页*/
        {
          path: 'users/:id',
          name: 'user',
          component: User
        },
        /**设备列表*/
        {
          path: 'device',
          name: 'device',
          component: Device
        },
        /** 设备详情页*/
        {
          path: 'devices/:id',
          name: 'devices',
          component: Devices
        },
        /**当前直播列表页*/
        {
          path: 'living',
          name: 'living',
          component: Living
        },
        /**历史直播列表页*/
        {
          path: 'historyLiving',
          name: 'historyLiving',
          component: HistoryLiving
        },
        /**直播详情页*/
        {
          path: 'liveDetail/:liveId',
          name: 'liveDetail',
          component: liveDetail
        },
        /** 访问日志页*/
        {
          path: 'access',
          name: 'access',
          component: Access
        },
        /** 短信日志页*/
        {
          path: 'note',
          name: 'note',
          component: Note
        },
        /** 系统消息日志页*/
        {
          path: 'system',
          name: 'system',
          component: System
        }
      ]
    },
    /**登录页*/
    {
      path: '/login',
      name: 'login',
      component: Login
    }

  ]
});

/**
 * 判断用户是否有登录，没有登录则跳转到登录页面
 * 如果用户已经登录，则回跳到登录的页的时候，要转到首页
 * */
router.beforeEach((to, from, next) => {
  var user = Util.dataToSessionStorageOperate.achieve('user');
  if (!user && to.path != '/login') {
    next('/login');
  } else {
    next();
  }
});
export default router
