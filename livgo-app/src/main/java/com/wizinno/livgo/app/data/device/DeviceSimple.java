package com.wizinno.livgo.app.data.device;

import com.wizinno.livgo.app.document.User;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */
public class DeviceSimple {
    private long id;

    private String deviceName;

    private String deviceId;

    private String userName; //绑定用户

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
