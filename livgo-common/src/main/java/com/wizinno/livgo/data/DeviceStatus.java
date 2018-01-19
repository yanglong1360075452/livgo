package com.wizinno.livgo.data;

/**
 * Created by Administrator on 2017/5/19.
 */
public enum DeviceStatus {
    active(1,"激活"),
    forbidden(2,"禁用"),
    broken(3,"损坏");

    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    DeviceStatus(Integer _nCode, String _name) {
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
    public static DeviceStatus valueOf(Integer code) {
        for (DeviceStatus deviceStatus : DeviceStatus.values()) {
            if (deviceStatus.toCode().equals(code)) {
                return deviceStatus;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (DeviceStatus deviceStatus : DeviceStatus.values()) {
            if (deviceStatus.toCode().equals(code)) {
                return deviceStatus.toString();
            }
        }
        return null;
    }
}
