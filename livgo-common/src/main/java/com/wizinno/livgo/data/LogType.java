package com.wizinno.livgo.data;

/**
 * Created by HP on 2017/5/18.
 */
public enum  LogType {
    accessLog(1,"访问日志"),
    noteLog(2,"短信日志"),
    systemLog(3,"系统消息日志"),

    ;
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    LogType(Integer _nCode, String _name) {
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

    public static LogType valueOf(Integer code) {
        for (LogType logType : LogType.values()){
            if (logType.toCode().equals(code)){
                return logType;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (LogType logType : LogType.values()){
            if (logType.toCode().equals(code)){
                return logType.toString();
            }
        }
        return null;
    }
}
