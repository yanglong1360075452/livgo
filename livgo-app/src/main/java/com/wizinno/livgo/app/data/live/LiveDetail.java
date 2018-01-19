package com.wizinno.livgo.app.data.live;

import com.wizinno.livgo.app.document.LiveMsg;

import java.util.Date;
import java.util.List;

public class LiveDetail {
    private Date startTime;//直播开始时间

    private Date endTime;//直播结束时间

    private Integer count=0;//直播间的观看人次

    private String img;

    private String  userName;

    private List<LiveMsg> liveMsg;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<LiveMsg> getLiveMsg() {
        return liveMsg;
    }

    public void setLiveMsg(List<LiveMsg> liveMsg) {
        this.liveMsg = liveMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
