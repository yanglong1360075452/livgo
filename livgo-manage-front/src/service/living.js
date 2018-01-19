import axios from 'axios';

export default{
  getList(data){
    return axios.get('/lives',{params:data});
  },
  get(liveId){
    return axios.get('/lives/'+liveId);
  },
  getAudience(data){
    return axios.get('/lives/liveAudience',{params:data});
  },
  getHistoryAudience(data){
    return axios.get('/lives/historyAudience',{params:data});
  }
}
