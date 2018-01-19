import axios from 'axios';

export default{
  getList(data){
    return axios.get('/users',{params:data});
  },
  get(data){
    return axios.get('/users/'+data);
  },
  update(data){
    return axios.post("/users",data);
  },
  getFriends(data){
    return axios.get("/users/friends",{params:data});
  },
  getLives(data){
    return axios.get("/lives/liveList",{params:data});
  },
  getSeeLives(data){
    return axios.get("/lives/seeLiveList",{params:data});
  },
  resetPassword(data){
    return axios.post("/users/reset",data);
  }
}
