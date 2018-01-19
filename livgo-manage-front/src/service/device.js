import axios from 'axios';

export default{
  getList(data){
    return axios.get('/devices/getDeviceList',{params:data});
  },
  get(data){
    return axios.get('/devices/'+data);
  }
}
