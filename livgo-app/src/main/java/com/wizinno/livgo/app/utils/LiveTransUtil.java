package com.wizinno.livgo.app.utils;

import com.wizinno.livgo.app.data.live.LiveExtend;
import com.wizinno.livgo.app.data.live.LiveHistory;
import com.wizinno.livgo.app.data.live.LiveSimple;
import com.wizinno.livgo.app.data.user.LiveAudience;
import com.wizinno.livgo.app.data.user.UserExtend;
import com.wizinno.livgo.app.document.InOut;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.InoutRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.data.LiveType;
import com.wizinno.livgo.data.LogOperation;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.data.Sex;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-22.
 */
@Component
public class LiveTransUtil {
    @Autowired
    private LiveRepository liveRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InoutRepository inoutRepository;
    private static LiveTransUtil liveTransUtil;

    public void setLiveRepository(LiveRepository liveRepository){
        this.liveRepository=liveRepository;
    }
    public void setLogRepository(LogRepository logRepository){
        this.logRepository=logRepository;
    }
    public void setInoutRepository(InoutRepository inoutRepository){
        this.inoutRepository=inoutRepository;
    }
    public void setUserRepository(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @PostConstruct
    public void init(){
        liveTransUtil=this;
        liveTransUtil.liveRepository=this.liveRepository;
        liveTransUtil.logRepository=this.logRepository;
        liveTransUtil.inoutRepository=this.inoutRepository;
        liveTransUtil.userRepository=this.userRepository;

    }

    public static LiveHistory liveHistory(InOut inOut){
        if(inOut!=null){
            LiveHistory liveHistory= new LiveHistory();
            BeanUtils.copyProperties(inOut,liveHistory);
            //直播类型
            Live live= liveTransUtil.liveRepository.findById(inOut.getLiveId());
            liveHistory.setTypeDesc(LiveType.getNameByCode(live.getType()));
            liveHistory.setUsername(liveTransUtil.userRepository.findById(inOut.getUserId()).getUsername());
            //观众被邀请时间
            Date inviteTime=null;
            Long userId=inOut.getUserId();
            if(live.getType()==1){
                Criteria criteria=new Criteria();
                criteria.andOperator(criteria.where("liveId").is(inOut.getLiveId()),criteria.where("friendId").is(userId));
                criteria.orOperator(criteria.where("operation").is(LogOperation.inviteLive.toCode()),
                        criteria.where("operation").is(LogOperation.startLive.toCode()));
                List<Log> logs=liveTransUtil.logRepository.findByQuerys(new Query(criteria));
                inviteTime= logs.get(0).getCreateTime();

            }else{
                inviteTime=null;
            }
            liveHistory.setInviteTime(inviteTime);;

            //观众驻留时间
            long stayTime;
            Date outTime=inOut.getOutTime();
            Date inTime=inOut.getInTime();
            if(outTime!=null){
                stayTime=(outTime.getTime()-inTime.getTime())/1000;
            }else{
                stayTime=(new Date().getTime()-inTime.getTime())/1000;
            }
            liveHistory.setStayTime(stayTime);
            return liveHistory;
        }else{
            return null;
        }

    }
    public static List<LiveHistory> liveHistoryList(List<InOut> inOuts) {
        if (inOuts != null && inOuts.size() > 0) {
            List<LiveHistory> liveHistories = new ArrayList<>();
            for (InOut inOut : inOuts) {
                liveHistories.add(liveHistory(inOut));
            }
            return liveHistories;
        } else {
            return null;
        }
    }
    public static LiveExtend liveToExtend(Live live){
    if(live !=null){
        Integer audienceTime=0;
        LiveExtend liveExtend=new LiveExtend();
        BeanUtils.copyProperties(live,liveExtend);
        List<LiveAudience> c=live.getAudience();
        Integer fCount=0;//好友数量
        if(c!=null&&c.size()>0){
            fCount=c.size();
        }
        liveExtend.setfCounts(fCount);
        liveExtend.setTypeDesc(LiveType.getNameByCode(live.getType()));
        List audienceId=new ArrayList();//正在观看直播观众id的集合
        List<LiveAudience> list4= live.getAudience();
        int audienceCount = 0;
        if(list4==null){
            audienceCount=0;
        }else{
           for(int k=0;k<list4.size();k++){
               if(list4.get(k).isWatchLive()){
                   //将正在观看直播观众的Id添加的集合
                   audienceId.add(list4.get(k).getId());
                   audienceCount=audienceCount+1;
               }
           }

        }

        liveExtend.setLiveId(live.getId());//直播id
        liveExtend.setUserId(live.getUser().getId());//主播id
        liveExtend.setName(live.getUser().getUsername());
        liveExtend.setAudiences(audienceCount);//正在看直播观众人数
        //观众人次
        Query query2=new Query();
        query2.addCriteria(Criteria.where("liveId").is(live.getId()));
        query2.addCriteria(Criteria.where("inTime").ne(null));
        List<InOut> audienceTimes= liveTransUtil.inoutRepository.findByQuerys(query2);
        if(audienceTimes!=null){
            audienceTime=audienceTimes.size();
        }
        liveExtend.setAudienceTime(audienceTime);
        long totalStayTime = 0;//驻留时间
        long avgStayTime=0;//平均驻留时间
        //获取平均驻留时间
        Query qu=new Query();
        qu.addCriteria(Criteria.where("liveId").is(live.getId()));
        qu.addCriteria(Criteria.where("userId").ne(null));
        qu.addCriteria(Criteria.where("inTime").ne(null));
        qu.addCriteria(Criteria.where("outTime").ne(null));
       List<InOut> total=liveTransUtil.inoutRepository.findByQuerys(qu);
        if(total!=null&&total.size()>0){
            for(InOut inout:total){
                long stayTime= inout.getOutTime().getTime()-inout.getInTime().getTime();
                totalStayTime=totalStayTime+stayTime;
            }
            avgStayTime=totalStayTime/1000/total.size();
        }

        liveExtend.setAvgTime(avgStayTime);
        return liveExtend;

    }
    return null;
    }
    public static List<LiveExtend> liveToExtends(List<Live> lives) {
        if (lives != null && lives.size() > 0) {
            List<LiveExtend> liveExtends = new ArrayList<>();
            for (Live live : lives) {
                liveExtends.add(liveToExtend(live));
            }
            return liveExtends;
        } else {
            return null;
        }
    }
    public static LiveSimple liveToSimple(Live live){
    if(live!=null){
        LiveSimple liveSimple=new LiveSimple();
        List<String> list=new ArrayList<>();
        BeanUtils.copyProperties(live,liveSimple);
        liveSimple.setLiveName(live.getUser().getUsername());
        liveSimple.setTypeDesc(LiveType.getNameByCode(live.getType()));
        liveSimple.setLiveName(live.getUser().getUsername());
        List<LiveAudience> audiences=live.getAudience();
        if(audiences!=null){
            liveSimple.setCount(audiences.size());
            for(int i=0;i<audiences.size();i++){
                if(audiences.get(i).isWatchLive()){
                    String username= audiences.get(i).getUsername();
                    list.add(username+", ");
                }

            }
        }else{
            liveSimple.setCount(0);
            list.add(null);
        }
        liveSimple.setAudienceName(list);
        return liveSimple;


    }
    return null;

    }
    public static List<LiveSimple> livesToSimples(List<Live>lives){
        if(lives!=null&&lives.size()>0){
            List<LiveSimple> liveSimples=new ArrayList<>();
            for(Live live:lives){
                liveSimples.add(liveToSimple(live));
            }
            return liveSimples;
        }
        return null;
    }

}
