import axios from 'axios';

export default{
  getAccessLogs(data){
    return axios.get('/logs/access',{params:data});
  }
}
