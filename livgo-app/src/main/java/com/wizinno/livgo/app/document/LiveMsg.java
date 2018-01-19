package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "livMsg")
public class LiveMsg {
    @GeneratedValue
    @Id
    private long id;

    private Long liveId;

    private String sendUsrname;

    private String picture;//发送者头像

    private String context;

    private Date  creatTime;

    private Integer role;//1:主播  2.观众


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public String getSendUsrname() {
        return sendUsrname;
    }

    public void setSendUsrname(String sendUsrname) {
        this.sendUsrname = sendUsrname;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
