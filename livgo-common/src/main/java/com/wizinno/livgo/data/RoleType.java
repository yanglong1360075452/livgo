package com.wizinno.livgo.data;

public enum RoleType {
    live(1,"主播"),
    audience(2,"观众");
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    RoleType(Integer _nCode, String _name) {
        this.nCode = _nCode;
        this.name = _name;
    }

    public Integer toCode(){
        return this.nCode;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static RoleType valueOf(Integer code) {
        for (RoleType roleType : RoleType.values()){
            if (roleType.toCode().equals(code)){
                return roleType;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (RoleType roleType : RoleType.values()){
            if (roleType.toCode().equals(code)){
                return roleType.toString();
            }
        }
        return null;
    }
}
