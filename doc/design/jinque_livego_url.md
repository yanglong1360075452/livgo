金雀LiveGO视频直播类API
=============

[TOC]

## 1 视频推流

### 1.1 视频耳机获取视频推流URL

​	摄像头直播设备（蓝牙耳机）直播前前调用此API获取rtmp视频流推送URL。

* __Method__
  GET

* __URL__
   http://server.livgo.com.cn/api/app/getLivePushUrl

* __Request__

  | Param    | Type   | Description   |
  | -------- | ------ | ------------- |
  | deviceId | String | 设备ID，蓝牙耳机设备ID |
  直播设备传递设备ID参数，该ID经过加密处理，以动态密文形式传到服务器。

  ​



* __Response__

  > 成功

  ```
  {
      "code": 0,
      "data": "rtmp://9341.livepush.myqcloud.com/live/9341_1?bizid=9341&txSecret=d932bb94d416980a6976f6a5c5df5078&txTime=59644CD7"
  }
  ```

  > 失败

  ```
  {
    "code": -1,
    "data": "直播间未开启"
  }
  ```
  ```
  {
    "code": -2,
    "data": "该设备未绑定用户"
  }
  ```
    ```
  {
    "code": 13,
    "data": "该设备未授权"
  }
    ```



### 1.2 视频耳机推流状态更新通知

​	摄像头直播设备（蓝牙耳机）向后台服务器报告当前状态（开始直播、停止直播、直播出错）。

- __Method__
  GET

- __URL__
  /api/app/notifyDevState

- __Request__

  | Param    | Type    | Description   |
  | -------- | ------- | ------------- |
  | deviceId | String  | 设备ID，蓝牙耳机设备ID |
  | state    | Integer | 设备状态代码，见下表    |
  直播设备传递设备ID参数，该ID经过加密处理，以动态密文形式传到服务器。

  ​

  设备状态代码

  | ID   | 含义                             |
  | ---- | ------------------------------ |
  | 1    | LIVE_START_T 视频直播推送开始(耳机开关触发)  |
  | 2    | LIVE_START_M 视频直播推送开始(手机APP触发) |
  | 3    | LIVE_STOP_T 视频直播推送停止(耳机开关触发)   |
  | 4    | LIVE_STOP_M 视频直播推送停止(手机APP触发)  |
  | -1   | LIVE_ERR 视频推送出错                |


   

- __Response__

  > 成功

  ```
  {
    "code": 0,
    "data": null
  }
  ```

  > 失败

  ```
  {
    "code": -1,
    "data": "直播间未开启"
  }
  ```
  ```
  {
    "code": -2,
    "data": "该设备未绑定用户"
  }
  ```
    ```
  {
    "code": 13,
    "data": "该设备未授权"
  }
    ```






## 2 视频播放

### 2.1 获取视频流播放URL

* __Method__
  POST

* __URL__
  /api/app/getLivePlayUrl

* __Request__

  | Param  | Type    | Description       |
  | ------ | ------- | ----------------- |
  | token  | String  | Authorization 头信息 |
  | roomId | Integer | 直播间ID             |


* __Response__

  > 成功

  ```
  {
      "code": 0,
      "data": {
          "FLV": "http://9341.liveplay.myqcloud.com/live/9341_1.flv",
          "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_1",
          "HLS": "http://9341.liveplay.myqcloud.com/live/9341_1.m3u8"
      }
  }
  ```

  > 失败

  ```
  {
    "code": 13,
    "data": "该用户未授权"
  }
  ```
