package com.wizinno.livgo.app.controller;
import com.google.gson.Gson;
import com.wizinno.livgo.app.data.live.LiveDetail;
import com.wizinno.livgo.app.data.live.LiveThumbnail;
import com.livgo.app.mq.util.PushMessage;
import com.wizinno.livgo.app.data.user.LiveAudience;
import com.wizinno.livgo.app.document.*;
import com.wizinno.livgo.app.repository.*;
import com.wizinno.livgo.app.utils.JwtUtil;
import com.wizinno.livgo.app.utils.Url;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.app.utils.WeekTime;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.LiveType;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.data.RoleType;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;


/**
 * Created by Administrator on 2017/5/17.
 */
@RestController
@RequestMapping("/api/app")
public class LiveController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(LiveController.class);
    @Autowired
    private LiveRepository liveRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private InoutRepository inoutRepository;

    @Autowired
    private LiveMsgReponsitory liveMsgReponsitory;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LiveInviteRepository liveInviteRepository;

    /**
     * 查询好友正在直播的视频（只显示能看到的）
     *
     * @param nickName
     * @param
     * @return
     */
    @RequestMapping(value = "/liveQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveQury(@RequestParam(value = "nickName", required = false) String nickName) {
        long id = (long) request.getAttribute("userId");
        User user = userRepository.findById(id);
        List<Long> friendIdList = user.getFriends();
        List<Live> list = new ArrayList();
        List<Live> list1 = new ArrayList();
        if (friendIdList != null) {
            for (int i = 0; i < friendIdList.size(); i++) {
                //1.查询我所有的好友都有谁在直播
                Query query = new Query();
                query.addCriteria(Criteria.where("user._id").is(friendIdList.get(i)));
                query.addCriteria(Criteria.where("endTime").is(null));
                Live live = liveRepository.findByQuery(query);
                if (live != null) {
                    //2.如果是私人直播，
                    if (live.getType() == 1) {
                        List<LiveAudience> audiences = live.getAudience();
                        if (audiences != null && audiences.size() > 0) {
                            for (LiveAudience liveAudience : audiences) {
                                //2.1判断主播人有没有邀请你
                                if (liveAudience.getId() == id) {
                                    list1.add(live);
                                    break;
                                }

                            }
                        }


                    } else {
                        list1.add(live);
                    }

                }

            }
            if(!Util.stringNull(nickName)){
                if(list1.size()>0){
                    for(Live live:list1){
                        if(live.getUser().getNickname().indexOf(nickName)!=-1){
                            list.add(live);
                        }
                    }

                }
                return new ResponseVO(list);
            }else{
                return new ResponseVO(list1);
            }

        } else {
            return new ResponseVO(0);
        }


    }

    /**
     * 查询好友历史直播
     */
    @RequestMapping(value = "queryHistoryLive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO queryHistoryLive(@RequestParam(value = "nickName", required = false) String nickName) {
        long id = (long) request.getAttribute("userId");
        User user = userRepository.findById(id);
        List<Long> friendIdList = user.getFriends();
        List<Live> list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 7);
        Date d = c.getTime();
        //查询好友的历史直播（最近一周按时间排序）
        if(nickName!=null){
            Query query=new Query();
            Sort sort=new Sort(new Sort.Order(Sort.Direction.DESC ,"startTime"));
            query.addCriteria(Criteria.where("user.nickname").regex(nickName));
            query.addCriteria(Criteria.where("startTime").gt(d).lt(new Date()));
            query.addCriteria(Criteria.where("endTime").ne(null));
            query.with(sort);
            List<Live> lives= liveRepository.findByQuerys(query);
            if(lives!=null&&lives.size()>0){
                for(Live live:lives){
                    list.add(live);
                }
            }
        }else{
            if(friendIdList!=null&&friendIdList.size()>0){
                for(Long friendId:friendIdList){
                    Query query=new Query();
                    Sort sort=new Sort(new Sort.Order(Sort.Direction.DESC ,"startTime"));
                    query.addCriteria(Criteria.where("user.username").is(userRepository.findById(friendId).getUsername()));
                    query.addCriteria(Criteria.where("startTime").gt(d).lt(new Date()));
                    query.addCriteria(Criteria.where("endTime").ne(null));
                    query.with(sort);
                    List<Live> lives= liveRepository.findByQuerys(query);
                    if(lives!=null&&lives.size()>0){
                        for(Live live:lives){
                            list.add(live);
                        }
                    }
                }
                if(list!=null&&list.size()>0){
                    for(int i=0;i<list.size();i++){
                        for(int j=0;j<list.size()-1;j++){
                            if(list.get(i).getStartTime().getTime()>list.get(j).getStartTime().getTime()){
                                Live live=list.get(i);
                                list.set(i,list.get(j));
                                list.set(j,live);
                            }
                        }

                    }
                }
            }
        }

        return new ResponseVO(list);
    }


    /**
     * 我的历史直播(最近一周)
     */
    @RequestMapping(value = "myHistoryLive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO myHistoryLive() {
        long id = (long) request.getAttribute("userId");
        Query query = new Query();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "startTime"));
        query.addCriteria(Criteria.where("user.id").is(id));
        query.addCriteria(Criteria.where("endTime").ne(null));
        query.addCriteria(Criteria.where("startTime").gt(WeekTime.getPastDate()).lt(new Date()));
        List<Live> lives = liveRepository.findByQuerys(query);
        return new ResponseVO(lives);

    }

    /**
     * 开启直播间
     *
     * @param appVersions app版本
     * @param invite      邀请的朋友
     * @param location    直播地理位置
     * @param livePicture 直播缩略图
     * @param type        直播类型
     * @return
     */

    @RequestMapping(value = "/createLiveRoom", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO startLive(@RequestParam(value = "appVersions", required = true) String appVersions,
                                @RequestParam(value = "invite", required = false) long[] invite,
                                @RequestParam(value = "location", required = true) String location,
                                @RequestParam(value = "livePicture", required = true) String livePicture,
                                @RequestParam(value = "type", required = true) Integer type) {
        if (appVersions == null && location == null && livePicture == null) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        List<LiveAudience> list = new ArrayList<>();
        long id = (long) request.getAttribute("userId");
        //查询直播id为***的是否正在直播，是强行关闭
        User user1 = userRepository.findById(id);
        String str = user1.getUsername();
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("user.id").is(id));
        query1.addCriteria(Criteria.where("endTime").is(null));
        Live live1 = liveRepository.findByQuery(query1);
        if (live1 != null) {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("id").is(live1.getId()));
            Update update = new Update();
            update.set("endTime", new Date());
            liveRepository.update(query2, update);
        }
        User user = userRepository.findById(id);
        //直播的次数插入用户表
        Integer liveCount = user.getLiveCounts();
        if (liveCount == null) {
            liveCount = 1;
        } else {
            liveCount = liveCount + 1;
        }
        Query query2 = new Query();
        Update update2 = new Update();
        query2.addCriteria(Criteria.where("id").is(id));
        update2.set("liveCounts", liveCount);
        userRepository.update(query2, update2);
        LiveAudience liveAudience1 = new LiveAudience();
        liveAudience1.setId(id);
        liveAudience1.setNickname(user.getNickname());
        liveAudience1.setUsername(user.getUsername());
        liveAudience1.setImg(user.getImg());
        Live live = new Live();
        live.setLiveStatue(0);
        live.setUser(liveAudience1);
        live.setStartTime(new Date());
        live.setEndTime(null);
        if (type == LiveType.publicLive.toCode()) {
            live.setType(LiveType.publicLive.toCode());
        } else if (type == LiveType.privateLive.toCode()) {
            live.setType(LiveType.privateLive.toCode());
            if (invite != null) {
                for (int i = 0; i < invite.length; i++) {
                    LiveAudience liveAudience = new LiveAudience();
                    User audience = userRepository.findById(invite[i]);
                    liveAudience.setId(audience.getId());
                    liveAudience.setNickname(audience.getNickname());
                    liveAudience.setUsername(audience.getUsername());
                    list.add(liveAudience);
                }
            }

            live.setAudience(list);
        }
        live.setLocation(location);//直播地理位置
        live.setLivePicture(livePicture);
        liveRepository.save(live);
        Long liveId = live.getId();
        //保存推流Url
        live.setPushUrl(Url.getPushUrl(liveId));
        Map playUrl1 = Url.getPlayUrl(liveId);
        //保存播放url
        live.setPlayUrl((HashMap) playUrl1);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(liveId));
        Update update = new Update();
        update.set("pushUrl", live.getPushUrl());
        update.push("playUrl", live.getPlayUrl());
        liveRepository.update(query, update);
        return new ResponseVO(live);
    }


    /**
     * 邀请好友
     *
     * @param appVersions app版本
     * @param invite      邀请好友的数组
     * @param liveId      直播间id
     * @return
     */
    @RequestMapping(value = "/inviteFriends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO inviteFriends(@RequestParam(value = "appVersions", required = true) String appVersions,
                                    @RequestParam(value = "invite", required = true) long[] invite,
                                    @RequestParam(value = "liveId", required = true) long liveId) {
        if (appVersions != null && invite != null && liveId != 0) {
            long id = (long) request.getAttribute("userId");
            String userName = userRepository.findById(id).getUsername();
            Map<String, String> param = new HashMap<>();
            Collection<String> usernames = new ArrayList<>();
            Live live = liveRepository.findById(liveId);
            List<LiveAudience> list = live.getAudience();
            List<Long> ids = new ArrayList<>();
            if (list != null) {
                //将观众的id放入到一个集合
                for (LiveAudience liveAudience1 : list) {
                    ids.add(liveAudience1.getId());
                }
            }

            for (int i = 0; i < invite.length; i++) {
                LiveAudience liveAudience = new LiveAudience();
                User user = userRepository.findById(invite[i]);
                //将主播人邀请你参加直播保存到记录表中
                LiveInvite liveInvite = new LiveInvite();
                liveInvite.setLiveImg(live.getUser().getImg());
                liveInvite.setLiveUserName(live.getUser().getUsername());
                liveInvite.setIsInviteName(user.getUsername());
                liveInvite.setCreateTime(new Date());
                liveInviteRepository.save(liveInvite);
                //将邀请的好友添加到直播表的观众属性中
                usernames.add(user.getUsername());
                liveAudience.setId(invite[i]);
                liveAudience.setNickname(user.getNickname());
                liveAudience.setUsername(user.getUsername());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(liveAudience);
                } else {
                    //查看观众是否被邀请过，被邀请过不再添加
                    if (!ids.contains(invite[i])) {
                        list.add(liveAudience);
                    }

                }

            }
            PushMessage.sendPushMessage(usernames, userName + "邀请你参加直播");
            param.put("msg",userName + "邀请你参见直播");
            //推送通知
            PushMessage.sendPushNotice(param,usernames);
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(liveId));
            Update update = new Update();
            update.set("audience", list);
            liveRepository.update(query, update);
            return new ResponseVO();
        } else {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
    }

    /**
     * 观众进入直播间
     *
     * @param appVersions app版本
     * @param liveId      直播间id
     * @return
     */
    @RequestMapping(value = "/spectator", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO spectator(@RequestParam(value = "appVersions", required = true) String appVersions,
                                @RequestParam(value = "liveId", required = true) long liveId) {
        if (appVersions == null && liveId == 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Map<String, String> param = new HashMap<>();
        Collection<String> usernames = new ArrayList<>();
        long userId = (long) request.getAttribute("userId");
        List<Long> userids = new ArrayList<>();
        Live live = liveRepository.findById(liveId);
        //查看该房间是私人直播还是公开直播
        Live liveType = liveRepository.findById(liveId);
        if (liveType.getType() == 0) {
            //进入的是公开直播
            LiveAudience audience = new LiveAudience();
            User user = userRepository.findById(userId);
            List<LiveAudience> audiences = liveType.getAudience();
            audience.setId(userId);
            audience.setNickname(user.getNickname());
            audience.setUsername(user.getUsername());
            if (audiences != null) {
                for (LiveAudience audience1 : audiences) {
                    userids.add(audience1.getId());
                }
                if (!userids.contains(userId)) {
                    audiences.add(audience);
                }

            } else {
                audiences = new ArrayList<>();
                audiences.add(audience);
            }
            live.setAudience(audiences);
            //更新公开直播的数据库
            Query query = new Query();
            Update update = new Update();
            query.addCriteria(Criteria.where("id").is(liveId));
            update.set("audience", audiences);
            liveRepository.update(query, update);
            //记录日志


        } else {
            //进入私人直播
            List<LiveAudience> list = live.getAudience();
            int joinAudience = 0;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    //查看直播间观众是否满5个人（条件是wacthLive=true)
                    if (list.get(i).isWatchLive()) {
                        joinAudience++;
                    }
                }
                if (joinAudience < 5) {
                    for (LiveAudience audience : list) {
                        if (audience.getId() == userId) {
                            //改变该观众的状态
                            audience.setWatchLive(true);
                            Query query = new Query();
                            Update update = new Update();
                            query.addCriteria(Criteria.where("id").is(liveId));
                            update.set("audience", list);
                            liveRepository.update(query, update);
                            break;
                        }
                    }

                } else {
                    throw new CustomException(CustomExceptionCode.roomFull);
                }
            } else {
                throw new CustomException(CustomExceptionCode.noAutho);
            }
        }
        //想主播和在直播间的观众推送消息
        User user = userRepository.findById(userId);
        String username2 = liveRepository.findById(liveId).getUser().getUsername();
        List<LiveAudience> audiences = live.getAudience();
        if (audiences != null && audiences.size() > 0) {
            for (LiveAudience audience : audiences) {
                if (audience.isWatchLive()) {
                    usernames.add(audience.getUsername());
                }
            }
        }
        usernames.add(username2);
        PushMessage.sendPushMessage(usernames, user.getUsername() + "_user@:进入了直播间");
        //把用户发的消息存数据库
        LiveMsg liveMsg = new LiveMsg();
        liveMsg.setLiveId(liveId);
        liveMsg.setSendUsrname(user.getUsername());
        liveMsg.setContext("进入了直播间");
        liveMsg.setCreatTime(new Date());
        liveMsg.setRole(RoleType.audience.toCode());
        liveMsgReponsitory.save(liveMsg);
        //观众进入直播更新进入进出表
        InOut inOut = new InOut();
        inOut.setLiveId(liveId);
        inOut.setUserId(userId);
        inOut.setInTime(new Date());
        inOut.setCount(live.getAudience().size());
        inoutRepository.save(inOut);
        String username = userRepository.findById(userId).getUsername();
//        InterfaceMq.sendMessage(InterfaceMq.audineceJoin,username+MessageType.EnterLive);
        return new ResponseVO(Url.getPlayUrl(liveId));
    }

    /**
     * 踢出直播间的观众/观众退出直播间
     *
     * @param appVersions app版本
     * @param liveId      直播间id
     * @param userId
     * @return
     */
    @RequestMapping(value = "/outlive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO outlive(@RequestParam(value = "appVersions", required = true) String appVersions,
                              @RequestParam(value = "liveId", required = true) long liveId,
                              @RequestParam(value = "userId", required = false) Long userId) {
        if (appVersions == null && liveId == 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Map<String, String> param = new HashMap<>();
        Collection<String> usernames = new ArrayList<>();
        long id = (long) request.getAttribute("userId");
        Live liv = liveRepository.findById(liveId);
        if (userId == null) {
            Query qu = new Query();
            Update up = new Update();
            qu.addCriteria(Criteria.where("liveId").is(liveId));
            qu.addCriteria(Criteria.where("userId").is(id));
            qu.addCriteria(Criteria.where("inTime").ne(null));
            qu.addCriteria(Criteria.where("outTime").is(null));
            up.set("outTime", new Date());
            inoutRepository.update(qu, up);
            Live live = liveRepository.findById(liveId);
            List<LiveAudience> list = live.getAudience();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == id) {
                        list.remove(i);
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == id) {
                    list.remove(i);
                }
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(liveId));
            Update update = new Update();
            update.set("audience", list);
            liveRepository.update(query, update);
            //观众退出直播间消息
            String username1 = userRepository.findById(id).getUsername();
            List<LiveAudience> audiences = live.getAudience();
            if (audiences != null && audiences.size() > 0) {
                for (LiveAudience audience : audiences) {
                    if (live.getType() == LiveType.privateLive.toCode()) {
                        if (audience.isWatchLive()) {
                            usernames.add(audience.getUsername());
                        }
                    } else if (live.getType() == LiveType.publicLive.toCode()) {
                        usernames.add(audience.getUsername());
                    }

                }
            }
            usernames.add(liv.getUser().getUsername());
            PushMessage.sendPushMessage(usernames, username1 + "_user@:退出直播间");
            //把用户发的消息存数据库
            LiveMsg liveMsg = new LiveMsg();
            liveMsg.setLiveId(liveId);
            liveMsg.setSendUsrname(username1);
            liveMsg.setContext("退出直播间");
            liveMsg.setCreatTime(new Date());
            liveMsg.setRole(RoleType.audience.toCode());
            liveMsgReponsitory.save(liveMsg);

        } else {
            //踢出观众直播间记录进出入表
            Query qu = new Query();
            Update up = new Update();
            qu.addCriteria(Criteria.where("liveId").is(liveId));
            qu.addCriteria(Criteria.where("userId").is(userId));
            qu.addCriteria(Criteria.where("inTime").ne(null));
            qu.addCriteria(Criteria.where("outTime").is(null));
            up.set("outTime", new Date());
            inoutRepository.update(qu, up);
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("id").is(liveId));
            query1.addCriteria(Criteria.where("endTime").is(null));
            Live live = liveRepository.findByQuery(query1);
            List<LiveAudience> list = live.getAudience();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == userId) {
                    list.remove(i);
                }
            }
            Update update = new Update();
            update.set("audience", list);
            liveRepository.update(query1, update);
            //踢出观众
            String livName = liveRepository.findById(liveId).getUser().getUsername();
            String aUserName = userRepository.findById(userId).getUsername();
            usernames.add(aUserName);
            PushMessage.sendPushMessage(usernames, livName + "把你踢出直播间");

        }

        return new ResponseVO(0);

    }

    /**
     * 直播间查看观众
     *
     * @param appVersions app版本
     * @param liveId      直播间id
     * @return
     */
    @RequestMapping(value = "/liveFriends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveFriends(@RequestParam(value = "appVersions", required = true) String appVersions,
                                  @RequestParam(value = "liveId", required = true) long liveId) {
        if (appVersions == null && liveId == 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long id = (long) request.getAttribute("userId");
        Live live = liveRepository.findById(liveId);
        List<LiveAudience> list = live.getAudience();
        List<User> listUser = new ArrayList<>();
        //私人直播的情况
        if (live.getType() == LiveType.privateLive.toCode()) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isWatchLive()) {
                        listUser.add(userRepository.findById(list.get(i).getId()));
                    }
                }
            }
        }
        if (live.getType() == LiveType.publicLive.toCode()) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    listUser.add(userRepository.findById(list.get(i).getId()));
                }

            }

        }
        //更新直播间心跳包时间
        Query query=new Query();
        Update update=new Update();
        query.addCriteria(Criteria.where("id").is(liveId));
        update.set("packageTime",new Date());
        liveRepository.update(query,update);
        return new ResponseVO(listUser);
    }

    /**
     * 关闭直播间
     *
     * @param appVersions app版本
     * @param liveId      直播间id
     * @return
     */
    @RequestMapping(value = "/closeLiveRoom", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO endLive(@RequestParam(value = "appVersions", required = true) String appVersions,
                              @RequestParam(value = "liveId", required = true) long liveId) {
        if (appVersions == null && liveId == 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        //首先判断直播间是否已被关闭
        Date time=liveRepository.findById(liveId).getEndTime();
        if (time==null) {
            Map<String, String> param = new HashMap<>();
            Collection<String> usernames = new ArrayList<>();
            long id = (long) request.getAttribute("userId");
            //获取直播间观众人次
            Integer audienceTime = 0;
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("liveId").is(liveId));
            query2.addCriteria(Criteria.where("inTime").ne(null));
            List<InOut> audienceTimes = inoutRepository.findByQuerys(query2);
            if (audienceTimes != null && audienceTimes.size() > 0) {
                audienceTime = audienceTimes.size();
            }
            //获取直播间观众人数的峰值
            Integer maxcount = 0;
            Query q = new Query();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "count"));
            q.with(sort);
            q.addCriteria(Criteria.where("liveId").is(liveId));
            List<InOut> list = inoutRepository.findByQuerys(q);
            if (list != null && list.size() > 0) {
                maxcount = list.get(0).getCount();
            }

            Live live = liveRepository.findById(liveId);
            Integer count = userRepository.findById(live.getUser().getId()).getLiveCounts();
            if (count == null) {
                count = 0;
            }
            live.setEndTime(new Date());
            live.setCount(audienceTime);
            live.setMaxCoun(maxcount);
            live.setHistoryCount(count);
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(liveId));
            Update update = new Update();
            update.set("endTime", live.getEndTime());
            update.set("count", audienceTime);
            update.set("maxCount", maxcount);
            update.set("historyCount", count);
            liveRepository.update(query, update);

            //推送消息
            User user = userRepository.findById(id);
            String livName = user.getUsername();
            List<LiveAudience> audiences = live.getAudience();
            if (audiences != null && audiences.size() > 0) {
                for (LiveAudience liveAudience : audiences) {
                    if(live.getType()==LiveType.publicLive.toCode()){
                        String username = liveAudience.getUsername();
                        usernames.add(username);
                    }else if(live.getType()==LiveType.privateLive.toCode()){
                        if (liveAudience.isWatchLive()) {
                            String username = liveAudience.getUsername();
                            usernames.add(username);
                        }
                    }

                }
                PushMessage.sendPushMessage(usernames, livName + "_host@:关闭了直播间");
            }
            //把用户发的消息存数据库
            LiveMsg liveMsg = new LiveMsg();
            liveMsg.setLiveId(liveId);
            liveMsg.setSendUsrname(user.getUsername());
            liveMsg.setPicture(user.getImg());
            liveMsg.setContext("关闭了直播间");
            liveMsg.setCreatTime(new Date());
            liveMsgReponsitory.save(liveMsg);
            liveMsg.setRole(RoleType.live.toCode());
            return new ResponseVO(live);
        } else {
            return new ResponseVO();
        }


    }

    /**
     * 手机推流状态更新通知
     *
     * @param appVersions app版本
     * @param liveId      直播间id
     * @param state       设备状态代码
     * @return
     */
    @RequestMapping(value = "/notifyLiveState", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO notifyLiveState(@RequestParam(value = "appVersions", required = true) String appVersions,
                                      @RequestParam(value = "liveId", required = true) Long liveId,
                                      @RequestParam(value = "state", required = true) Integer state) {
        long id = (long) request.getAttribute("userId");
        Collection<String> usernames = new ArrayList<>();
        if(state==2||state==1){
            //更改直播间的直播状态
            Query query2=new Query();
            Update update=new Update();
            query2.addCriteria(Criteria.where("id").is(liveId));
            update.set("liveStatue",1);
            liveRepository.update(query2,update);
        }
        User user= userRepository.findById(id);
        usernames.add(user.getUsername());
        PushMessage.sendPushMessage(usernames, user.getUsername() + "开始直播");
        return new ResponseVO();


    }

    /**
     * 查询那些人邀请我参加私人直播
     * @param appVersions app版本
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/inviteLive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO inviteLive(@RequestParam(value = "appVersions", required = true) String appVersions) {
        long id = (long) request.getAttribute("userId");
        if (Util.stringNull(appVersions)) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        User user = userRepository.findById(id);
        List<Long> friendIds = user.getFriends();
        List<Live> lives = new ArrayList<>();
        List<LiveAudience> liveUsers = new ArrayList<>();
        //先查询我的哪些好友在直播
        if (friendIds != null) {
            for (Long friendId : friendIds) {
                Query query = new Query();
                query.addCriteria(Criteria.where("user.id").is(friendId));
                query.addCriteria(Criteria.where("endTime").is(null));
                query.addCriteria(Criteria.where("type").is(LiveType.privateLive.toCode()));
                Live live = liveRepository.findByQuery(query);
                if (live != null) {
                    lives.add(live);
                }

            }
            //查询正在进行的直播有谁邀请我
            for (Live live : lives) {
                List<LiveAudience> liveAudiences = live.getAudience();
                for (LiveAudience liveAudience : liveAudiences) {
                    if (liveAudience.getId() == id) {
                        liveUsers.add(live.getUser());
                    }
                    break;
                }
            }
            return new ResponseVO(liveUsers);

        } else {
            return new ResponseVO();
        }


    }

    /**
     * 缩略图
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/Thumbnail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveThumbnail(@RequestBody LiveThumbnail liveThumbnail) {
        if (liveThumbnail != null) {
            String id = liveThumbnail.getChannel_id();
            String[] str = id.split("_");
            Long liveId = Long.parseLong(str[1]);
            Query query = new Query();
            Update update = new Update();
            query.addCriteria(Criteria.where("id").is(liveId));
            update.set("livePicture", liveThumbnail.getPic_full_url());
            liveRepository.update(query, update);
        }
        return new ResponseVO(liveThumbnail);
    }

    /**
     * 发送消息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseVO sendMessage() throws Exception {
        ServletInputStream inStream = request.getInputStream();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] message = swapStream.toByteArray();
        if (message != null) {
            User user = null;
            msgEntity.Msg info = msgEntity.Msg.parseFrom(message);
            String content = info.getContent();
            String token = info.getToken();
            Long liveId = (long) info.getLiveId();
            if (token != null) {
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    String subject = claims.getSubject();
                    Gson gson = new Gson();
                    user = gson.fromJson(subject, User.class);
                }
            }
            user=userRepository.findById(user.getId());
            Live live = liveRepository.findById(liveId);
            if (live == null) {
                throw new CustomException(CustomExceptionCode.liveNoExits);
            }
            List<LiveAudience> audiences = live.getAudience();

            Collection<String> usernames = new ArrayList<>();
            //把用户发的消息存数据库
            LiveMsg liveMsg = new LiveMsg();
            if (live.getUser().getUsername().equals(user.getUsername())) {
                liveMsg.setRole(RoleType.live.toCode());
            } else {
                liveMsg.setRole(RoleType.audience.toCode());
            }
            liveMsg.setPicture(user.getImg());
            liveMsg.setLiveId(liveId);
            liveMsg.setSendUsrname(user.getUsername());
            liveMsg.setContext(content);
            liveMsg.setCreatTime(new Date());

            liveMsgReponsitory.save(liveMsg);
            //极光推送消息
            if (live.getType() == LiveType.publicLive.toCode()) {
                //公开直播
                if (audiences != null && audiences.size() > 0) {
                    for (LiveAudience audience : audiences) {
                        usernames.add(audience.getUsername());
                    }
                }
            }
            if (live.getType() == LiveType.privateLive.toCode()) {
                //私人直播
                if (audiences.size() > 0) {
                    for (LiveAudience audience : audiences) {
                        if (audience.isWatchLive()) {
                            usernames.add(audience.getUsername());
                        }


                    }
                }
            }
            usernames.add(live.getUser().getUsername());
            String allContent = null;
            if (live.getUser().getUsername().equals(user.getUsername())) {
                allContent = user.getUsername() + "_host@:" + content;
            } else {
                allContent = user.getUsername() + "_user@:" + content;
            }

            PushMessage.sendPushMessage(usernames, allContent);
            return new ResponseVO();


        } else {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }


    }

    /**
     * 获取直播间的所有历史消息
     *
     * @param liveId
     * @param
     * @return
     */
    @RequestMapping(value = "/historyMessage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO historyMessage(@RequestParam(value = "liveId", required = true) Long liveId) {
        if (liveId <= 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long id = (long) request.getAttribute("userId");
        //查询直播间的历史消息
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "creatTime"));
        Query query = new Query();
        query.addCriteria(Criteria.where("liveId").is(liveId));
        query.with(sort);
        List<LiveMsg> liveMsgs = liveMsgReponsitory.findByQuerys(query);
        Live live = liveRepository.findById(liveId);
        if (live == null) {
            throw new CustomException(CustomExceptionCode.liveNoExits);
        }
        LiveDetail liveDetail = new LiveDetail();
        liveDetail.setImg(live.getUser().getImg());
        liveDetail.setUserName(live.getUser().getUsername());
        liveDetail.setCount(live.getCount());
        liveDetail.setStartTime(live.getStartTime());
        liveDetail.setEndTime(live.getEndTime());
        liveDetail.setLiveMsg(liveMsgs);
        return new ResponseVO(liveDetail);

    }

    /**
     * 直播邀请记录
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/liveInviteRecords", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveInviteRecords() {
        long id = (long) request.getAttribute("userId");
        User user = userRepository.findById(id);
        Query query = new Query();
        query.addCriteria(Criteria.where("isInviteName").is(user.getUsername()));
        List<LiveInvite> liveInvites = liveInviteRepository.findByQuerys(query);
        return new ResponseVO(liveInvites);
    }
    /**
     * 直播间详情
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/liveDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveDetail(@RequestParam(value = "liveId", required = true) Long liveId){
        if (liveId <= 0) {
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Live live= liveRepository.findById(liveId);
        return  new ResponseVO(live);

    }
    /**
     * 回复直播
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/recoverLive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO recoverLive(){
        long id = (long) request.getAttribute("userId");
        User user=userRepository.findById(id);
        String userName=user.getUsername();
        Query query=new Query();
        query.addCriteria(Criteria.where("user.username").is(userName));
        query.addCriteria(Criteria.where("endTime").ne(null));
        Live live=liveRepository.findByQuery(query);
        Map url=new HashMap();
        if(live!=null){
            url=live.getPlayUrl();
        }
        return new ResponseVO(url);
    }
    /**
     * 异常关闭直播间
     *
     * @param liveId
     * @param
     * @return
     */
    @RequestMapping(value = "/exceptionCloseLive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO exceptionCloseLive(@RequestParam(value = "liveId", required = true) Long liveId){
        if(liveId==null){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Query query1=new Query();
        Update update1=new Update();
        query1.addCriteria(Criteria.where("id").is(liveId));
        update1.set("endTime",new Date());
    return new ResponseVO();
    }
}
