package com.wizinno.livgo.app.data.live;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */
public class LiveSimple {
    private Long id;
    private String liveName;
    private String deviceNumber;
    private Date startTime;
    private Date endTime;
    private String location;//直播地理位置
    private Integer type;//直播类型
    private String typeDesc;
    private Integer count;//观众数量
    private List<String > audienceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getAudienceName() {
        return audienceName;
    }

    public void setAudienceName(List<String> audienceName) {
        this.audienceName = audienceName;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
