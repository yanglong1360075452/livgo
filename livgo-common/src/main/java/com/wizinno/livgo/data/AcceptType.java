package com.wizinno.livgo.data;

public enum AcceptType {
    nodo(1,"未操作"),
    forbidden(2,"拒绝"),
    agree(3,"同意");

    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    AcceptType(Integer _nCode, String _name) {
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
    public static AcceptType valueOf(Integer code) {
        for (AcceptType acceptType : AcceptType.values()) {
            if (acceptType.toCode().equals(code)) {
                return acceptType;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (AcceptType acceptType : AcceptType.values()) {
            if (acceptType.toCode().equals(code)) {
                return acceptType.toString();
            }
        }
        return null;
    }
}
