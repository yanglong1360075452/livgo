import axios from 'axios';
import qs from 'qs';

export default{
  login(data){
    return axios.post('/login',qs.stringify(data));
  },
  logout(){
    return axios.post('/logout');
  }
}
