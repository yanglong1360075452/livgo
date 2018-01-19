package com.livgo.app.mq.interfaceMq;

import com.livgo.app.mq.service.Account;
import com.livgo.app.mq.service.Message;
import com.livgo.app.mq.service.Queue;
import com.livgo.app.mq.service.QueueMeta;
import com.livgo.app.mq.util.MessageType;
import java.util.List;

/**
 * Created by hp on 2017/7/20.
 */
public  class InterfaceMq {
    public static final String stopLive="wizinno-stoplive";
    public static final String startLive="wizinno-startlive";
    public static final String closeLive="wizinno-closelive";
    public static final String createLive = "wizinno-createlive";
    public static final String audineceJoin="wizinno-audienceJoin";
    public static final String pushAudience="wizinno-pushaudience";
    public static final String audienceLeave="wizinno-audienceLeave";
    public static final String secretId = "AKID57HOSNM7kCTefXzoEg09DmBQUN7gmHJD";
    public static final String secretKey = "ufFh1FIArNn394wiLqAMQM8j9OWUHuMe";
    public static final String endpoint = "http://cmq-queue-sh.api.qcloud.com";
//    public static HttpServletRequest request;
//    public static HttpSession session = request.getSession();
    //创建队列(主播开启直播间时创建自己的队列)
    public static final void createQueue(String username){
        Account account = new Account(endpoint,secretId, secretKey);
        System.out.println("---------------create queue ...---------------");
        QueueMeta meta = new QueueMeta();
        meta.pollingWaitSeconds = 10;
        meta.visibilityTimeout = 10;
        meta.maxMsgSize = 65536;
        meta.msgRetentionSeconds = 345600;
        try {
            account.createQueue(username,meta);
            sendMessage(username, MessageType.Createtype);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //发送消息
    public static final void sendMessage(String username,String type){
        Account account = new Account(endpoint,secretId, secretKey);
        System.out.println("---------------create queue ...---------------");
        QueueMeta meta = new QueueMeta();
        meta.pollingWaitSeconds = 10;
        meta.visibilityTimeout = 10;
        meta.maxMsgSize = 65536;
        meta.msgRetentionSeconds = 345600;
        try {
            //获取队列对象并发送此队列对象的消息
            Queue queue = (Queue) account.getQueue(username);
            if(type.equals("进入直播间")||type.equals("退出直播间")){
                queue.sendMessage(type);
            }else{
                queue.sendMessage(type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //接受此队列的消息对象
    public static final List<Message> receiveMessage (String username){
        Account account = new Account(endpoint,secretId, secretKey);
        List<Message> msgList = null;
        try {
            Queue queue = (Queue) account.getQueue(username);
            msgList = queue.batchReceiveMessage(15,1,username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  msgList;
    }
    //用户退出直播间删除这个用户消息
    public static final void deleteUserMsg (String username,String queueName){
        Account account = new Account(endpoint,secretId, secretKey);
        List<Message> msgList = null;
        try {
            Queue queue = (Queue) account.getQueue(queueName);//获取当前主播的队列
            msgList = queue.batchReceiveMessage(15,10,queueName);
            for(int i=0;i<msgList.size();i++){
                if(msgList.get(i).msgBody.indexOf(username)!=-1){
                    queue.deleteMessage(msgList.get(i).receiptHandle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //主播退出直播间删除这个主播队列并删除队列下的所有消息
    public static final void deleteLiveUserMsg (String username){
        Account account = new Account(endpoint,secretId, secretKey);
        try {
            account.deleteQueue("qwer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    Account account = new Account(endpoint,secretId, secretKey);
//     account.deleteQueue("qwer");
}
