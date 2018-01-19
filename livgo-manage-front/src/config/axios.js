import axios from 'axios'
import router from '../router/index'
import config from './index'
import store from '../store/index'

// axios 配置
axios.defaults.timeout = 10000;
axios.defaults.withCredentials = true;

var base_url;
if(window.location.href.indexOf('localhost') != -1){
  base_url = 'http://localhost:8090/livgo-server/api/manage';
}else{
  base_url = 'api/manage';
}

axios.defaults.baseURL =  base_url;


// http request 拦截器
axios.interceptors.request.use(
  config => {
    store.commit('startLoading');
    config.headers["Accept"] = "application/json";
    if (!config.url.endsWith('/login')) {
      config.headers["Content-Type"] = 'application/json';
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });

// http response 拦截器
axios.interceptors.response.use(
  response => {
    store.commit('stopLoading');
    return response.data;
  },
  error => {
    store.commit('stopLoading');
    if(error.response == undefined){
      router.replace({
        path: 'login',
        query: {redirect: router.currentRoute.fullPath}
      })
    }else if (error.response) {
      if(error.response.status == 401){
        router.replace({
          path: 'login',
          query: {redirect: router.currentRoute.fullPath}
        })
      }
    }
    return Promise.reject(error.response.data)
  });

export default axios;
