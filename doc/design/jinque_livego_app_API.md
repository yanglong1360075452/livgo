
金雀LiveGO_APP_API
=============

[TOC]

## 1 登录模块

### 1.1 登入
* __Method__
  POST

* __URL__
  /api/app/login

* __Request__

  | Param          | Type   | Description |
  | -------------- | ------ | ----------- |
  | username/phone | string | 用户名/手机号码登录  |
  | password       | string | 密码          |
  | appVersions    | string | APP版本       |





* __Response__

> 成功

```
{
  "code": 0,
  "data": {
    "id": 1,
    "headImgUrl":"e://a.jpa", //头像
    "nickname":"小明", //昵称
    "userName": "admin",
    "phoneNumber":"1111111" //手机号码
    "sex":0,
    "sexDesc":"男"
    "twoCodeUrl":"e://b.jpa", //二维码
    "inviteTime":"2017.4.24", //受邀时间
    "registerTime":"2017.4.24", //注册时间
    "isActive":"0" //0 非活跃 1活跃
    "status": 1,
    "statusDesc": "活动",
    "token":"qweqweqqfdafffsdfsfsdfsd",
    "userSig":"qweqweqwe";
  }
}
```

> 失败

```
{
  "code": 4,
  "data": "用户名或密码错误"
}
```


### 1.2 登出
* __Method__
  POST

* __URL__
  /api/app/logout

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |



* __Response__

> 成功

```
{
  "code": 0,
  "data": "success"
}
```

> 失败

```
{
  "code": -1,
  "data": "XXX"
}
```

## 2 用户注册模块

### 2.0 发送验证码
* __Method__
  POST

* __URL__
  /api/app/user/captcha/{phone}

* __Request__

  | Param       | Type   | Descriptio |
  | ----------- | ------ | ---------- |
  |             |        |            |
  | appVersions | string | APP版本      |


* __Response__

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
  "code": 9,
  "reason": "验证码发送失败"
}
```

### 2.1 创建用户
* __Method__
  POST

* __URL__
  /api/app/user

* __Request__

  | Param       | Type   | Description |
  | ----------- | ------ | ----------- |
  | username    | String | 用户名         |
  | password    | String | 密码          |
  | phone       | String | 手机号码        |
  | captcha     | String | 验证码         |
  | appVersions | string | APP版本       |




* __Response__

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
  "code": 6,
  "reason": "用户名已存在"
}
```


### 2.2 查询用户
* __Method__
  POST

* __URL__
  /api/app/user/userQuery

* __Request__

  | Param | Type   | Description       |
  | ----- | ------ | ----------------- |
  | token | String | Authorization 头信息 |



* __Response__

> 成功

```
{
  "code": 0,
  "data": {
    "id": 4,
    "phone": "18739923675",
    "username": "qw",
    "password": "$2a$10$Tfun66FN6Qr5DNRUg.CVAOL698WRnkhsNnotCPZtzu7kr/l3Qxfry",
    "img": null,
    "bgImg": null,
    "nickname": null,
    "sex": null,
    "qrCode": null,
    "status": 1,
    "registerTime": 1497857429066,
    "friends": [
      2
    ],
    "editTime": null,
    "devices": [  //用户未绑定 devices为null
      {
        "id": 19,
        "deviceName": "aa",
        "deviceId": "livgo_0003",
        "user": null,
        "deviceModule": "aad",
        "deviceMemo": "bb",
        "bind": true
      }
    ],
    "active": null
  }
  }
}
```
> 失败

```
{
  "code": -1,
  "data": "未授权用户"
}
```


### 2.3 注销用户
* __Method__
  POST

* __URL__
  /api/app/user/logoff

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |


* __Response__

> 成功

```
{
  "code": 0,
  "data": null;

}
```

### 2.4查询手机号是否被注册过

- __**Method**__POST

- **URL**/api/app/user/isPhoneRegister

- __**Request**__

- | Param        | Type     | Description |
  | ------------ | -------- | ----------- |
  | PhoneNumbers | String[] | 手机号码        |



__***\*Response****__

成功

```
{
  "code": 0,
  "data": ["12345678901","12345673901"];

}
```

失败

```
{
  "data": "参数错误",
  "code": 2
}
```

## 3 设备

### 3.1 绑定设备

手机APP绑定设备时通过蓝牙接口从设备读取相关信息，该信息通过以下接口传参到服务器，服务器在设备表中查deviceId，并做相应处理：

