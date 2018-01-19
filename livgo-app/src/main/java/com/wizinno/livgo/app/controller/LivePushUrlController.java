package com.wizinno.livgo.app.controller;
import com.livgo.app.mq.util.PushMessage;
import com.wizinno.livgo.app.document.*;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by HP on 2017/6/15.
 */
@RestController
@RequestMapping("/api/app")
public class LivePushUrlController extends BaseController{

    private static Logger log = LoggerFactory.getLogger(LivePushUrlController.class);
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private  LiveRepository liveRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;


     String bizid= Constant.BIZID;
    String securityKey=Constant.HONGJING_PUSH_ANTI_THEFT_CHAIN_KEY;

//    public static void main(String[] args) {
//        LivePushUrlController livePushUrlController = new LivePushUrlController();
//        System.out.println(livePushUrlController.responseTencentLive(1L,2l));
//    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};



    @RequestMapping(value = "/getLivePushUrl",method = RequestMethod.GET)
    private ResponseVO responseTencentLive(@RequestParam(value = "deviceNumber",required= true )String deviceNumber){

        Query query = new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceNumber));

        Device device = deviceRepository.findByQuery(query);
        if(device == null){
            throw new CustomException(CustomExceptionCode.liveIdNotExits);
        }
        User user = device.getUser();

        if(user == null){
            throw new CustomException(CustomExceptionCode.liveIdNotExits);
        }

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("user.id").is(user.getId()));
        query1.addCriteria(Criteria.where("endTime").is(null));
        Live live = liveRepository.findByQuery(query1);

        if (live != null ){
           User user1=userRepository.findById(live.getUser().getId());
           //在直播间插入设备序列号
            live.setDeviceNumber(deviceNumber);
            Query query2=new Query();
            Update update=new Update();
            query2.addCriteria(Criteria.where("id").is(live.getId()));
            update.set("deviceNumber",deviceNumber);
            liveRepository.update(query2,update);
                return new ResponseVO(live.getPushUrl());
        }else {
            throw new CustomException(CustomExceptionCode.liveIdNotExits);
        }


    }

    private  String getPushUrl(String roomId){
        StringBuilder url = new StringBuilder();

        long insertTime = (long) (System.currentTimeMillis() / 1000);
        long outTime = insertTime + 60*60*24;//25tian
        url.append("rtmp://").append(bizid).append(Constant.URL_TENCENT_COMMON_PUSH).append(bizid).append("_");
        url.append(roomId).append("?").append("bizid=").append(bizid).append("&")
                .append(getSafeUrl(securityKey, bizid+"_"+roomId, outTime));


        System.out.println(url.toString());
        String s = url.toString();
       // InterfaceMq.sendMessage(username, MessageType.Starttype);
        return s;
    }


    /*
     * KEY+ stream_id + txTime
     */
    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

}
