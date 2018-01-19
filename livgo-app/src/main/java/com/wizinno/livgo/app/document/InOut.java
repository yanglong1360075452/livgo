package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/17.
 */
@Document(collection = "inout")
public class InOut {
    @GeneratedValue
    @Id
    private long id;

    private long liveId;//直播间id

    private  long userId;//用户id


    private Date inTime;//进入直播时间

    private Date outTime;//离开直播时间

    private Integer count;//直播间人数


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