1. 若设备不存在，则在服务器设备表中添加该设备并将其绑定到所请求的用户。

2. 若设备已存在但并未与用户绑定，则直接将该设备绑定用户。

3. 若设备已存在并且已经与其他用户绑定，则先解除与前用户的绑定，并同新用户进行绑定。

   ​

* __Method__
  POST

* __URL__
  /api/app/user/binding

* __Request__

  | Param        | Type   | Description             |
  | ------------ | ------ | ----------------------- |
  | token        | String | Authorization 头信息       |
  | appVersions  | String | APP版本                   |
  | deviceName   | String | 设备名称                    |
  | deviceModule | String | 设备型号                    |
  | deviceId     | String | 设备标识（替代原deviceNumber参数） |
  | deviceMemo   | String | 设备备注                    |


* __Response__

> 成功

```
{
  "code": 0,
  "data"：null
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```



### 3.2 我的设备-解除绑定

* __Method__
  POST

* __URL__
  /api/app/user/removeBinding

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |



* __Response__

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
  "code": 2,
  "reason": "参数错误"
}
```





##  4 直播

### 4.1 开启直播间

* __Method__
  POST
* __URL__
  /api/app/createLiveRoom

* __Request__

  | Param       | Type    | Description          |
  | ----------- | ------- | -------------------- |
  | token       | String  | Authorization 头信息    |
  | appVersions | String  | app版本号               |
  | invite      | Integer | 直播邀请多个好友，传数组（公开直播不传） |
  | location    | string  | 直播地理位置               |
  | livePicture | string  | 直播缩略图                |
  | type        | Integer | 直播类型                 |



* **type参数number含义**

* | number | 含义   |
  | ------ | ---- |
  | 0      | 公开直播 |
  | 1      | 私人直播 |

* __Response__

> 成功

```
{
  "code": 0,
  "data":  {
              "id": 1,  //直播间ID
              user：{"id":"1", //直播人ID
                      "nickname":"熊明",//昵称
                      "username"："张三",//用户名
                      "img":"********",//用户头像
                      "watchLive":false//是否观看直播}
              "startTime":"2017.4.25", //开始时间
              "endTime": null,结束时间
              "isPrivate":"0" ,//私人直播
              "livePicture":"ks000dgjl",//直播缩略图
              "location":"beijing",//直播地理位置
              "pushUrl":"jfladksjgfasdkjfjlkadsmf", //推流url
              "playUrl":"jfladksjgfasdkjfjlkadsmf", //播放url

         }
}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```


### 4.2 关闭直播间

* __Method__
  POST

* __URL__
  /api/app/closeLiveRoom

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | liveId      | Long   | 直播间ID             |
  | appVersions | String | app版本号            |



* __Response__

> 成功

```
{
  "code": 0,
  "data":  {
              "id": 1,  //直播间ID
              user：{"id":"1", //直播人ID
                      "nickname":"熊明",//昵称
                      "username"："张三",//用户名
                      "watchLive":false//是否观看直播}
              "startTime":"2017.4.25", //开始时间
              "endTime": "2017.4.26", ,结束时间
              "isPrivate":"0" ,//私人直播
              "livePicture":"ks000dgjl",//直播缩略图
              "location":"beijing",//直播地理位置
              "pushUrl":"jfladksjgfasdkjfjlkadsmf", //推流url
              "playUrl":"jfladksjgfasdkjfjlkadsmf", //播放url
              "count":45,//观众人次
              "maxCount" :25//某时刻观众最多的人数

         }
}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```


### 4.3 邀请直播观众

* __Method__
  POST

* __URL__
  /api/app/inviteFriends


* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | liveId      | Long   | 直播间ID             |
  | invite      | Long   | 直播人邀请好友ID，传输组     |
  | appVersions | String | app版本号            |






* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```

### 4.4 直播间查看观众

* __Method__
  POST

* __URL__
  /api/app/liveFriends

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | liveId      | Long   | 直播间Id             |
  | appVersions | String | app版本号            |




* __Response__

> 成功

```
  {
  "code": 0,
  "data": {
    "data": [{
     "userID": 1,  //当前用户的id
      "friendID":"2", //好友Id
      "friendName":"小红" //好友昵称
      "headImgUrl":"a.jpa" //好友头像
    }，{
      "userID": 1,  //当前用户的id
      "friendID":"3", //好友Id
      "friendName":"小红" //好友昵称
      "headImgUrl":"a.jpa" //好友头像
    }
    ]
  }
  }
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```



### 4.5 剔出观看直播的用户（带userId参数）

### /观众退出直播（不带userId参数）

* POST
* __URL__
  /api/app/outlive

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | liveId      | Long   | 直播间ID             |
  | userId      | Long   | 观看用户Id            |
  | appVersions | String | app版本号            |





* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```



### 4.6 进入直播间-观众

* __Method__
  POST

* __URL__
  /api/app/spectator

* __Request__

  |    Param    | Type   | Description       |
  | :---------: | ------ | ----------------- |
  |    token    | String | Authorization 头信息 |
  |   liveId    | Long   | 直播间I              |
  |             |        |                   |
  | appVersions | String | app版本号            |



* __Response__

> 成功

```
{
  "code": 0,
  "data": {
  rtmpUrl："sdgfasdlkfgasd;lfk",
  flvUrl:sdgfasdlkfgasd",
  hlsUrl:sdgfasdlkfgasd"

}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.7获取好友直播列表

- **Method**

  POST

- **URL**

  /api/app/liveQuery

- **Request**

| Param    | Type   | Description                  |
| -------- | ------ | ---------------------------- |
| token    | String | Authorization 头信息            |
| nickName | String | 昵称（不传此参数查询所有，传此参数搜索昵称为***直播） |

- **Response**

> 成功

```
{
  "code": 0,
   "data": [
    {
      "id": 95,
      "user": {
        "id": 5,
        "nickname": "",
        "username": "MONKEY",
        "img":"*******"
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852101353,
      "endTime": 1505097602384,
      "type": 0,
      "liveStatue":0, //0 未开始直播  1 已经开始直播
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_95?bizid=9341&txSecret=ce583700a240a4a8e34ecb6e5c9b48a5&txTime=599CEE85",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_95.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_95",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_95.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,//累计观看人数
      "maxCount": 0,//最高人数
      "audience": null,
      "historyCount": null//历史直播次数
    },
    {
      "id": 99,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852587997,
      "endTime": 1505098135369,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_99?bizid=9341&txSecret=cc0206facf1c13d7560c7721828bcaaa&txTime=599CF06C",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_99.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_99",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_99.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    },
    {
      "id": 100,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852689011,
      "endTime": 1505015624962,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_100?bizid=9341&txSecret=67b2b9bf1fd57ffcf2e51a16d5d083fa&txTime=599CF0D1",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_100.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_100",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_100.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    },
    {
      "id": 101,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852795830,
      "endTime": 1505096405837,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_101?bizid=9341&txSecret=c1791c80338b86e0ca974a4fbd6e7a87&txTime=599CF13B",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_101.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_101",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_101.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    }
  ]

}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.7获取好友历史直播列表

- **Method**

  POST

- **URL**

  /api/app/queryHistoryLive

- Request

- | Param         | Type   | Description |
  | ------------- | ------ | ----------- |
  | token         | String | 头信息         |
  | nickName（可不选） | String | 昵称          |

成功

```
{
  "code": 0,
 "data": [
    {
      "id": 95,
      "user": {
        "id": 5,
        "nickname": "",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852101353,
      "endTime": 1505097602384,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_95?bizid=9341&txSecret=ce583700a240a4a8e34ecb6e5c9b48a5&txTime=599CEE85",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_95.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_95",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_95.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,//累计观看人数
      "maxCount": 0,//最高人数
      "audience": null,
      "historyCount": null//历史直播次数
    },
    {
      "id": 99,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852587997,
      "endTime": 1505098135369,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_99?bizid=9341&txSecret=cc0206facf1c13d7560c7721828bcaaa&txTime=599CF06C",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_99.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_99",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_99.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    },
    {
      "id": 100,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852689011,
      "endTime": 1505015624962,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_100?bizid=9341&txSecret=67b2b9bf1fd57ffcf2e51a16d5d083fa&txTime=599CF0D1",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_100.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_100",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_100.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    },
    {
      "id": 101,
      "user": {
        "id": 5,
        "nickname": "Monkey",
        "username": "MONKEY",
        "watchLive": false
      },
      "deviceNumber": null,
      "startTime": 1502852795830,
      "endTime": 1505096405837,
      "type": 0,
      "typeDesc": null,
      "pushUrl": "rtmp://9341.livepush.myqcloud.com/live/9341_101?bizid=9341&txSecret=c1791c80338b86e0ca974a4fbd6e7a87&txTime=599CF13B",
      "playUrl": {
        "FLV": "http://9341.liveplay.myqcloud.com/live/9341_101.flv",
        "RTMP": "rtmp://9341.liveplay.myqcloud.com/live/9341_101",
        "HLS": "http://9341.liveplay.myqcloud.com/live/9341_101.m3u8"
      },
      "location": "111",
      "livePicture": "1",
      "count": 0,
      "maxCount": 0,
      "audience": null,
      "historyCount": null
    }
  ]

}
```

失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

直播的视频

* __Method__
  POST

* __URL__
  /api/app/liveQuery

* __Request__

  | Param  | Type    | Description |
  | ------ | ------- | ----------- |
  | userId | Integer | 用户Id        |



* __Response__

> 成功

```
{

      "code": 0,
         "data": {
             ""total": 2"
             "data": [{
                   "nickname":"小明" , //直播人id
                   "直播间ID":"3", //直播间ID
                    "startTime":"2017.4.26" , //开始直播
                    "endTime": null ,  //结束直播
                    "isPrivate":0,  //0:公开,1:私人
                    "audienceNumber": 45, //观众人数
                 },{

                     "nickname":"小梅" , //直播人id
                     "直播间ID":"4", //直播间ID
                     "startTime":"2017.4.26" , //开始直播
                      "endTime": null ,  //结束直播
                      "isPrivate":0,  //0:公开,1:私人
                      "audienceNumber": 45, //观众人数
             }]
              "length": 10,
               "page": 1
         }
}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.8 手机推流状态更新通知

​	手机端直播时向后台服务器报告当前状态（开始直播、停止直播、直播出错）。

- __Method__
  GET

- __URL__
  /api/app/notifyLiveState

- __Request__

  | Param       | Type    | Description       |
  | ----------- | ------- | ----------------- |
  | token       | String  | Authorization 头信息 |
  | liveId      | Long    | 直播间ID             |
  | state       | Integer | 设备状态代码，见下表        |
  | appVersions | String  | app版本号            |

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


### 4.9查询谁邀请我参私人直播

- __**Method**__GET

- **URL**/api/app/inviteLive

- __**Request**__

- | Param       | Type   | Descripition |
  | ----------- | ------ | ------------ |
  | token       | String | 头信息          |
  | appVersions | String | app版本        |

Response

成功

```
{
  "code": 0,
  "data": [
    {
      "id": 4,
      "nickname": "菲菲",
      "username": "qw",
      "watchLive": false
    },
    {
      "id": 6,
      "nickname": null,
      "username": "davidgu",
      "watchLive": false
    }
  ]
}
```

失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.10 直播过程发消息

- __**Method**__

- POST

- **URL**

- /api/app/sendMessage

- __**Request**__

- | param   | type   | Description |
  | ------- | ------ | ----------- |
  | token   | String | 头信息         |
  | liveId  | Long   | 直播间id       |
  | message | byte[] | 发送消息的内容     |

**Response**

**成功**

```
{
  "code": 0,
  "data": null
}
```

**失败**

```
{
  "code": 2,
  "data": “参数错误”
}

{
  "code": 20,
  "data":“直播间不存在”
}
```

### 4.11 观众直播间查看历史消息

**Method**

post

**URL**

api/app/historyMessage

**Request**

| param  | type   | descirption      |
| ------ | ------ | ---------------- |
| token  | String | Authorization头信息 |
| liveId | Long   | 直播间id            |

**Response**

成功

```
{
  "code": 0,
  "data": {
    "img":"******",//直播人头像
    "userName":"aaa"//直播人用户名
    "startTime": 1505016199567, //直播开始时间
    "endTime": 1505016206190,//直播结束时间
    "count": 0,//直播间观众人次
    "liveMsg": [
      {
        "id": 740,
        "liveId": 700,
        "sendUsrname": "gjjhxjxjjx",
        "picture": null,
        "context": "已经开始直播了",
        "creatTime": 1505016199748,
        "role": 1
      },
      {
        "id": 741,
        "liveId": 700,
        "sendUsrname": "gjjhxjxjjx",
        "picture": null,
        "context": "关闭了直播间",
        "creatTime": 1505016206193,
        "role": null
      }
    ]
  }
}
```

失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.12直播间详情

****Method****

post

**URL**

api/app/liveDetail

Request

| param  | type   | description |
| ------ | ------ | ----------- |
| token  | String | 头信息         |
| liveId | Long   | 直播间id       |

Response

成功

```
{
    "_id" : NumberLong(754),
    "user" : {
        "_id" : NumberLong(3),
        "nickname" : "飞的更好",
        "username" : "qwer",
        "img":"adlsnkfglnasdlfjk"
        "watchLive" : false
    },
     "deviceNumber": null,
    "startTime" : ISODate("2017-09-15T08:12:22.557Z"),
    "type" : 0,
    "location" : "111",
    "livePicture" : "1",
    "pushUrl" : "rtmp://9341.livepush.myqcloud.com/live/9341_754?bizid=9341&txSecret=37fe73bc7fd7f5823a1da1ad80ef3e3f&txTime=59C4C5E6",
    "playUrl" : [ 
        {
            "FLV" : "http://9341.liveplay.myqcloud.com/live/9341_754.flv",
            "RTMP" : "rtmp://9341.liveplay.myqcloud.com/live/9341_754",
            "HLS" : "http://9341.liveplay.myqcloud.com/live/9341_754.m3u8"
        }
    ]
}
```

失败

```
{
  code:2,
  data:"参数错误"
}
```
###4.13直播邀请记录
* __Method__
  POST

* __URL__
  /api/app/liveInviteRecords

* __Request__

  | Param | Type   | Description       |
  | ----- | ------ | ----------------- |
  | token | String | Authorization 头信息 |
     |



* __Response__

> 成功

```
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "liveImg": null,
      "liveUserName": "qwer",
      "isInviteName": "coolcloud",
      "content": "邀请你参见直播",
      "createTime": 1505901115272
    },
    {
      "id": 4,
      "liveImg": null,
      "liveUserName": "qwer",
      "isInviteName": "coolcloud",
      "content": "邀请你参见直播",
      "createTime": 1505901977068
    },
    {
      "id": 7,
      "liveImg": null,
      "liveUserName": "qwer",
      "isInviteName": "coolcloud",
      "content": "邀请你参见直播",
      "createTime": 1505902159954
    },
    {
      "id": 10,
      "liveImg": "http://118.89.114.55:8080/img/1503891382978bgimg.jpg", //邀请人的头像
      "liveUserName": "qwer",//邀请人的用户名
      "isInviteName": "coolcloud",//被邀请人的用户名
      "content": "邀请你参见直播",
      "createTime": 1505902170869//时间
    }
  ]
}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

### 4.14 直播间恢复

- __**Method**__POST

- **URL**/api/app/recoverLive

- **Request**

- | param | type   | Description |
  | ----- | ------ | ----------- |
  | token | String | 头信息         |



**Response**

成功

```
{
  "code":0,
  "data":{
    "playUrl" : [ //如果data中有值就恢复现场
        {
            "FLV" : "http://9341.liveplay.myqcloud.com/live/9341_754.flv",
            "RTMP" : "rtmp://9341.liveplay.myqcloud.com/live/9341_754",
            "HLS" : "http://9341.liveplay.myqcloud.com/live/9341_754.m3u8"
        }
    ]
  }
}
```

失败

```
{
  "code": 2,
  "reason": "认证失败"
}
```

### 4.15异常关闭直播间

- **Method**POST

- **URL**/api/app/exceptionCloseLive

- Request

  | param    | type   | description |
  | -------- | ------ | ----------- |
  | liveId   | Long   | 直播间id       |
  | packages | String | 心跳包         |
  |          |        |             |

**Response**

成功

```
{
  "code": 0,
  "data": null
}
```

失败

```
{
  "code": 2,
  "data": "参数错误"
}
```

## 5 离线录制

## 6 我的好友

### 6.1 添加好友

* __Method__
  POST

* __URL__
  /api/app/user/addFriend

* __Request__

  | Param        | Type     | Description       |
  | ------------ | -------- | ----------------- |
  | token        | String   | Authorization 头信息 |
  | phoneNumbers | String[] | 添加人的手机号码          |
  | appVersions  | string   | APP版本             |



* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```

### 6.2 邀请好友注册

* __Method__
  POST

* __URL__
  /api/app/user/friendRegister

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | phoneNumber | String | 添加人的手机号码或者用户名     |
  | appVersions | string | APP版本             |



* __Response__

> 成功

```
{
  "code": 0,
  "data": null
}

{
  "code": 0,
  "data": {
    "id": 1,
    "headImgUrl":"e://a.jpa", //头像
    "nickname":"小明", //昵称
    "userName": "admin",
    "phoneNumber":"1111111" //手机号码
    "sex":0,
    "sexDesc":"男"
    "twoCodeUrl":"e://b.jpa", //二维码
    "inviteTime":"2017.4.24", //受邀时间
    "registerTime":"2017.4.24", //注册时间
    "isActive":"0" //0 非活跃 1活跃
    "status": 1,
    "statusDesc": "活动",
    "token":"qweqweqqfdafffsdfsfsdfsd",
    "userSig":"qweqweqwe";
  }
}
```

> 失败

```
{
  "code": 2,
  "data": "参数错误"
}
```


### 6.3 用户同意添加好友

* __Method__
  POST

* __URL__
  /api/app/user/friendAgree

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |
  | friendId    | long   | 好友ID              |



* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```



### 6.4 用户拒绝添加好友

* __Method__
  POST

* __URL__
  /api/app/user/friendReject

* __Request__

  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |
  | friendId    | long   | 好友ID              |



* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```


### 6.5 查询好友

* __Method__
  POST

* __URL__
  /api/app/user/friends

* __Request__

  | Param | Type   | Description       |
  | ----- | ------ | ----------------- |
  | token | String | Authorization 头信息 |


* __Response__

> 成功

```
{"code":0,"data":[{"id":2,"phone":"15268545409","username":"coolcloud","password":"$2a$10$.3lfc1BwAzl6WfTNv8DB8Ouz9Nc9iVWkOYeHu2cKy6sJXkFcxY3h6","img":"http://118.89.114.55:8080/img/1505020096238bgimg.jpg","bgImg":"","nickname":"","sex":0,"qrCode":"","status":1,"registerTime":1502072988802,"friends":[4,3,1,5],"editTime":null,"devices":[{"id":54,"deviceName":"livgo_8268","deviceId":"J78100478","user":{"id":2,"phone":null,"username":null,"password":null,"img":null,"bgImg":null,"nickname":"","sex":null,"qrCode":null,"status":null,"registerTime":null,"friends":null,"editTime":null,"devices":null,"liveCounts":null,"active":null},"deviceModule":"livgo_8268","deviceMemo":"livgo_8268","bindTime":1508897974395,"bind":true}],"liveCounts":147,"active":true},{"id":1,"phone":"13916130572","username":"davidgu","password":"$2a$10$KusKYEuG6HMmaoFHPeIG1.36j9PCAP4PBiqhIH4K1ZyCosjMA/mR.","img":"http://118.89.114.55:8080/img/1506571714947bgimg.jpg","bgImg":"","nickname":"谷哥","sex":0,"qrCode":"","status":1,"registerTime":1502071345539,"friends":[3,7,2,5,4,13],"editTime":1506571893388,"devices":[],"liveCounts":12,"active":true},{"id":4,"phone":"13696655620","username":"yanglong","password":"$2a$10$J7IyYfnpKHS1mM7yNpd.l.nbMi9YXPey8Iw/ERHVebY8R/CEKFww.","img":"http://118.89.114.55:8080/img/1502171400242bgimg.jpg","bgImg":"http://118.89.114.55:8080/img/1502171432487bgimg.jpg","nickname":"龙","sex":0,"qrCode":"","status":1,"registerTime":1502157532797,"friends":[2,3,5,1],"editTime":null,"devices":null,"liveCounts":10,"active":true},{"id":7,"phone":"13552880326","username":"Jin00001","password":"$2a$10$2Ks.fhp0SqJ7N07xiuTGru7WG0vJX925GZL4IyvponVqzBXmVrILS","img":"http://118.89.114.55:8080/img/1506585006871bgimg.jpg","bgImg":"","nickname":"可口可乐","sex":0,"qrCode":"","status":1,"registerTime":1504491914402,"friends":[3,1,5,13],"editTime":null,"devices":[],"liveCounts":73,"active":true},{"id":11,"phone":"13605199359","username":"gjjhxjxjjx","password":"$2a$10$B6pMkQzm0rp.Mg3BvQV5FecO.94mgja0mEd5CO20IdIVUflL0td2.","img":"http://118.89.114.55:8080/img/1505014040432bgimg.jpg","bgImg":"http://118.89.114.55:8080/img/1505014011044bgimg.jpg","nickname":"","sex":0,"qrCode":"","status":1,"registerTime":1505012017052,"friends":[3],"editTime":null,"devices":[{"id":33,"deviceName":"livgo_0269","deviceId":"J7810075H","user":{"id":11,"phone":null,"username":null,"password":null,"img":null,"bgImg":null,"nickname":"","sex":null,"qrCode":null,"status":null,"registerTime":null,"friends":null,"editTime":null,"devices":null,"liveCounts":null,"active":null},"deviceModule":"livgo_0269","deviceMemo":"livgo_0269","bindTime":1505017709099,"bind":true}],"liveCounts":null,"active":true},{"id":5,"phone":"17011860809","username":"MONKEY","password":"$2a$10$udda7RDxeLueyyPzGDqoze9AMAL723yTWQ3VUVcLFXjsea5j0l7Ga","img":"http://118.89.114.55:8080/img/1507606431991bgimg.jpg","bgImg":"","nickname":"Test","sex":0,"qrCode":"","status":1,"registerTime":1502852030961,"friends":[1,7,3,2,4,5,12,13],"editTime":null,"devices":[],"liveCounts":223,"active":true},{"id":12,"phone":"12345678801","username":"abc","password":"$2a$10$v0Utun22ESEo4KrSBMyOyOvwCxurhxt6MQ9KLlFkGJwdnzoKlEr7a","img":"http://118.89.114.55:8080/img/1508388175399bgimg.jpg","bgImg":"","nickname":"","sex":0,"qrCode":"","status":1,"registerTime":1505624481092,"friends":[3,5],"editTime":null,"devices":[],"liveCounts":21,"active":true},{"id":3,"phone":"18739923673","username":"qwer","password":"$2a$10$v0Utun22ESEo4KrSBMyOyOvwCxurhxt6MQ9KLlFkGJwdnzoKlEr7a","img":"http://118.89.114.55:8080/img/1503891382978bgimg.jpg","bgImg":"http://118.89.114.55:8080/img/1502697306733bgimg.jpg","nickname":"飞的更好","sex":0,"qrCode":"","status":1,"registerTime":1502085779777,"friends":[2,1,4,7,11,5,12,3,13],"editTime":1502106791508,"devices":[{"id":45,"deviceName":"livgo_8268","deviceId":"J78108268","user":{"id":3,"phone":null,"username":null,"password":null,"img":null,"bgImg":null,"nickname":"飞的更好","sex":null,"qrCode":null,"status":null,"registerTime":null,"friends":null,"editTime":null,"devices":null,"liveCounts":null,"active":null},"deviceModule":"livgo_8268","deviceMemo":"livgo_8268","bindTime":1509075320359,"bind":true}],"liveCounts":337,"active":true},{"id":13,"phone":"17702105757","username":"17702105757","password":"$2a$10$/SAEx60Q5Du5gGS3IdEYuOGpXFCMSdFdToz4MLidR67Y2OAvxBJJm","img":"","bgImg":"","nickname":"大大的勇敢","sex":0,"qrCode":"","status":1,"registerTime":1507705675106,"friends":[7,5,1,3],"editTime":null,"devices":[],"liveCounts":42,"active":true}]}
```

> 失败

```
{
  "code": -1,
  "data": "XXX"
}
```


### 6.6 删除好友

* __Method__
  POST

* __URL__
  /api/app/user/dropUser

* __Request__
  | Param       | Type   | Description       |
  | ----------- | ------ | ----------------- |
  | token       | String | Authorization 头信息 |
  | appVersions | string | APP版本             |
  | friendId    | long   | 好友Id              |



* __Response__

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
  "code": 2,
  "data": "参数错误"
}
```

### 6.7 查看谁邀请我为好友

- __**Method**__

- POST

- **URL**

- /api/app/user/queryInvite

- __**Request**__

- __**Response**__

- 成功

- ```
  {
    "code": 0,
    "data": [
      {
        "id": 2,
        "img":"************",//用户头像地址
        "requestName": "aa",//请求加好友的用户
        "acceptName": "bb",//接受邀请的用户
        "statue": "1",//1：需要接受邀请的人没有做任何操作 2：***同意  3：***拒绝 
  }，{
     "id": 3,
     "requestName": "aa",//请求加好友的用户
     "acceptName": "bb",//接受邀请的用户
     "statue": "2",//1：需要接受邀请的人没有做任何操作 2：***同意  3：***拒绝 
  }
  ```

  失败

  ```
  {
    "code": 2,
    "reason": "认证失败"
  }
  ```

> 成功

```
{  "code": 0,  "data": null}

```

> 失败

```
{
  "code": 2,
  "reason": "参数错误"
}
```

> 成功

```
{  "code": 0,  "data": null}

```

> 失败

```
{
  "code": 2,
  "reason": "参数错误"
}
```

## 7 我的视频


## 8 我的

### 8.1 编辑我的头像
* __Method__
  POST

* __URL__
  /api/app/user/editor

* __Request__

  | Param | Type          | Description       |
  | ----- | ------------- | ----------------- |
  | token | String        | Authorization 头信息 |
  | img   | MultipartFile | 头像文件              |





* __Response__

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
  "code": 2,
  "reason": "参数错误"
}
```



### 8.2编辑我的背景墙

* __Method__
  POST

* __URL__
  /api/app/user/editorBgImg

* __Request__

  | Param | Type          | Description       |
  | :---- | ------------- | ----------------- |
  | token | String        | Authorization 头信息 |
  | bgImg | MultipartFile | 背景墙文件             |




* __Response__

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
  "code": 2,
  "reason": "参数错误"
}
```

### 8.3修改密码

* __Method__
  POST

* __URL__
  /api/app/user/setPassword

* __Request__

  | Param      | Type   | Description       |
  | ---------- | ------ | ----------------- |
  | token      | String | Authorization 头信息 |
  | password   | string | 用户密码              |
  | rePassword | string | 用户重设的密码           |



* __Response__

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
  "code": 2,
  "reason": "参数错误"
}
```

### 8.4 编辑我的性别和昵称

* __Method__
  POST

* __URL__
  /api/app/user/editorText

* __Response__

  | Param    | Type    | Description       |
  | -------- | ------- | ----------------- |
  | token    | String  | Authorization 头信息 |
  | nickname | string  | 昵称                |
  | sex      | Integer | 性别  0 男 1女 2 未知   |




* __Response__

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
  "code": 2,
  "reason": "参数错误"
}
```
### 8.5忘记密码

- __**Method**__

- POST

- **URL**

- /api/app/user/forgetPassword

- __**Response**__

- | Param    | Type   | Decripition |
  | -------- | ------ | ----------- |
  | token    | String | 头信息         |
  | captcha  | String | 验证码         |
  | password | String | 密码          |
  | phone    | String | 电话号码        |

成功

```
{
  "code": 0,
  "data": null
}
```

失败

```
{
  "code": 2,
  "reason": "参数错误"
}
```
### 8.6修改手机号

**method**

post

**url**

/api/app/user/alterPhone

**Request**

| Param       | Type   | Description |
| ----------- | ------ | ----------- |
| token       | String | 头信息         |
| captcha     | String | 验证码         |
| phoneNumber | String | 电话号码        |
| appVersions | String | app版本号      |

**Response**

成功

```
{
  "code": 0,
  "data": null
}
```

 失败

```
{
  "code":2,
  "data":"参数错误"
}

 

{
  "code":2,
  "data":"该手机号已经注册"
}
```
## 9.消息

### 9.1观众获取主播消息

****method****

post

**url**

/api/app/message/liveMsg

Request

| Param | Type   | Description |
| ----- | ------ | ----------- |
| token | String | 头信息         |

Response

成功

```
{
  "code": 0,
  "data": [
    "qw创建直播间"，
    "qw开始直播"，
    "qw关闭直播"
    
  ]
}
```

失败

```
{
 "code" :2,
 "data" :"token验证失败"
}

```

### 9.2主播获取观众消息

********method********

post

**url**

/api/app/message/audienceMessage

Request

| Param | Type   | Description |
| ----- | ------ | ----------- |
| token | String | 头信息         |

Response

成功

```
{
  "code": 0,
  "data": [
    "qw进入直播"，
    "qw退出直播"
  ]
}
```

失败、

```
 

{
 "code" :2,
 "data" :"token验证失败"
}

```


### 9.3 获取直播缩略图

********method********

post

**url**

/api/app/Thumbnail

Request

| Param | Type | Description |
| ----- | ---- | ----------- |
| 无     |      |             |

Response

成功

```
{
    "channel_id": "2016090090936", //直播间信息
    "create_time": 1473645788, 
    "event_type": 200, //状态200成功
    "pic_url": "/2016-09-12/2016090090936-screenshot-10-03-08-1280x720.jpg", //文件路径名
    "sign": "8704a0297ab7fdd0d8d94f8cc285cbb7", 
    "stream_id": "2016090090936",  //直播间信息
    "t": 1473646392,
    "pic_full_url":"http://c0b31d74lvb1253289177screenshot-1252813850.file.myqcloud.com/2017-08-29/9341_434-screenshot-10-53-45-544x960.jpg"//图片的完整路径
}

```

失败、

```
 

{
 "code" :2,
 "data" :"token验证失败"
}

```