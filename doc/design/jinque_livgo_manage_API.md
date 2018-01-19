金雀LiveGO_Manage_API
=============
## 1 设备列表

### 1.1 获取设备列表
* __Method__
  POST

* __URL__
  /api/manage/devices/getDeviceList

* __Request__

 Param | Type | Description
 filter|String|过滤条件

* __Response__

> 成功

```
{
  "code": 0,
  "data": {
    "id":"001",
    "deviceName":"蓝牙耳机",
    "deviceNumber":"设备编号",
    "bingUserTime":绑定用户时间
    "user":"绑定用户",
    "type":"设备型号"
  }
}
```

> 失败

```
{
  "code": -1,
  "data": "xxxx"
}
```

### 1.2查看设备详情
* __Method__
  POST

* __URL__
  /api/manage/devices/getDeviceDetail

* __Request__

 Param | Type | Description

 deviceNumber | String | 设备编号





* __Response__

> 成功

```
{
  "code": 0,
  "data": {
      "id":"001",
      "deviceName":"蓝牙耳机",
      "deviceNumber":"设备编号",
      "idBand":"是否绑定",
      "user":"绑定用户",
      "deviceConfig":"设备信息",
      "isActive":"0（设备未激活）1（设备已经激活）",
      "activeTime":"2017/05/08",
      "bingUserTime":"2017/05/08",
      "relieveBingTime":"2017/05/08",
      "note":"备注"
}
}
```

> 失败

```
{
  "code": -1,
  "data": "XXX"
}
```
