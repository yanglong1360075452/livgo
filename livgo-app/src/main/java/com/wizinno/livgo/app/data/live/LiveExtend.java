package com.wizinno.livgo.app.data.live;

import java.util.Date;

/**
 * Created by LiuMei on 2017-05-22.
 */
public class LiveExtend {
    private Long liveId;//直播id
    private Long userId;//主播id
    private String name;//主播姓名
    private Integer audiences;//观众人数
    private Integer fCounts;//好友数量
    private Date startTime;//直播开始时间
    private Date endTime;//直播结束时间
    private Integer audienceTime;//观众人次
    private long avgTime;//观众驻留平均时长
    private Integer type;//直播类型
    private String typeDesc;
    private String location;//直播地理坐标
    private String livePicture;//直播缩略图

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAudiences() {
        return audiences;
    }

    public void setAudiences(Integer audiences) {
        this.audiences = audiences;
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

    public Integer getAudienceTime() {
        return audienceTime;
    }

    public void setAudienceTime(Integer audienceTime) {
        this.audienceTime = audienceTime;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(long avgTime) {
        this.avgTime = avgTime;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLivePicture() {
        return livePicture;
    }

    public void setLivePicture(String livePicture) {
        this.livePicture = livePicture;
    }

    public Integer getfCounts() {
        return fCounts;
    }

    public void setfCounts(Integer fCounts) {
        this.fCounts = fCounts;
    }
}
