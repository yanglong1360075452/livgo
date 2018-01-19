package com.wizinno.livgo.data;

/**
 * Created by HP on 2017/5/9.
 */
public enum UserStatus {
    active(1, "活动"),
    forbidden(2, "禁用"),
    logOff(3, "注销"),;
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    UserStatus(Integer _nCode, String _name) {
        this.nCode = _nCode;
        this.name = _name;
    }

    public Integer toCode() {
        return this.nCode;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static UserStatus valueOf(Integer code) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.toCode().equals(code)) {
                return userStatus;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.toCode().equals(code)) {
                return userStatus.toString();
            }
        }
        return null;
    }
}
