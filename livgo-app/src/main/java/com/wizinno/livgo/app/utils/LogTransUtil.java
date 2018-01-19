package com.wizinno.livgo.app.utils;


import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.data.LogExtend;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.data.LogOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017/5/17.
 */

@Component
public class LogTransUtil {

    @Autowired
    private LogRepository logRepository;  //添加所需注入的私有成员

    @Autowired
    private  UserRepository userRepository;

    private static LogTransUtil logTransUtil;  //  关键点1   静态初使化 一个工具类  这样是为了在spring初使化之前

    public void setUserRepository(LogRepository logRepository,UserRepository userRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct     //关键二   通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    public void init() {
        logTransUtil = this;
        logTransUtil.logRepository = this.logRepository;
        logTransUtil.userRepository = this.userRepository;
    }

    public static LogExtend UserToExtend(Log log) {
        if (log != null) {
            LogExtend logExtend = new LogExtend();
            BeanUtils.copyProperties(log, logExtend);
            logExtend.setOperationDesc(LogOperation.getNameByCode(log.getOperation()));
            if(log.getCreateBy()==null){
                logExtend.setCreateBy(null);
                logExtend.setCreateByDesc(null);
            }else{
                User user = logTransUtil.userRepository.findById(log.getCreateBy());
                if (user != null){
                    logExtend.setCreateByDesc(user.getUsername());
                }
            }

            return logExtend;
        } else {
            return null;
        }
    }

    public static List<LogExtend> UsersToExtends(List<Log> logs) {
        if (logs != null && logs.size() > 0) {
            List<LogExtend> logExtend = new ArrayList<>();
            for (Log log : logs) {
                logExtend.add(UserToExtend(log));
            }
            return logExtend;
        } else {
            return null;
        }
    }
}
