/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50157
Source Host           : localhost:3306
Source Database       : goldspink

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2017-04-19 11:10:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL COMMENT '主键',
  `phone_number` varchar(255) NOT NULL COMMENT '手机号码',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名，非注册用户未空',
  `password` varchar(255) DEFAULT NULL COMMENT '密码 非注册用户未空',
  `head_img` varchar(255) DEFAULT NULL COMMENT '头像',
   `background` varchar(255) DEFAULT NULL COMMENT '背景墙',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `sex` enum('0','1','2') DEFAULT '2' COMMENT '0 男 1女 2未知 默认未知',
  `qr_code` varchar(255) DEFAULT NULL COMMENT '二维码',
  `invite_time` datetime DEFAULT NULL COMMENT '被邀请的未注册用户受邀时间',
  `status` enum('0','1') DEFAULT '0' COMMENT '0 能正常使用 1 禁用',
  `register_time` datetime DEFAULT NULL COMMENT '用户注册时间',
  `is_active` enum('0','1') DEFAULT NULL COMMENT '0 非活跃 1活跃',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_number` (`phone_number`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `qr_code` (`qr_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
DROP TABLE IF EXISTS `user_friends`;
CREATE TABLE `user_friends` (
  `user_id` varchar(255) NOT NULL COMMENT '用户id 外键',
  `friend_id` varchar(255) NOT NULL COMMENT '好友id 外键',
  `status` enum('0','1') DEFAULT NULL COMMENT '0:被邀请，1：同意',
  `invite_time` datetime DEFAULT NULL COMMENT '被邀请时间',
  `agree_time` datetime DEFAULT NULL COMMENT '未同意为空（定时清理）',
  KEY `user_id` (`user_id`),
  KEY `friend_id` (`friend_id`),
  CONSTRAINT `user_friends_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_friends_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_friends
-- ----------------------------
DROP TABLE IF EXISTS `audience`;
CREATE TABLE `audience` (
  `live_id` varchar(255) NOT NULL COMMENT '直播间id 外键',
  `user_id` varchar(255) NOT NULL COMMENT '观众id',
  `invite_time` datetime DEFAULT NULL COMMENT '私人直播的受邀观众，公开直播为null',
  `join_time` datetime  DEFAULT NULL COMMENT '私人直播的受邀观众未进入时为null',
  `leave_time` datetime DEFAULT NULL COMMENT '观众离开时记录离开时间',
  KEY `live_id` (`live_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audience_ibfk_1` FOREIGN KEY (`live_id`) REFERENCES `live_cast` (`live_id`),
  CONSTRAINT `audience_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of audience
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `device_id` varchar(255) NOT NULL COMMENT '设备主键',
  `device_name` varchar(255) NOT NULL COMMENT '设备名称',
  `device_number` varchar(255) NOT NULL COMMENT '设备编号',
  `is_bind` enum('0','1') NOT NULL COMMENT '0没有绑定 1绑定',
  `device_config` varchar(255) DEFAULT NULL COMMENT '设备配置信息',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id 外键',
  PRIMARY KEY (`device_id`),
  UNIQUE KEY `device_number` (`device_number`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `device_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
DROP TABLE IF EXISTS `live_cast`;
CREATE TABLE `live_cast` (
  `live_id` varchar(255) NOT NULL COMMENT '直播主键',
  `user_id` varchar(255) NOT NULL COMMENT '主播id 也就是用户id',
  `start_time` datetime NOT NULL COMMENT '直播开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '直播结束时间 为空正在直播 部位空直播结束',
  `is_private` enum('0','1') NOT NULL COMMENT '0公开 1 私人',
  `location` varchar(255) NOT NULL COMMENT '直播主键',
  `audience_number` int(11) NOT NULL COMMENT '当前直播观众人数',
  PRIMARY KEY (`live_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `live_cast_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of live_cast
-- ----------------------------