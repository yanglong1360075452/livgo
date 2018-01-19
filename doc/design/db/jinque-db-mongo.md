
金雀LiveGO数据库设计-MongoDB版
=============

[TOC]

## 1 用户
	用户表id（主键）
	用户手机号
	用户名
	密码
	头像(Base64编码)
	背景墙(Base64编码)
	昵称
	性别
	二维码
	状态
	注册时间
	是否为活跃用户
	好友ID数组
	编辑时间
	设备
		设备ID
		设备名称

## 2 设备
	设备id(主键）
	设备名称
	设备编号
	设备是否绑定
	绑定用户
		用户ID
		用户昵称
	设备配置信息

## 3 直播
	直播间ID
	主播
		主播ID
		主播昵称
	直播开始时间
	直播结束时间
	公开/私人直播
	直播地理位置
	直播缩略图
	直播观众(公开直播-为当前正在直播间的观众)
		用户ID
		用户昵称
	受邀观众(私人直播)
		用户ID
		用户昵称

## 4 消息
	消息id
	消息来源
	消息去处
	消息内容
	消息状态
	消息是否被阅读

## 5 日志
操作类型
 ### 登录
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    备注
 ### 登出
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    备注
 ### 注册
    用户ID
    手机号码
    用户IP
    用户设备
    APP版本
    操作时间
    备注
 ### 注销
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    备注
 ### 邀请好友注册
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    好友手机号码
    备注
 ### 添加好友
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    好友ID
    备注
 ### 接受添加好友请求
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    好友ID
    备注
 ### 拒绝添加好友请求
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    好友ID
    备注
 ### 删除好友
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    好友ID
    备注
 ### 开始直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    备注
 ### 邀请好友参加直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    好友ID
    备注
 ### 把好友踢出直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    好友ID
    备注
 ### 结束直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    备注
 ### 进入直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    备注
 ### 退出直播
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    直播ID
    备注
 ### 绑定设备
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    设备ID
    备注
 ### 解绑设备
    用户ID
    用户IP
    用户设备
    APP版本
    操作时间
    设备ID
    备注
