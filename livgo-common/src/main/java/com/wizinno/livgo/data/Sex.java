package com.wizinno.livgo.data;

/**
 * Created by LiuMei on 2017-05-12.
 */
public enum  Sex {

    male(0, "男"),
    female(1, "女"),
    unknown(2, "未知");
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    Sex(Integer _nCode, String _name) {
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

    public static Sex valueOf(Integer code) {
        for (Sex sex : Sex.values()) {
            if (sex.toCode().equals(code)) {
                return sex;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (Sex sex : Sex.values()) {
            if (sex.toCode().equals(code)) {
                return sex.toString();
            }
        }
        return null;
    }
}
