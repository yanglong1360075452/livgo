package com.livgo.app.mq.util;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.wizinno.livgo.app.document.msgEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class PushMessage {
    private static final Logger log = LoggerFactory.getLogger(PushMessage.class);
    // 这是在极光网站上申请的密钥
    private static final String masterSecret = "72725b2081ec3e2b2dced4eb";
    // 应用的appKey,同样在网站上申请
    private static final String appKey = "c8f8962d3cc5cae923c195da";


    /**
     * 推送通知接口
     */
    public static void sendPushNotice(Map<String, String> parm, Collection<String> usernames) {
        //创建JPushClient
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        //推送的关键,构造一个payload
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())//指定android平台的用户
                .setAudience(Audience.alias(usernames))//指定发消息的设备
                .setNotification(Notification.android(parm.get("msg"), "通知：", parm))
                //发送内容,这里是我从controller层中拿过来的参数)
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                //这里是指定开发环境,不用设置也没关系
                .setMessage(Message.content(parm.get("msg")))//自定义信息
                .build();

        try {
            PushResult pu = jpushClient.sendPush(payload);

        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }
    /**
     * 推送自定义消息接口.根据别名修改标签（tag）
     * @param usernames 别名
     * @param content 推送内容
     */
    public static void sendPushMessage(Collection<String> usernames, String content) {
        //创建JPushClient
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        PushPayload payload = null;
            payload = PushPayload.newBuilder()
                    .setAudience(Audience.alias(usernames))
                    .setPlatform(Platform.android_ios())
                    .setMessage(Message.content(content)).build();
        try {
            System.out.println(payload.toString());
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {

        } catch (APIRequestException e) {

        }
    }
    public static void main(String[] args) throws IOException {
//        msgEntity.Msg.Builder builder=msgEntity.Msg.newBuilder();
//        builder.setLiveId(563);
//        builder.setToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE1MDQzMzAzNzUsInN1YiI6IntcInVzZXJuYW1lXCI6XCJxd2VyXCIsXCJpZFwiOjN9IiwiZXhwIjoxNTA1MTc0MDUzfQ.ixjHB09f21Lol54ZXC9lmM3Iqw0LvMid-fj37o4majA");
//        builder.setContent("aaa");
//        msgEntity.Msg msg=builder.build();
//        byte[]message= msg.toByteArray();
//        for(int i=0;i<message.length;i++){
//            System.out.print(message[i]+",");
//        }

    }
    }

