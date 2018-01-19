package com.wizinno.livgo.data;

/**
 * Created by LiuMei on 2017-05-09.
 */
public enum LogOperation {
    login(1,"登录"),
    logOut(2,"登出"),
    register(3,"注册"),
    logOff(4,"注销"),
    inviteRegister(5,"邀请好友注册"),
    addFriend(6,"添加好友"),
    acceptAddFriend(7,"接受添加好友请求"),
    rejectAddFriend(8,"拒绝添加好友请求"),
    startLive(9,"创建直播间"),
    inviteLive(10,"邀请好友参加直播"),
    kickLive(11,"把好友踢出直播"),
    endLive(12,"结束直播"),
    enterLive(13,"进入直播"),
    outLive(14,"退出直播"),
    bindDevice(15,"绑定设备"),
    unbindDevice(16,"解绑设备"),
    deleteFriends(17,"删除好友"),
    sendCaptcha(18,"发送验证码"),
    editorSex(19,"编辑性别"),
    editorImg(21,"编辑头像"),
    editorBgImg(22,"编辑背景墙"),
    editorNickname(23,"编辑昵称"),
    editorQrcode(24,"编辑二维码"),
    setPassword(25,"修改密码"),
    cleanAccount(26,"注销账号"),
    queryUser(27,"查询用户"),
    queryInvite(28,"查询谁邀请我为好友"),
    queryFriends(29,"查询好友"),
    queryLive(30,"查询正在直播好友"),
    queyLiveFriends(31,"查询直播间观众"),
    getPlayUrl(32,"手机端获取播放流Url"),
    getPushUrl(33,"开始直播"),
    livgoStatue(34,"耳机端推流状态"),
    phoneStatue(35,"手机端推流状态"),
    phoneIsResgister(35,"查询手机号是否被注册"),
    forgetPassword(36,"忘记密码"),
    whoInviteMe(37,"都有谁邀请我参见私人直播"),
    alterPhone(38,"修改手机号码"),
    liveMsg(39,"查看主播消息"),
    audMsg(40,"查看观众消息"),
    myHistoryLives(41,"我的历史直播"),
    sendMessage(42,"直播间发送消息"),
    getPicture(43,"获取缩略图"),
    liveDetail(44,"直播详情"),
    friendHistoryLive(45,"好友的历史直播"),
    historyLiveMsg(46,"获取历史直播的信息"),
    recoverLive(47,"直播间重新连接"),
    exceptionCloseLive(48,"异常关闭直播间"),



    ;
    // 定义私有变量
    private Integer nCode;

    private String name;

    // 构造函数，枚举类型只能为私有
    LogOperation(Integer _nCode, String _name) {
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

    public static LogOperation valueOf(Integer code) {
        for (LogOperation logOperation : LogOperation.values()){
            if (logOperation.toCode().equals(code)){
                return logOperation;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        for (LogOperation logOperation : LogOperation.values()){
            if (logOperation.toCode().equals(code)){
                return logOperation.toString();
            }
        }
        return null;
    }
}
