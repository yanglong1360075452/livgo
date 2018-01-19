package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by LiuMei on 2017-05-09.
 */
@Document(collection = "log")
public class Log {

    private static final long serialVersionUID = 179822189115264434L;

    @GeneratedValue
    @Id
    private long id;

    private Integer operation;

    private String userIp;

    private String deviceId;

    private String appVersion;

    private String phone;

    private Long friendId;

    private Long liveId;

    private Date createTime;

    private Long createBy;

    private Integer type;

    private String note;

    private String requestMessage;

    private int responseStatue;

    private  String responseMessage;

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

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
