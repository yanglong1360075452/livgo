package com.wizinno.livgo.app.controller;
import com.livgo.app.mq.interfaceMq.InterfaceMq;
import com.livgo.app.mq.service.Message;
import com.livgo.app.mq.util.MessageType;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.MessageService;
import com.wizinno.livgo.data.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */
@RestController
@RequestMapping("/api/app/message")
public class MessageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LiveRepository liveRepository;
    /**
     *观众获取主播创建直播间消息
     * @param
     * @return
     */
    @RequestMapping(value = "/liveMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO liveMessage(){
        long id = (long) request.getAttribute("userId");
         User user= userRepository.findById(id);
        String userName=user.getUsername();
         List<Long> list=user.getFriends();
        List<String> messages=new ArrayList<>();
        List<Message>  mgs= InterfaceMq.receiveMessage(InterfaceMq.createLive);
        List<Message> startLive=InterfaceMq.receiveMessage(InterfaceMq.startLive);
        List<Message> stopLive=InterfaceMq.receiveMessage(InterfaceMq.stopLive);
        List<Message> closeLive=InterfaceMq.receiveMessage(InterfaceMq.closeLive);
        List<Message> pusshLive=InterfaceMq.receiveMessage(InterfaceMq.pushAudience);
        for(Long friendId:list){
            User userFriend=userRepository.findById(friendId);
            String liveUsername=userFriend.getUsername();
            if(mgs!=null){
                //创建直播间的消息
                MessageService.liveMessage(mgs,liveUsername,userName,messages,MessageType.Createtype);
            }
          if(startLive!=null){
              //开始直播的消息
              MessageService.liveMessage(startLive,liveUsername,userName,messages,MessageType.Starttype);
          }
          if(closeLive!=null){
              //关闭直播的消息
              MessageService.liveMessage(closeLive,liveUsername,userName,messages,MessageType.Closetype);
          }
          if(pusshLive!=null){
              //被踢出直播间的消息
              MessageService.liveMessage(pusshLive,liveUsername,userName,messages,MessageType.pushOut);
          }

            }

        return new ResponseVO(messages);
    }

    /**
     *主播获取观众进入退出直播间的消息
     * @param
     * @return
     */
    @RequestMapping(value = "/audienceMessage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseVO audienceMessage(){
        long id = (long) request.getAttribute("userId");
        List<Long> friendIds=userRepository.findById(id).getFriends();
        List<String>list=new ArrayList<>();
        List<Message>  entermgs= InterfaceMq.receiveMessage(InterfaceMq.audineceJoin);
        List<Message>  leavemgs= InterfaceMq.receiveMessage(InterfaceMq.audienceLeave);
        if(friendIds!=null){
            for(Long friendId:friendIds){
               String userName= userRepository.findById(friendId).getUsername();
                if(entermgs!=null){
                    for(Message entermg:entermgs){
                        String str=entermg.msgBody;
                        String userName1= str.split(MessageType.EnterLive)[0];
                        if(userName.equals(userName1)){
                            list.add(str);
                            InterfaceMq.deleteUserMsg(userName1,InterfaceMq.audineceJoin);
                        }

                    }
                }
                if(leavemgs!=null){
                    for(Message leaveMsg:leavemgs){
                        String str=leaveMsg.msgBody;
                        String userName1= str.split(MessageType.outLive)[0];
                        if(userName.equals(userName1)){
                            list.add(str);
                            InterfaceMq.deleteUserMsg(userName1,InterfaceMq.audienceLeave);
                        }
                    }
                }

        }

        }
       return new ResponseVO(list);


    }

}
