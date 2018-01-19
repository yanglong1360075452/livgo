package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-03.
 */
@Document(collection = "user")
public class User  implements Serializable {

    private static final long serialVersionUID = 179822189115264434L;

    @GeneratedValue
    @Id
    private long id;

    private String phone;

    private String username;

    private String password;

    private String img;

    private String bgImg;

    private String nickname;

    private Integer sex;

    private String qrCode;

    private Integer status;

    private Date registerTime;

    private Boolean isActive;

    private List<Long> friends;

    private Date editTime;

    private List<Device> devices;

    private Integer liveCounts; //直播次数

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
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

    public Integer getLiveCounts() {
        return liveCounts;
    }

    public void setLiveCounts(Integer liveCounts) {
        this.liveCounts = liveCounts;
    }
}
