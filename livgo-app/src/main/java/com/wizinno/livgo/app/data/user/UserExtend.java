package com.wizinno.livgo.app.data.user;

import com.wizinno.livgo.app.document.Device;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-12.
 */
public class UserExtend implements Serializable {

    private Long id;

    private String phone;

    private String username;

    private String img;

    private String bgImg;

    private String nickname;

    private Integer sex;
    private String sexDesc;

    private String qrCode;

    private Integer status;
    private String statusDesc;

    private Date registerTime;

    private Boolean isActive;

    private List<UserBrief> friends;

    private Date editTime;

    private List<Device> devices;

    private String token;

    private Date beInviteTime;

    private String userSig;

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public Date getBeInviteTime() {
        return beInviteTime;
    }

    public void setBeInviteTime(Date beInviteTime) {
        this.beInviteTime = beInviteTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSexDesc() {
        return sexDesc;
    }

    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<UserBrief> getFriends() {
        return friends;
    }

    public void setFriends(List<UserBrief> friends) {
        this.friends = friends;
    }
}
