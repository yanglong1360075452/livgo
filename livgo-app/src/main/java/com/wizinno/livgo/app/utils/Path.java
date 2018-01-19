package com.wizinno.livgo.app.utils;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.data.LogOperation;
import com.wizinno.livgo.data.LogType;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@Component
public  class Path {
    @Autowired
    UserRepository userRepository;
    @Autowired
    LogRepository logRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    LiveRepository liveRepository;

    private static Path path;
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void setLogRepository(LogRepository logRepository){this.logRepository=logRepository;}
    public void setDeviceRepository(DeviceRepository deviceRepository){this.deviceRepository=deviceRepository;}
    public void setLiveRepository(LiveRepository liveRepository){this.liveRepository=liveRepository;}
    @PostConstruct     //关键二   通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    public void init() {
        path = this;
        path.userRepository = this.userRepository;   // 初使化时将已静态化的userRepository实例化
        path.logRepository=this.logRepository;
        path.deviceRepository=this.deviceRepository;
        path.liveRepository=this.liveRepository;
    }
    public static void pathUrl(String allurl,String url, User user,HttpServletRequest request,
                               String parameter,int statue ,String responseMessage){
        Map map=new HashMap();
        if (parameter!=null) {
            String[]para= parameter.split("&");
            for(int i=0;i<para.length;i++){
                String [] pa=para[i].split("=");
                map.put(pa[0],pa[1]);
            }
        }
        if(user==null){
            if(url.equals("/api/app/login")){
                String appVersions= (String) map.get("appVersions");
                String phone=(String) map.get("phone");
                String username=(String) map.get("username");
                User user1=path.userRepository.findByUsernameOrPhone(username,phone);
                Log log = new Log();
                if(user1!=null){
                    log.setCreateBy(user1.getId());
                    log.setCreateTime(new Date());
                    log.setOperation(LogOperation.login.toCode());
                    log.setPhone(phone);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setAppVersion(appVersions);
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }
            } else if(url.equals("/api/app/user")){
                String appVersions= (String) map.get("appVersions");
                String username=(String) map.get("username");
                User user1=path.userRepository.findByUsername(username);
                Log log = new Log();
                if(user1!=null){
                    log.setCreateBy(user1.getId());
                    log.setCreateTime(new Date());
                    log.setOperation(LogOperation.register.toCode());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setAppVersion(appVersions);
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }
            }else if(url.equals("/api/app/getLivePlayUrl")){
                Log log = new Log();
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.getPlayUrl.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }else if(url.equals("/api/app/getLivePushUrl")){
                String deviceNumber= (String) map.get("deviceNumber");
                Log log = new Log();
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.getPushUrl.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setPhone(deviceNumber);
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }else if(url.equals("/api/app/notifyDevState")){
                String deviceNumber= (String) map.get("deviceNumber");
                Log log = new Log();
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.livgoStatue.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setPhone(deviceNumber);
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }else if(url.equals("/api/app/user/forgetPassword")){
                Log log = new Log();
                String phone=(String) map.get("phone");
                User user1= path.userRepository.findByPhone(phone);
                if(user1!=null){
                    log.setCreateBy(user1.getId());
                }
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.forgetPassword.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }else if(url.equals("/api/app/sendMessage")) {
                Log log = new Log();
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.sendMessage.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }else if(url.equals("/api/app/Thumbnail")) {
                Log log = new Log();
                log.setCreateTime(new Date());
                log.setOperation(LogOperation.getPicture.toCode());
                log.setUserIp(Util.getIpAddr(request));
                log.setType(LogType.accessLog.toCode());
                log.setResponseStatue(statue);
                log.setRequestMessage(allurl);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
                else if(!url.equals("/api/app/login")&&
                    !url.equals("/api/app/user")&&
                    !url.equals("/api/app/getLivePlayUrl")&&
                    !url.equals("/api/app/getLivePushUrl") &&
                    !url.equals("/api/app/notifyDevState")&&
                    !url.equals("/api/app/sendMessage")&&
                    !url.equals("/api/app/Thumbnail")){
                Log log=new Log();
                log.setCreateTime(new Date());
                log.setAppVersion(allurl);
                log.setType(LogType.accessLog.toCode());
                log.setRequestMessage(allurl);
                log.setUserIp(Util.getIpAddr(request));
                log.setResponseStatue(statue);
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }

        }else{

            if(url.equals("/api/app/logout")){
                String appVersions= (String) map.get("appVersions");
                Log log = new Log();
                if(user!=null){
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.logOut.toCode());
                    log.setCreateTime(new Date());
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/userQuery")){
                Log log = new Log();
                if(user!=null){
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.queryUser.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/logoff")){
                String appVersions= (String) map.get("appVersions");
                if(user!=null){
                    List<Device> devices= user.getDevices();
                    if(devices.size()>0){
                        for(Device d:devices){
                            Log log = new Log();
                            if(user==null){
                                log.setCreateBy(null);
                            }else{
                                log.setCreateBy(user.getId());
                            }
                            log.setOperation(LogOperation.logOff.toCode());
                            log.setCreateTime(new Date());
                            log.setDeviceId(d.getDeviceId());
                            log.setAppVersion(appVersions);
                            log.setUserIp(Util.getIpAddr(request));
                            log.setRequestMessage(allurl);
                            log.setResponseStatue(statue);
                            log.setType(LogType.accessLog.toCode());
                            log.setResponseMessage(responseMessage);
                            path.logRepository.save(log);
                        }

                    }else{
                        Log log = new Log();
                        log.setCreateBy(null);
                        log.setOperation(LogOperation.logOff.toCode());
                        log.setCreateTime(new Date());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setRequestMessage(allurl);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }
                }


            }if(url.equals("/api/app/user/binding")){
                String deviceId= (String) map.get("deviceId");
                String appVersions= (String) map.get("appVersions");
                Log log = new Log();
                if(user!=null){
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.bindDevice.toCode());
                    log.setCreateTime(new Date());
                    log.setDeviceId(deviceId);
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }
            if(url.equals("/api/app/user/removeBinding")){
                String appVersions= (String) map.get("appVersions");
                List<Device>decies=new ArrayList<>();
                if(user!=null){
                   String[] responsev= responseMessage.split(",");
                   String []responsevo= responsev[1].split(":");
                   String[] deviceid=responsevo[1].split("}");
                   String deciceId=deviceid[0];
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.unbindDevice.toCode());
                    log.setCreateTime(new Date());
                    log.setAppVersion(appVersions);
                    log.setDeviceId(deciceId);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }
            if(url.equals("/api/app/createLiveRoom")){
                if(user!=null){
                   String in= (String) map.get("invite");
                    long[] friendIds = new long[0];
                    String appVersions= (String) map.get("appVersions");
                    Query query=new Query();
                    query.addCriteria(Criteria.where("user.id").is(user.getId()));
                    query.addCriteria(Criteria.where("endTime").is(null));
                    Live live=path.liveRepository.findByQuery(query);
                    if(in!=null){
                       String[] inv= in.split(",");
                        friendIds=new long[inv.length];
                        for(int i=0;i<inv.length;i++){
                           friendIds[i]=Long.parseLong(inv[i]);
                        }
                        for(long id:friendIds){
                            Log log=new Log();
                            log.setCreateBy(user.getId());
                            log.setOperation(LogOperation.startLive.toCode());
                            log.setCreateTime(new Date());
                            log.setAppVersion(appVersions);
                            log.setRequestMessage(allurl);
                            log.setResponseStatue(statue);
                            log.setFriendId(id);
                            log.setUserIp(Util.getIpAddr(request));
                            log.setType(LogType.accessLog.toCode());
                            log.setLiveId(live.getId());
                            log.setResponseMessage(responseMessage);
                            path.logRepository.save(log);
                        }

                    }else{
                        Log log=new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.startLive.toCode());
                        log.setCreateTime(new Date());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setRequestMessage(allurl);
                        log.setResponseStatue(statue);
                        log.setLiveId(live.getId());
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }


                }
            }if(url.equals("/api/app/closeLiveRoom")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String livId=(String)map.get("liveId");
                    Long liveId=Long.parseLong(livId);
                    Log log=new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.endLive.toCode());
                    log.setCreateTime(new Date());
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setLiveId(liveId);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/inviteFriends")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String livId=(String)map.get("liveId");
                    long liveId=Long.parseLong(livId);
                    String in= (String) map.get("invite");
                        String []inv=in.split(",");
                        long[] invite=new long[inv.length];
                        for(int i=0;i<inv.length;i++){
                            invite[i]=Long.parseLong(inv[i]);
                    }
                    for(long id:invite){
                        Log log=new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.inviteLive.toCode());
                        log.setCreateTime(new Date());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setFriendId(id);
                        log.setLiveId(liveId);
                        log.setRequestMessage(allurl);
                        log.setResponseStatue(statue);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }
                }


            }if(url.equals("/api/app/liveFriends")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String livId=(String)map.get("liveId");
                    long liveId=Long.parseLong(livId);
                    Log log=new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.queyLiveFriends.toCode());
                    log.setCreateTime(new Date());
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setLiveId(liveId);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/outlive")){
                if(user!=null){
                    String useId=(String)map.get("userId");
                    Long userId = Long.parseLong(useId);
                    String appVersions= (String) map.get("appVersions");
                    String livId=(String)map.get("liveId");
                    long liveId=Long.parseLong(livId);

                    if(userId==null){
                        Log log=new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.outLive.toCode());
                        log.setCreateTime(new Date());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseStatue(statue);
                        log.setRequestMessage(allurl);
                        log.setLiveId(liveId);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }else{
                        Log log=new Log();
                        log.setCreateBy(userId);
                        log.setOperation(LogOperation.kickLive.toCode());
                        log.setCreateTime(new Date());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseStatue(statue);
                        log.setRequestMessage(allurl);
                        log.setLiveId(liveId);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }

                }

            }if(url.equals("/api/app/spectator")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String livId=(String)map.get("liveId");
                    Long liveId=Long.parseLong(livId);
                    Log log=new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.enterLive.toCode());
                    log.setCreateTime(new Date());
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setLiveId(liveId);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/liveQuery")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.queryLive.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/notifyLiveState")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setCreateTime(new Date());
                    log.setOperation(LogOperation.phoneStatue.toCode());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/addFriend")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String phones= (String) map.get("phoneNumbers");
                    String[] phoneNumbers=phones.split(",");
                    for(String phone:phoneNumbers){
                        User phoneUser= path.userRepository.findByPhone(phone);
                        if(phoneUser==null){
                            throw new CustomException(CustomExceptionCode.UserNotExists);
                        }
                        Log log = new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.addFriend.toCode());
                        log.setCreateTime(new Date());
                        log.setFriendId(phoneUser.getId());
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setRequestMessage(allurl);
                        log.setResponseStatue(statue);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }
                }


            }if(url.equals("/api/app/user/friendRegister")){
                if(user!=null){
                    String appVersions= (String) map.get("appVersions");
                    String phoneNumber= (String) map.get("phoneNumber");
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.inviteRegister.toCode());
                    log.setCreateTime(new Date());
                    log.setPhone(phoneNumber);
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    log.setRequestMessage(allurl);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/friendAgree")){
                if(user!=null){
                    String frienId=(String)map.get("friendId");
                    Long friendId=Long.parseLong(frienId);
                    String appVersions= (String) map.get("appVersions");
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.acceptAddFriend.toCode());
                    log.setCreateTime(new Date());
                    log.setFriendId(friendId);
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/friendReject")){
                if(user!=null){
                    long friendId= (long) map.get("friendId");
                    String appVersions= (String) map.get("appVersions");
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.rejectAddFriend.toCode());
                    log.setCreateTime(new Date());
                    log.setFriendId(friendId);
                    log.setAppVersion(appVersions);
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/friends")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.queryFriends.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/dropUser")){
                if(user!=null){
//                    List<Long>friendIds= (List<Long>) map.get("friendId");
                    String friIds=(String)map.get("friendId");
                   String[] frieIds= friIds.split(",");
                    Long[]friendIds=new Long[frieIds.length];
                    for(int i=0;i<frieIds.length;i++){
                        friendIds[i]=Long.parseLong(frieIds[i]);
                    }
                    String appVersions= (String) map.get("appVersions");
                    for(Long id:friendIds){
                        Log log = new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.deleteFriends.toCode());
                        log.setCreateTime(new Date());
                        log.setFriendId(id);
                        log.setAppVersion(appVersions);
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseStatue(statue);
                        log.setRequestMessage(allurl);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }

                }

            }if(url.equals("/api/app/user/queryInvite")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.queryInvite.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/editor")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.editorImg.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/editorBgImg")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.editorBgImg.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setRequestMessage(allurl);
                    log.setResponseStatue(statue);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/setPassword")){
                if(user!=null){
                    Log log = new Log();
                    log.setCreateBy(user.getId());
                    log.setOperation(LogOperation.setPassword.toCode());
                    log.setCreateTime(new Date());
                    log.setUserIp(Util.getIpAddr(request));
                    log.setType(LogType.accessLog.toCode());
                    log.setResponseStatue(statue);
                    log.setRequestMessage(allurl);
                    log.setResponseMessage(responseMessage);
                    path.logRepository.save(log);
                }

            }if(url.equals("/api/app/user/editorText")){
                if(user!=null){
                    String nickname= (String) map.get("nickname");
                    Integer sex= (Integer) map.get("sex");
                    if(nickname!=null){
                        Log log = new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.editorNickname.toCode());
                        log.setCreateTime(new Date());
                        log.setUserIp(Util.getIpAddr(request));
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseStatue(statue);
                        log.setRequestMessage(allurl);
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }if(sex!=null){
                        Log log = new Log();
                        log.setCreateBy(user.getId());
                        log.setOperation(LogOperation.editorSex.toCode());
                        log.setCreateTime(new Date());
                        log.setUserIp(Util.getIpAddr(request));
                        log.setRequestMessage(allurl);
                        log.setResponseStatue(statue);
                        log.setType(LogType.accessLog.toCode());
                        log.setResponseMessage(responseMessage);
                        path.logRepository.save(log);
                    }

                }

            }if(url.equals("/api/app/inviteLive")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.whoInviteMe.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }if(url.equals("/api/app/alterPhone")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.alterPhone.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/message/audienceMessage")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.audMsg.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/message/liveMsg")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.liveMsg.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/user/isPhoneRegister")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.phoneIsResgister.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/myHistoryLive")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.myHistoryLives.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/sendMessage")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.sendMessage.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/liveDetail")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.liveDetail.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/queryHistoryLive")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.friendHistoryLive.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/historyMessage")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.historyLiveMsg.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/recoverLive")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.recoverLive.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }
            if(url.equals("/api/app/exceptionCloseLive")){
                Log log = new Log();
                log.setCreateBy(user.getId());
                log.setOperation(LogOperation.exceptionCloseLive.toCode());
                log.setCreateTime(new Date());
                log.setUserIp(Util.getIpAddr(request));
                log.setRequestMessage(allurl);
                log.setResponseStatue(statue);
                log.setType(LogType.accessLog.toCode());
                log.setResponseMessage(responseMessage);
                path.logRepository.save(log);
            }


        }

    }


}
