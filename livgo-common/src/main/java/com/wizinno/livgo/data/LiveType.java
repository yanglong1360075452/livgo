package com.wizinno.livgo.data;

/**
 * Created by Administrator on 2017/5/22.
 */
public enum LiveType {
    publicLive(0,"公开直播"),
    privateLive(1,"私人直播");
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    LiveType(Integer _nCode, String _name) {
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
    public static LiveType valueOf(Integer code) {
        for (LiveType liveType : LiveType.values()){
            if (liveType.toCode().equals(code)){
                return liveType;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (LiveType liveType : LiveType.values()){
            if (liveType.toCode().equals(code)){
                return liveType.toString();
            }
        }
        return null;
    }


}
