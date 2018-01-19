package com.wizinno.livgo.manage.controller;

import com.wizinno.livgo.app.data.LogExtend;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.LogTransUtil;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.LogType;
import com.wizinno.livgo.data.PageData;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017/5/17.
 */
@RestController
@RequestMapping("/api/manage/logs")
public class LogsController extends BaseController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/access",method = RequestMethod.GET)
    public ResponseVO getAccessLogs(@RequestParam(value = "page",defaultValue = "0") int page,
                                    @RequestParam(value = "length",defaultValue = "10") int length,
                                    @RequestParam(value = "username",required = false) String username,
                                    @RequestParam(value = "ipAddress",required = false) String ipAddress,
                                    @RequestParam(value = "createTime",required = false) Long createTime,
                                    @RequestParam(value = "endTime",required = false) Long endTime ){

        Query query = new Query();

        if (!Util.stringNull(username)){
            User user = userRepository.findByUsername(username);
            if (user == null ){
                throw new CustomException(CustomExceptionCode.UserNotExists);
            }
            query.addCriteria(Criteria.where("createBy").is(user.getId()));
        }if(!Util.stringNull(ipAddress)){
            query.addCriteria(Criteria.where("userIp").is(ipAddress));
        }

        if(createTime!=null &&endTime !=null){
            if(createTime>0 &&endTime>0){
                query.addCriteria(Criteria.where("createTime").gt(new Date(createTime)).lt(new Date(endTime)));
            }

        }


        query.addCriteria(Criteria.where("type").is(LogType.accessLog.toCode()))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC ,"createTime")));
        Pageable pageable = new PageRequest(page,length);
        Page<Log> logs = logRepository.find(query, pageable);
        List<LogExtend> logExtends = LogTransUtil.UsersToExtends(logs.getContent());
        return new ResponseVO(new PageData<>(page,length,logExtends,logs.getTotalPages(),logs.getTotalElements()));

    }


    @RequestMapping(value = "/note",method = RequestMethod.GET)
    public ResponseVO getNoteLogs(@RequestParam(value = "page",defaultValue = "0") int page,
                              @RequestParam(value = "length",defaultValue = "10") int length,
                              @RequestParam(value = "filter",required = false) String filter){

        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(LogType.noteLog.toCode()));

        if (filter != null){
            query.addCriteria(Criteria.where("phone").regex(filter));
        }

        Pageable pageable = new PageRequest(page,length);
        Page<Log> logs = logRepository.find(query, pageable);
        List<LogExtend> logExtends = LogTransUtil.UsersToExtends(logs.getContent());
        List<LogExtend> lists = new ArrayList<>();


        return new ResponseVO(new PageData<>(page,length,logExtends,logs.getTotalPages(),logs.getTotalElements()));

    }

    @RequestMapping(value = "/system",method = RequestMethod.GET)
    public ResponseVO getSystemLogs(@RequestParam(value = "page",defaultValue = "0") int page,
                                  @RequestParam(value = "length",defaultValue = "10") int length,
                                  @RequestParam(value = "filter",required = false) String filter){

        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(LogType.systemLog.toCode()));

        if (filter != null){
            query.addCriteria(Criteria.where("phone").regex(filter));
        }

        Pageable pageable = new PageRequest(page,length);
        Page<Log> logs = logRepository.find(query, pageable);
        List<LogExtend> logExtends = LogTransUtil.UsersToExtends(logs.getContent());
        List<LogExtend> lists = new ArrayList<>();

        return new ResponseVO(new PageData<>(page,length,logExtends,logs.getTotalPages(),logs.getTotalElements()));

    }
}
