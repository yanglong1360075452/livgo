package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "livInvite")
public class LiveInvite {
    @GeneratedValue
    @Id
    private long id;

    private String  liveImg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLiveImg() {
        return liveImg;
    }

    public void setLiveImg(String liveImg) {
        this.liveImg = liveImg;
    }

    public String getLiveUserName() {
        return liveUserName;
    }

    public void setLiveUserName(String liveUserName) {
        this.liveUserName = liveUserName;
    }

    public String getIsInviteName() {
        return isInviteName;
    }

    public void setIsInviteName(String isInviteName) {
        this.isInviteName = isInviteName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private String liveUserName;

    private String isInviteName;

    private String content="邀请你参见直播";

    private Date createTime;

}
