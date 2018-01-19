package com.wizinno.livgo.app.utils;

import com.alibaba.fastjson.JSON;
import com.livgo.app.mq.service.Message;
import com.livgo.app.mq.util.MessageType;
import com.livgo.app.mq.util.SessionUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageService {
    private static final String SERVER_SEND_URL = "https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_CHECK_URL = "https://api.netease.im/sms/verifycode.action";//验证验证码的请求路径URL
    private static final String APP_KEY = "4c34014938e579043aeea8f0e7678fd4";//网易云信分配的账号
    private static final String APP_SECRET = "7934960723bd";//网易云信分配的密钥
    private static final String NONCE = "12345";//随机数

    public static String sendMsg(String phone) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(SERVER_SEND_URL);

        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

        //设置请求的header
        post.addHeader("AppKey", APP_KEY);
        post.addHeader("Nonce", NONCE);
        post.addHeader("CurTime", curTime);
        post.addHeader("CheckSum", checkSum);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //设置请求参数
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

        //执行请求
        HttpResponse response = httpclient.execute(post);
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");

        //判断是否发送成功，发送成功返回true
        String code = JSON.parseObject(responseEntity).getString("code");
        if (code.equals("200")) {
            return "success";
        }
        return "error";
    }

    public static String checkMsg(String phone, String code) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(SERVER_CHECK_URL);

        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

        //设置请求的header
        post.addHeader("AppKey", APP_KEY);
        post.addHeader("Nonce", NONCE);
        post.addHeader("CurTime", curTime);
        post.addHeader("CheckSum", checkSum);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //设置请求参数
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));
        nameValuePairs.add(new BasicNameValuePair("code", code));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

        //执行请求
        HttpResponse response = httpclient.execute(post);
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");

        //判断是否发送成功，发送成功返回true
        code = JSON.parseObject(responseEntity).getString("code");
        if (code.equals("200")) {
            return "success";
        }
        return "error";
    }

    public static void liveMessage(List<Message> mgs, String liveUsername, String userName,
                                   List<String> messages, String type) {
        for (Message message : mgs) {
            if (message != null) {
                String str = message.msgBody;
                String msgId = message.msgId;
                String str1 = null;
                if (type.equals("被踢出直播间")) {
                    str1 = userName + type;
                } else {
                    str1 = liveUsername + type;
                }
                    if (str.equals(str1)) {
                        List<String> mids = (List<String>) SessionUtil.getSessionAttribute(userName);
                        if (mids != null) {
                            if (!mids.contains(message.msgId)) {
                                messages.add(str);
                                mids.add(msgId);
                            }
                        } else {
                            mids = new ArrayList<>();
                            mids.add(msgId);
                            messages.add(str);
                            SessionUtil.setSessionAttribute(userName, mids);
                        }

                    }
                }
            }

        }
    }

