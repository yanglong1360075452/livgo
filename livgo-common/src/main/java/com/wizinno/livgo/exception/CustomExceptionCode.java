package com.wizinno.livgo.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2016/6/24.
 */
public class CustomExceptionCode {
    private final static Map<Integer, String> errorMap = new HashMap<>();
    public static final Integer AccessDenied = 1;
    public static final Integer AuthFailed = 2;
    public static final Integer UsernameOrPasswordError = 3;
    public static final Integer ErrorParam = 4;
    public static final Integer UserExists = 5;
    public static final Integer UserNotExists = 6;
    public static final Integer WrongPassword = 7;
    public static final Integer CaptchaError = 8;
    public static final Integer CaptchaSendFail = 9;
    public static final Integer StatusError = 10;
    public static final Integer defferentPassword = 11;
    public static final Integer liveFriendsNotExits = 12;
    public static final Integer liveIdNotExits = 13;
    public static final Integer liveEnd = 14;
    public static final Integer liveExits=15;
    public static final Integer HanZi=16;
    public static final  Integer phoneExit=17;
    public static final  Integer roomFull=18;
    public static final  Integer noAutho=19;
    public static final  Integer liveNoExits=20;


    static {
        errorMap.put(1, "权限不足");
        errorMap.put(2, "认证失败");
        errorMap.put(3, "用户名或密码错误");
        errorMap.put(4, "参数错误");
        errorMap.put(5, "用户已存在");
        errorMap.put(6, "用户不存在");
        errorMap.put(7, "密码错误");
        errorMap.put(8, "验证码验证失败");
        errorMap.put(9, "验证码发送失败");
        errorMap.put(10, "状态有误");
        errorMap.put(11, "输入密码与所设置密码不一致");
        errorMap.put(12, "没有好友在直播");
        errorMap.put(13, "该设备未授权");
        errorMap.put(14, "该直播结束");
        errorMap.put(15, "您正在直播中不能再创建直播间");
        errorMap.put(16, "请不要输入汉字");
        errorMap.put(17, "该手机号已经注册");
        errorMap.put(18, "该房间已经满员");
        errorMap.put(19, "您没有权限进入该房间");
        errorMap.put(20, "直播间不存在");
    }

    public static String getReasonByCode(Integer code, String defaultReason){
        if(errorMap.containsKey(code)){
            return errorMap.get(code);
        }else{
            return defaultReason;
        }
    }
}
