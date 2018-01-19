package com.wizinno.livgo.app.controller;

import com.wizinno.livgo.app.document.Constant;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.data.LogOperation;
import com.wizinno.livgo.data.LogType;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2017/6/16.
 */
@RestController
@RequestMapping("/api/app")
public class LivePlayUrlController {

    private static Logger log = LoggerFactory.getLogger(LivePlayUrlController.class);
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LiveRepository liveRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeviceRepository deviceRepository;
    String bizid= Constant.BIZID;
    String securityKey=Constant.HONGJING_PUSH_ANTI_THEFT_CHAIN_KEY;

//    public static void main(String[] args) {
//        LivePlayUrlController livePlayUrlController = new LivePlayUrlController();
//        System.out.println(livePlayUrlController.getLivePlayUrl(1L));
//    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};



    @RequestMapping(value = "/getLivePlayUrl",method = RequestMethod.POST)
    private ResponseVO getLivePlayUrl(@RequestParam(value = "roomId",required = true)Long roomId){


        Live live= liveRepository.findById(roomId);
        if (live != null){
            if (live.getEndTime() == null){
                return new ResponseVO(live.getPlayUrl());
            }else {
                throw new CustomException(CustomExceptionCode.liveEnd);
            }
        }else {
            throw new CustomException(CustomExceptionCode.liveIdNotExits);
        }
    }


}
