import Vue from 'vue';
import App from './App';
import router from './router';
import axios from './config/axios';
import store from './store/index.js';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-default/index.css';
import VueQuillEditor from 'vue-quill-editor';
import Global from './const/Global.vue';
import Version from './const/Version.vue';

Vue.use(VueQuillEditor);
Vue.prototype.GLOBAL = Global;
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.prototype.Version =Version;
// 将axios挂载到prototype上，在组件中可以直接使用this.axios访问
Vue.prototype.axios = axios;

Vue.filter('date',function (val){
  if(val > 0){
    var date = new Date(val);
    var Y = date.getFullYear(),
      m = date.getMonth() + 1,
      d = date.getDate(),
      H = date.getHours(),
      i = date.getMinutes(),
      s = date.getSeconds();
    if (m < 10) {
      m = '0' + m;
    }
    if (d < 10) {
      d = '0' + d;
    }
    if (H < 10) {
      H = '0' + H;
    }
    if (i < 10) {
      i = '0' + i;
    }
    if (s < 10) {
      s = '0' + s;
    }
    var t = Y + '-' + m + '-' + d + '  ' + H + ':'+i+':'+s;
    return t;
  }else {
    return;
  }
});

new Vue({
  el: '#app',
  router,
  store,
  axios,
  template: '<App/>',
  components: {App}
});
