package com.wizinno.livgo.app.document;
import com.wizinno.livgo.app.data.user.LiveAudience;
import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
@Document(collection = "live")
public class Live implements Serializable {
    @GeneratedValue
    @Id
    private long id;
    private LiveAudience user;
    private String deviceNumber;
    private Date startTime;
    private Date endTime;
    private Integer type;//直播类型
    private String typeDesc;
    private Integer liveStatue;//0为开始直播  1.已经开始直播
    private String pushUrl;//推流Url
    private HashMap playUrl;//播放url地址
    private String location;//直播地理位置
    private String livePicture;//直播缩略图
    private Integer count=0;//累计观看人数
    private Integer maxCoun=0;//同时最高人数
    private List<LiveAudience> audience;//直播的观众
    private Integer historyCount=0;//直播的次数
    private Date packageTime;

    public HashMap getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(HashMap playUrl) {
        this.playUrl = playUrl;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LiveAudience getUser() {
        return user;
    }

    public void setUser(LiveAudience user) {
        this.user = user;
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

    public List<LiveAudience> getAudience() {
        return audience;
    }

    public void setAudience(List<LiveAudience> audience) {
        this.audience = audience;
    }


    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMaxCoun() {
        return maxCoun;
    }

    public void setMaxCoun(Integer maxCoun) {
        this.maxCoun = maxCoun;
    }

    public Integer getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(Integer historyCount) {
        this.historyCount = historyCount;
    }

    public Integer getLiveStatue() {
        return liveStatue;
    }

    public void setLiveStatue(Integer liveStatue) {
        this.liveStatue = liveStatue;
    }

    public Date getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(Date packageTime) {
        this.packageTime = packageTime;
    }
}
