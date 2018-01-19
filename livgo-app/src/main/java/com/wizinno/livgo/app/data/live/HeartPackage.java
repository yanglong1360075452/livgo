package com.wizinno.livgo.app.data.live;

import java.util.Date;

public class HeartPackage {
    private Long liveId;

    private String packages;

    private Date acceptTime;

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }
}
