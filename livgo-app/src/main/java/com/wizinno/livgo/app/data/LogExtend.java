package com.wizinno.livgo.app.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2017/5/17.
 */
public class LogExtend implements Serializable {

    private long id;

    private Integer operation;
    private String operationDesc;

    private String userIp;

    private Long deviceId;

    private String appVersion;

    private String phone;

    private Long friendId;

    private Long liveId;

    private Date createTime;

    private Long createBy;
    private String createByDesc;

    private String note;

    private String requestMessage;

    private int responseStatue;

    private String responseMessage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateByDesc() {
        return createByDesc;
    }

    public void setCreateByDesc(String createByDesc) {
        this.createByDesc = createByDesc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public int getResponseStatue() {
        return responseStatue;
    }

    public void setResponseStatue(int responseStatue) {
        this.responseStatue = responseStatue;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
