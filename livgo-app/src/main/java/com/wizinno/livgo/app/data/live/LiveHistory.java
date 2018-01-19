package com.wizinno.livgo.app.data.live;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/10.
 * 用户观看历史
 */
public class LiveHistory {
    private long liveId;//直播间id

    private long userId;//观众id

    private String username;//用户名

    private Integer type;//直播类型
    private String typeDesc;

    private Date inviteTime;//观众受邀请时间

    private Date inTime;//进入直播时间

    private Date outTime;//离开直播时间

    private long stayTime;//观众驻留时间



    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Date getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Date inviteTime) {
        this.inviteTime = inviteTime;
    }

    public long getStayTime() {
        return stayTime;
    }

    public void setStayTime(long stayTime) {
        this.stayTime = stayTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
