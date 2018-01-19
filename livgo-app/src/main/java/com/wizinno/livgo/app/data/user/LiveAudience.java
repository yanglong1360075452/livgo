package com.wizinno.livgo.app.data.user;

/**
 * Created by Administrator on 2017/5/23.
 */
public class LiveAudience {
    private Long id;
    private String nickname;
    private String username;
    private String img;
    private boolean watchLive;//true 私人直播间公众正在看直播 null 公开直播

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isWatchLive() {
        return watchLive;
    }

    public void setWatchLive(boolean watchLive) {
        this.watchLive = watchLive;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
