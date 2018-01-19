package com.wizinno.livgo.app.document;

import com.wizinno.livgo.app.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addRequest")
public class AddRequest {
    @GeneratedValue
    @Id
    private long id;
    private Long req_id;//请求者的id

    private String img;//请求者的头像

    private String requestName;//加好友的请求者

    private Long acc_id;//接受者的id

    private String acceptName;//加好友的同意者

    private Integer statue;//被加好友是否同意

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Long getReq_id() {
        return req_id;
    }

    public void setReq_id(Long req_id) {
        this.req_id = req_id;
    }

    public Long getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(Long acc_id) {
        this.acc_id = acc_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
