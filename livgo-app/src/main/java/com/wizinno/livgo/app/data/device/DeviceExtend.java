package com.wizinno.livgo.app.data.device;

import com.wizinno.livgo.app.document.User;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/17.
 */
public class DeviceExtend {
    private Long id;

    private String deviceName;

    private String deviceId;

    private Boolean isBind;  //是否绑定

    private String userName; //绑定用户

    private String deviceModule; //设备型号

    private boolean isActive;//设备是否激活

    private Date activeTime;//激活时间

    private Date bindUserTime;//绑定用户时间

    private Date relieveBindTime;//解除绑定时间

    private String deviceMemo;//设备备注

    private Integer status;//设备状态
    private String statusDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Boolean getBind() {
        return isBind;
    }

    public void setBind(Boolean bind) {
        isBind = bind;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getBindUserTime() {
        return bindUserTime;
    }

    public void setBindUserTime(Date bindUserTime) {
        this.bindUserTime = bindUserTime;
    }

    public Date getRelieveBindTime() {
        return relieveBindTime;
    }

    public void setRelieveBindTime(Date relieveBindTime) {
        this.relieveBindTime = relieveBindTime;
    }

    public String getDeviceMemo() {
        return deviceMemo;
    }

    public void setDeviceMemo(String deviceMemo) {
        this.deviceMemo = deviceMemo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
