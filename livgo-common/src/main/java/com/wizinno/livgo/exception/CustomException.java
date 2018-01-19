package com.wizinno.livgo.exception;

/**
 * Created by HP on 2016/6/24.
 */
public class CustomException extends RuntimeException{

    private Integer code;
    private String reason;

    public CustomException(Integer code){
        super(code.toString(), null);
        this.code = code;
    }

    public CustomException(Integer code, String reason) {
        super(code.toString());
        this.code = code;
        this.reason = reason;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
