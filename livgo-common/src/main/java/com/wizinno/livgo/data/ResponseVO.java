package com.wizinno.livgo.data;

import java.io.Serializable;

/**
 * 返回数据基类
 */
public class ResponseVO implements Serializable{
    private Integer code;

    private Object data;

    public ResponseVO(){
        this.code = 0;
    }

    public ResponseVO(Integer code){
        this.code = code;
    }

    public ResponseVO(Object data){
        this.code = 0;
        this.data = data;
    }

    public ResponseVO(Integer code, Object data){
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
