package com.wizinno.livgo.app.utils;

import com.wizinno.livgo.app.data.device.DeviceExtend;
import com.wizinno.livgo.app.data.device.DeviceSimple;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.data.LogOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
@Component
public class DeviceTransUtil {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;
    private static DeviceTransUtil deviceTransUtil;
    public void setlogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    @PostConstruct
    public void init(){
        deviceTransUtil=this;
        deviceTransUtil.logRepository=this.logRepository;
    }


    public static DeviceExtend deviceToExtend(Device device) {
        if (device != null) {
            DeviceExtend deviceExtend = new DeviceExtend();
            BeanUtils.copyProperties(device, deviceExtend);
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("deviceId").is(device.getDeviceId()));
            query1.addCriteria(Criteria.where("operation").is(LogOperation.bindDevice.toCode()));
            Log log1 = deviceTransUtil.logRepository.findByQuery(query1);//绑定设备的日志
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("deviceId").is(device.getDeviceId()));
            query2.addCriteria(Criteria.where("operation").is(LogOperation.unbindDevice.toCode()));
            Log log2 = deviceTransUtil.logRepository.findByQuery(query2);//解除绑定设备的日志
            if (null != log1) {
                Date bindDate=log1.getCreateTime();//绑定时间
                deviceExtend.setBindUserTime(bindDate);
            }else{
                deviceExtend.setBindUserTime(null);
            }
            if(null != log2){
                Date unBindDate=log2.getCreateTime();
                deviceExtend.setRelieveBindTime(unBindDate);//解绑时间
            }else{
                deviceExtend.setRelieveBindTime(null);//解绑时间
            }
            if(device.getUser()!=null){
                User user=deviceTransUtil.userRepository.findById(device.getUser().getId());
                String username=user.getUsername();
                deviceExtend.setUserName(username);//绑定设备的用户名
            }else{
                deviceExtend.setUserName(null);
            }

            return deviceExtend;
        }else {
            return null;
        }

    }
    public static DeviceSimple deviceToSimple(Device device){
        if(device!=null){
            DeviceSimple deviceSimple=new DeviceSimple();
            BeanUtils.copyProperties(device, deviceSimple);
            if(device.getUser()!=null){
                User user= deviceTransUtil.userRepository.findById(device.getUser().getId());
                deviceSimple.setUserName(user.getUsername());
            }else{
                deviceSimple.setUserName(null);
            }

            return deviceSimple;
        }else{
            return  null;
    }

    }
    public static List<DeviceSimple> devicesToDeviceSimples(List<Device> devices){
        if(devices!=null&&devices.size()>0){
            List<DeviceSimple> liveSimples=new ArrayList<>();
            for(Device device:devices){
                liveSimples.add(deviceToSimple(device));
            }
            return liveSimples;
        }
        return null;
    }
}
