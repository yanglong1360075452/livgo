package com.wizinno.livgo.app.controller;

import com.livgo.app.mq.interfaceMq.InterfaceMq;
import com.livgo.app.mq.service.Message;
import com.livgo.app.mq.util.MessageType;
import com.livgo.app.mq.util.PushMessage;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.data.LogOperation;
import com.wizinno.livgo.data.LogType;
import com.wizinno.livgo.data.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/19.
 */
@RestController
@RequestMapping("/api/app")
public class Livgo {
    /**
     *推流状态更新通知
     * @param deviceId 设备ID，蓝牙耳机设备ID
     * @param state 设备状态代码，见下表
     * @return
     */
    private static Logger log = LoggerFactory.getLogger(Livgo.class);
    @Autowired
    private LiveRepository liveRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @RequestMapping(value = "/notifyDevState", method = RequestMethod.GET)
    public ResponseVO notifyDevState(@RequestParam(value = "deviceNumber",required = true)String deviceNumber,
                                     @RequestParam(value = "state",required = true)Integer state){

        log.debug("### notifyDevState(deviceNumber="+deviceNumber +", state="+state+")");
        Collection<String> usernames = new ArrayList<>();
        Query query =new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceNumber));
        Device device=deviceRepository.findByQuery(query);
        Query query1 =new Query();
        query1.addCriteria(Criteria.where("deviceNumber").is(deviceNumber));
        query1.addCriteria(Criteria.where("endTime").ne(null));
        Live live= liveRepository.findByQuery(query1);
        if(live!=null){
           String userName= live.getUser().getUsername();
           usernames.add(userName);
           PushMessage.sendPushMessage(usernames, userName + "开始直播");
        }

        return new ResponseVO(0);

    }
}
