package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by LiuMei on 2017-05-04.
 */
@Document(collection = "device")
public class Device {

    @GeneratedValue
    @Id
    private long id;

    private String deviceName;

    private String deviceId;//设备序列号

    private Boolean isBind;  //是否绑定

    private User user; //绑定用户

    private String deviceModule; //设备型号

    private String deviceMemo;//设备备注

    private Date bindTime;// 设备绑定时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Boolean getBind() {
        return isBind;
    }

    public void setBind(Boolean bind) {
        isBind = bind;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModule() {
        return deviceModule;
    }

    public void setDeviceModule(String deviceModule) {
        this.deviceModule = deviceModule;
    }

    public String getDeviceMemo() {
        return deviceMemo;
    }

    public void setDeviceMemo(String deviceMemo) {
        this.deviceMemo = deviceMemo;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }
}

