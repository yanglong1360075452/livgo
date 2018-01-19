package com.wizinno.livgo.app.controller;
import com.livgo.app.mq.util.PushMessage;
import com.wizinno.livgo.app.document.AddRequest;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.AddRequestReponsitory;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.MessageService;
import com.wizinno.livgo.app.utils.Upload;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.*;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * Created by LiuMei on 2017-05-03.
 */
@RestController
@RequestMapping("/api/app/user")
public class UserController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AddRequestReponsitory addRequestReponsitory;

    @RequestMapping(value = "/captcha/{phone}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO sendCode(@PathVariable(value = "phone") String phone, @RequestParam("appVersions") String appVersions) {
        if (!Util.phoneNumber(phone)) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        try {
            String str = MessageService.sendMsg(phone);
            if ("success".equals(str)) {
                log.info("发送验证码给" + phone + "成功");
            } else {
                log.info("发送验证码给" + phone + "成功");
            }

        } catch (IOException e) {
            throw new CustomException(CustomExceptionCode.CaptchaSendFail);
        }
        return new ResponseVO();
    }
    /**
     * 忘记密码
     *
     * @param captcha
     * @param
     * @return
     */
    @RequestMapping(value="/forgetPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO forgetPassword(@RequestParam(value = "captcha") String captcha,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "phone") String phone){
        if(Util.stringNull(captcha)&&Util.stringNull(password)&&Util.stringNull(phone)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }

        try {
            String str = MessageService.checkMsg(phone,captcha);
            if("success".equals(str)){
                    log.info(phone+"验证码验证成功");
                }else{
                    log.info(phone+"验证码验证失败");
                    throw new CustomException(CustomExceptionCode.CaptchaError);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Query query=new Query();
        Update update=new Update();
        query.addCriteria(Criteria.where("phone").is(phone));
        update.set("password",bCryptPasswordEncoder.encode(password));
        userRepository.update(query,update);
        return  new ResponseVO();

    }

    /**
     * 注册用户
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO createUser(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "captcha") String captcha,
                                 @RequestParam("appVersions") String appVersions) {

        if (Util.stringNull(username)) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }else{
            if(userRepository.findByUsername(username)!=null){
                throw new CustomException(CustomExceptionCode.UserExists);
            }

        }

        if (!Util.stringNull(phone)) {
            if(userRepository.findByPhone(phone)!=null){
                throw new CustomException(CustomExceptionCode.phoneExit);
            }
            if (Util.stringNull(captcha)) {
                throw new CustomException(CustomExceptionCode.ErrorParam);
            }
            try {
                    String str = MessageService.checkMsg(phone,captcha);
                    if("success".equals(str)){
                        log.info(phone+"验证码验证成功");
                    }else{
                        log.info(phone+"验证码验证失败");
                        throw new CustomException(CustomExceptionCode.CaptchaError);
                    }

            } catch (IOException e) {
                throw new CustomException(CustomExceptionCode.CaptchaError);
            }
        }
        User user;
        if (!Util.stringNull(username)) {
            user = userRepository.findByUsername(username);
            if (user != null) {
                throw new CustomException(CustomExceptionCode.UserExists);
            }
        }
        if (!Util.stringNull(phone)) {
            user = userRepository.findByPhone(phone);
            if (user != null) {
                throw new CustomException(CustomExceptionCode.UserExists);
            }
        }
        user = new User();
        user.setUsername(username);
        String nickname="";
        user.setNickname(nickname);
        user.setImg("");
        user.setSex(2);
        user.setActive(true);
        user.setBgImg("");
        user.setQrCode("");
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(phone);
        user.setStatus(UserStatus.active.toCode());
        user.setRegisterTime(new Date());
        userRepository.save(user);
        return new ResponseVO();
    }

    @RequestMapping(value = "/logoff",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO logoffUser(@RequestParam(value = "appVersions") String appVersions){
        if (appVersions == null){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Long id = (Long) request.getAttribute("userId");
        User user = userRepository.findById(id);
        List<Long> friends = user.getFriends();
        if (friends != null){
            if ( friends.size()> 0  ){
                for (Long friendId:friends){
                    User friend = userRepository.findById(friendId);
                    List<Long> friends1 = friend.getFriends();
                    List<Long> lists = new ArrayList<>();
                    for(Long friendsId:friends1){
                        if (!friendsId.equals(user.getId())){
                            lists.add(friendsId);
                        }
                    }
                    Query query = new Query();
                    query.addCriteria( Criteria.where("id").is(friendId));
                    Update update = new Update();
                    update.set("friends",lists);
                    userRepository.update(query,update);
                }
            }
        }

        List<Device> devices = user.getDevices();
        if (devices != null ){
            if ( devices.size() > 0  ){
                for (Device d:devices){
                    Query query = new Query();
                    query.addCriteria( Criteria.where("id").is(d.getId()));
                    Update update = Update.update("bind", false).set("user", null);
                    deviceRepository.updataDevice(query,update);
                }
            }
        }
        Query query = new Query();
        query.addCriteria( Criteria.where("id").is(user.getId()));
        Update update = Update.update("friends", null).set("devices", null).set("status",UserStatus.logOff.toCode());
        userRepository.update(query,update);
        return new ResponseVO();

    }



    /**
     *查询用户
     * @param
     * @return
     */
    @RequestMapping(value = "/userQuery",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO queryUser() throws IOException {

        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findById(userid);
        response.setCharacterEncoding("UTF-8");
        if(user==null){
            throw new CustomException(CustomExceptionCode.UserNotExists);
        }
        return new ResponseVO(user);



    }
    /**
     *编辑我的头像
     * @param
     * @return
     */
    @RequestMapping(value = "/editor",method = RequestMethod.POST)
    public ResponseVO editorImg(@RequestParam("img") MultipartFile img ) throws IOException {
        if(img.isEmpty()){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        String fileName= Upload.fileUpload(img,request);
        String path="http://118.89.114.55:8080/img/"+fileName;
        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findById(userid);
        user.setImg(path);
        Query query=new Query();
        query.addCriteria(Criteria.where("id").is(userid));
        Update update=new Update();
        update.set("img",user.getImg());
        userRepository.update(query,update);
        return  new ResponseVO(0);

    }
    /**
     *编辑背景墙
     * @param
     * @return
     */
    @RequestMapping(value = "/editorBgImg",method = RequestMethod.POST)
    public ResponseVO editorBgImg(@RequestParam("bgImg") MultipartFile bgImg) throws IOException {
        if(bgImg.isEmpty()){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
       String fileName= Upload.fileUpload(bgImg,request);
        String path="http://118.89.114.55:8080/img/"+fileName;
        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findById(userid);
        user.setBgImg(path);
        Query query=new Query();
        query.addCriteria(Criteria.where("id").is(userid));
        Update update=new Update();
        update.set("bgImg",user.getBgImg());
        userRepository.update(query,update);
        return  new ResponseVO(0);

    }
    /**
     *编辑我的昵称，性别，
     * @param  nickname
     *  @param  sex
     * @return
     */
    @RequestMapping(value = "/editorText",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO editorText(@RequestParam(value = "nickname", required = false) String nickname,
                                 @RequestParam(value = "sex", required = false) Integer sex                                                                       ){
        if((sex!=null||nickname!=null)){
            long userid= (long) request.getAttribute("userId");
            if(sex!=null){
                User user=userRepository.findById(userid);
                user.setNickname(nickname);
                user.setSex(sex);
                Query query=new Query();
                query.addCriteria(Criteria.where("id").is(userid));
                Update update=new Update();
                update.set("sex",user.getSex());
                userRepository.update(query,update);
            }if(nickname!=null){
                User user=userRepository.findById(userid);
                user.setNickname(nickname);
                user.setSex(sex);
                Query query=new Query();
                query.addCriteria(Criteria.where("id").is(userid));
                Update update=new Update();
                update.set("nickname",user.getNickname());
                userRepository.update(query,update);
            }
            return  new ResponseVO(0);

        }
        throw new CustomException(CustomExceptionCode.ErrorParam);


    }

    /**
     *查询谁邀请我为好友
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/queryInvite",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO queryInvite(){
        long userid= (long) request.getAttribute("userId");
        String acceptName=userRepository.findById(userid).getUsername();
        Query query1=new Query();
        query1.addCriteria(Criteria.where("acceptName").is(acceptName));
        query1.addCriteria(Criteria.where("statue").is(AcceptType.nodo.toCode()));
       List<AddRequest>addRequests= addRequestReponsitory.findByQuerys(query1);
       return new ResponseVO(addRequests);
    }

    /**
     *修改密码
     * @param  password
     * @param  rePassword
     * @return
     */
    @RequestMapping(value = "/setPassword",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO setPassword(@RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "rePassword", required = true) String rePassword){
        if(Util.stringNull(password)&&Util.stringNull(rePassword)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        User user= userRepository.findById(userid);
        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new CustomException(CustomExceptionCode.defferentPassword);
        }
        user.setPassword(bCryptPasswordEncoder.encode(rePassword));
        Query query=new Query();
        query.addCriteria(Criteria.where("id").is(userid));
        Update update=new Update();
        update.set("password",user.getPassword());
        userRepository.update(query,update);
        return  new ResponseVO(0);

    }
    /**
     *添加好友
     * @param appVersions
     * @param   phoneNumbers
     * @return
     */
    @RequestMapping(value = "/addFriend",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO addFriend(@RequestParam(value = "appVersions",required = true)String appVersions,
                                @RequestParam (value = "phoneNumbers",required = true)String[] phoneNumbers){
        if(phoneNumbers.length==0||Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        User user= userRepository.findById(userid);
        Map<String,String > param=new HashMap<>();
        Collection<String >usernames = new ArrayList<>();
        param.put("msg",user.getUsername()+"请求添加你为好友");
        AddRequest addRequest=null;
        for(String phoneNumber:phoneNumbers){
            User user2=userRepository.findByPhone(phoneNumber);
            if(user2==null){
                throw new CustomException(CustomExceptionCode.UserNotExists);
            }
            //将请求添加到请求表
           List<AddRequest>addRequests= addRequestReponsitory.findAll();
            if(addRequests!=null&&addRequests.size()>0){
                Query query=new Query();
                query.addCriteria(Criteria.where("requestName").is(user.getUsername()));
                query.addCriteria(Criteria.where("acceptName").is(user2.getUsername()));
                query.addCriteria(Criteria.where("statue").is(AcceptType.nodo .toCode()));
                List<AddRequest> addRequests1=addRequestReponsitory.findByQuerys(query);
                if(addRequests1.size()==0){
                    addRequest=new AddRequest();
                    addRequest.setImg(user.getImg());
                    addRequest.setReq_id(userid);
                    addRequest.setRequestName(user.getUsername());
                    addRequest.setAcc_id(user2.getId());
                    addRequest.setAcceptName(user2.getUsername());
                    addRequest.setStatue(AcceptType.nodo.toCode());
                    addRequestReponsitory.save(addRequest);
                    usernames.add(user2.getUsername());
                }
            }else{
                addRequest=new AddRequest();
                addRequest.setImg(user.getImg());
                addRequest.setReq_id(userid);
                addRequest.setRequestName(user.getUsername());
                addRequest.setAcc_id(user2.getId());
                addRequest.setAcceptName(user2.getUsername());
                addRequest.setStatue(AcceptType.nodo.toCode());
                addRequestReponsitory.save(addRequest);
                usernames.add(user2.getUsername());
            }

        }
        //推送通知
        PushMessage.sendPushNotice(param,usernames);


        return new ResponseVO(user);
    }
    /**
     *邀请好友注册
     * @param  phoneNumber
     * @param  appVersions
     * @return
     */
    @RequestMapping(value = "/friendRegister",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO friendRegister(@RequestParam (value = "phoneNumber",required = true)String phoneNumber,
                                     @RequestParam(value="appVersions",required=true)String appVersions){
        if(Util.stringNull(phoneNumber)||Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findByPhone(phoneNumber);
        if(user!=null){
            return new ResponseVO(user);
        }else{
            user=userRepository.findByUsername(phoneNumber);
            if(user!=null){
                return  new ResponseVO(user);
            }else{
                return  new ResponseVO(user);
            }
        }


    }
    /**
     *用户同意添加好友
     * @param  friendId
     * @param  appVersions
     * @return
     */
    @RequestMapping(value = "/friendAgree", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO friendAgree(@RequestParam(value = "friendId",required = true) long friendId, @RequestParam(value = "appVersions",required = true) String appVersions) {
        if (!Util.stringNull(appVersions)) {
            long userId = (long) request.getAttribute("userId");
            //用户加邀请的用户为好友
            User user = userRepository.findById(userId);
            List<Long> list = user.getFriends();
            if (list == null) {
                list = new ArrayList<>();
                list.add(friendId);
            } else {
                if(!list.contains(friendId)){
                    list.add(friendId);
                }
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(userId));
            Update update = new Update();
            update.set("friends", list);
            userRepository.update(query, update);

            //邀请的用户和用户为好友
            User userFriend = userRepository.findById(friendId);
            List<Long> lists = userFriend.getFriends();
            if (lists == null) {
                lists = new ArrayList<>();
                lists.add(userId);
            } else {
                if(!lists.contains(userId)){
                    lists.add(userId);
                }
            }
            Query querys=new Query();
            querys.addCriteria(Criteria.where("id").is(friendId));
            Update updates=new Update();
            updates.set("friends",lists);
            userRepository.update(querys,updates);

            //用户同意添加加入请求表
            Query query1=new Query();
            Update update1=new Update();
            query1.addCriteria(Criteria.where("requestName").is(userFriend.getUsername()));
            query1.addCriteria(Criteria.where("acceptName").is(user.getUsername()));
            query1.addCriteria(Criteria.where("statue").is(AcceptType.nodo.toCode()));
            update1.set("statue",AcceptType.agree.toCode());
            addRequestReponsitory.update(query1,update1);
            return new ResponseVO(0);

        }else{
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }


    }
    /**
     *用户拒绝添加好友
     * @param
     * @param  friendId
     * @param  appVersions
     * @return
     */
    @RequestMapping(value = "/friendReject",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO friendReject(@RequestParam (value = "friendId",required = true)long friendId,
                                   @RequestParam(value="appVersions",required=true)String appVersions){
        if(friendId==0&&Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        User user= userRepository.findById(userid);
        User userFriend=userRepository.findById(friendId);
        //用户拒绝添加加入请求表
        Query query1=new Query();
        Update update1=new Update();
        query1.addCriteria(Criteria.where("requestName").is(userFriend.getUsername()));
        query1.addCriteria(Criteria.where("acceptName").is(user.getUsername()));
        query1.addCriteria(Criteria.where("statue").is(AcceptType.nodo.toCode()));
        update1.set("statue",AcceptType.forbidden.toCode());
        addRequestReponsitory.update(query1,update1);
        return new ResponseVO(0);

    }
    /**
     *删除好友
     * @param
     * @param  friendIds
     * @param appVersions
     * @return
     */
    @RequestMapping(value = "/dropUser",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO droupUser(@RequestParam (value = "friendId",required = true)List<Long> friendIds,
                                @RequestParam(value="appVersions",required=true)String appVersions){
        if(friendIds.size()==0||Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        //删除用户的好友
        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findById(userid);
        List friends=user.getFriends();
        for(int i=0;i<friendIds.size();i++){
           for(int j=0;j<friends.size();j++){
               if(friendIds.get(i)==friends.get(j)){
                   friends.remove(j);
               }
           }
        }
        user.setFriends(friends);
        Query query=new Query();
        query.addCriteria(Criteria.where("id").is(userid));
        Update update=new Update();
        update.set("friends",user.getFriends());
        userRepository.update(query,update);
        //被删除用户和删除你的用户解除好友关系
           for(int n=0;n<friendIds.size();n++){
             User friendUser=userRepository.findById(friendIds.get(n));
             List<Long> listss=  friendUser.getFriends();
               for(int m=0;m<listss.size();m++){
                   if(listss.get(m)==userid){
                       listss.remove(m);
                       friendUser.setFriends(listss);
                       Query querys=new Query();
                       querys.addCriteria(Criteria.where("id").is(friendIds.get(n)));
                       Update updates=new Update();
                       updates.set("friends",friendUser.getFriends());
                       userRepository.update(querys,updates);
                       //记录日志

                   }
               }


        }

        return new ResponseVO(0);
    }

    /**
     *查询好友
     * @param
     * @return
     */
    @RequestMapping(value = "/friends",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO friends(){
        long userid= (long) request.getAttribute("userId");
        User user= userRepository.findById(userid);
        List<Long> userList=user.getFriends();
        List<User> friendUser=new ArrayList<>();
        if(userList.size()!=0 || userList != null){
            for(int i=0;i<userList.size();i++){
                User friuser=userRepository.findById(userList.get(i));
                friendUser.add(friuser);

            }
            return new ResponseVO(friendUser);
        }else{
            return new ResponseVO();
        }

    }

    /**
     *查询用户手机是否是注册
     * @param
     * @return
     */
    @RequestMapping(value = "/isPhoneRegister",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO isPhoneRegister(@RequestParam(value = "PhoneNumbers",required = true) String[] PhoneNumbers){
       if(PhoneNumbers==null){
           throw new CustomException(CustomExceptionCode.ErrorParam);
       }
        long userid= (long) request.getAttribute("userId");
        User user= userRepository.findById(userid);
       AddRequest addRequest;
        Map<String,String > param=new HashMap<>();
        Collection<String >usernames = new ArrayList<>();
        param.put("msg",user.getUsername()+"请求添加你为好友");
       List<String>phones=new ArrayList<>();
        for(String phoneNumber:PhoneNumbers ){
           User user1= userRepository.findByPhone(phoneNumber);
            if(user1==null){
            phones.add(phoneNumber);
            }else{
                //将请求添加到请求表
               User user2= userRepository.findByPhone(phoneNumber);
                List<AddRequest>addRequests= addRequestReponsitory.findAll();
                if(addRequests!=null&&addRequests.size()>0){
                    Query query=new Query();
                    query.addCriteria(Criteria.where("requestName").is(user.getUsername()));
                    query.addCriteria(Criteria.where("acceptName").is(user2.getUsername()));
                    query.addCriteria(Criteria.where("statue").is(AcceptType.nodo .toCode()));
                    List<AddRequest> addRequests1=addRequestReponsitory.findByQuerys(query);
                    if(addRequests1.size()==0){
                        addRequest=new AddRequest();
                        addRequest.setReq_id(userid);
                        addRequest.setImg(user.getImg());
                        addRequest.setRequestName(user.getUsername());
                        addRequest.setAcc_id(user2.getId());
                        addRequest.setAcceptName(user2.getUsername());
                        addRequest.setStatue(AcceptType.nodo.toCode());
                        addRequestReponsitory.save(addRequest);
                        usernames.add(user2.getUsername());
                    }
                }else{
                    addRequest=new AddRequest();
                    addRequest.setReq_id(userid);
                    addRequest.setImg(user.getImg());
                    addRequest.setRequestName(user.getUsername());
                    addRequest.setAcc_id(user2.getId());
                    addRequest.setAcceptName(user2.getUsername());
                    addRequest.setStatue(AcceptType.nodo.toCode());
                    addRequestReponsitory.save(addRequest);
                    usernames.add(user2.getUsername());
                }
                //推送通知
                PushMessage.sendPushNotice(param,usernames);
            }
            }

        return new ResponseVO(phones);
    }


    /**
     *
     *绑定设备(设备信息还不清楚，以后再完善)
     * @param deviceId
     * @param appVersions
     *  @param deviceName
     * @param deviceModule
     * @param deviceMemo
     * @return
     */
    @RequestMapping(value = "/binding",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO binding(@RequestParam(value = "deviceId",required = true) String deviceId,
                              @RequestParam(value="appVersions",required=true)String appVersions,
                              @RequestParam(value="deviceName",required=true)String deviceName,
                              @RequestParam(value="deviceModule",required=true)String deviceModule,
                              @RequestParam(value="deviceMemo",required=true)String deviceMemo){
        if(Util.stringNull(deviceId)||Util.stringNull(appVersions)||Util.stringNull(deviceName)||
                Util.stringNull(deviceModule)||Util.stringNull(deviceMemo)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        String number=deviceId.toUpperCase();
        //先查询设备是否存在
        Query query3=new Query();
        query3.addCriteria(Criteria.where("deviceId").is(number));
        Device device2 = deviceRepository.findByQuery(query3);
        User user=userRepository.findById(userid);
        if(device2==null){
            //把设备信息保存数据库
            device2=new Device();
            User user1=new User();
            device2.setDeviceId(number);
            device2.setDeviceName(deviceName);
            device2.setDeviceMemo(deviceMemo);
            device2.setDeviceModule(deviceModule);
            device2.setBind(true);
            device2.setBindTime(new Date());
            user1.setId(user.getId());
            user1.setNickname(user.getNickname());
            device2.setUser(user1);
            deviceRepository.save(device2);
            //把设备信息更新到用户
            Device dev=new Device();
            List list=new ArrayList();
            dev.setId(device2.getId());
            dev.setDeviceId(device2.getDeviceId());
            dev.setBind(true);
            dev.setDeviceName(device2.getDeviceName());
            dev.setDeviceMemo(device2.getDeviceMemo());
            dev.setDeviceModule(device2.getDeviceModule());
            dev.setBindTime(device2.getBindTime());
            list.add(dev);
            user.setDevices(list);
            Query query=new Query();
            query.addCriteria(Criteria.where("id").is(userid));
            Update update=new Update();
            update.set("devices",user.getDevices());
            userRepository.update(query,update);
        }else{
            //用户A的设备信息清除，把设备A的信息更新添加到用户B，用户B 的信息更新到设备
            if(device2.getUser()!=null) {
                //把已绑定用户信息中的设备信息清除
                User bindUser = userRepository.findById(device2.getUser().getId());
                Query query4 = new Query();
                Update update4 = new Update();
                query4.addCriteria(Criteria.where("id").is(bindUser.getId()));
                List<Device> bindDevice = new ArrayList<>();
                update4.set("devices", bindDevice);
                userRepository.update(query4, update4);
            }
                //把绑定的用户信息更新到设备信,并改变设备状态和绑定时间
                Query query = new Query();
                Update update = new Update();
                query.addCriteria(Criteria.where("deviceId").is(number));
                User user1 = new User();
                user1.setId(user.getId());
                user1.setNickname(user.getNickname());
                update.set("user", user1);
                update.set("isBind", true);
                update.set("bindTime", new Date());
                deviceRepository.update(query, update);
                //把设备信息更新到用户
                Query query1 = new Query();
                Update update1 = new Update();
                query1.addCriteria(Criteria.where("id").is(userid));
                List<Device> devices = new ArrayList<>();
                Query query2=new Query();
                query2.addCriteria(Criteria.where("deviceId").is(number));
                Device de=deviceRepository.findByQuery(query2);
                devices.add(de);
                update1.set("devices", devices);
                userRepository.update(query1, update1);

        }

        return new ResponseVO();


    }
    /**
     *解除绑定(设备信息还不清楚，以后再完善)
     * @param appVersions
     * @return
     */@RequestMapping(value = "/removeBinding",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO removeBinding(@RequestParam(value="appVersions",required=true)String appVersions){
        if(Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        User user=userRepository.findById(userid);
        Query query1=new Query();
        query1.addCriteria(Criteria.where("user.id").is(userid));
        Device device= deviceRepository.findByQuery(query1);
        String deviceId=device.getDeviceId();
        //记录解除绑定的日志
        List<Device> devices=user.getDevices();
        for(int i=0;i<devices.size();i++){
            if(devices.get(i).getId()==device.getId()){
                devices.remove(i);
            }
        }
        user.setDevices(devices);
        Query query =new Query();
        query.addCriteria(Criteria.where("id").is(userid));
        Update update=new Update();
        update.set("devices",user.getDevices());
        userRepository.update(query,update);
        //从设备里删除用户,绑定状态和时间清空
        device.setUser(null);
        Query querys =new Query();
        querys.addCriteria(Criteria.where("user.id").is(userid));
        Update updates=new Update();
        updates.set("user",device.getUser());
        updates.set("isBind",false);
        updates.set("bindTime",null);
        deviceRepository.update(querys,updates);
        //返回解绑设备的编号
        return new ResponseVO(deviceId);
    }
    /**
     *修改手机号
     * @param appVersions
     * @param  captcha
     * @param  phoneNumber
     * @return
     */@RequestMapping(value = "/alterPhone",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO alterPhone(@RequestParam(value="appVersions",required=true)String appVersions,
                                    @RequestParam(value="captcha",required=true)String captcha,
                                    @RequestParam(value="phoneNumber",required=true)String phoneNumber){
        if(Util.stringNull(appVersions)&&Util.stringNull(captcha)&&Util.stringNull(phoneNumber)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        try {
            String str = MessageService.checkMsg(phoneNumber,captcha);
            if("success".equals(str)){
                log.info(phoneNumber+"验证码验证成功");
            }else{
                log.info(phoneNumber+"验证码验证失败");
                throw new CustomException(CustomExceptionCode.CaptchaError);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long userid= (long) request.getAttribute("userId");
       User user= userRepository.findByPhone(phoneNumber);
        if(user!=null){
            throw new CustomException(CustomExceptionCode.phoneExit);
        }
         Query query=new Query();
         Update update=new Update();
         query.addCriteria(Criteria.where("id").is(userid));
         update.set("phone",phoneNumber);
        userRepository.update(query,update);
        return new ResponseVO();

    }

    public  boolean isChineseByScript(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }
}
