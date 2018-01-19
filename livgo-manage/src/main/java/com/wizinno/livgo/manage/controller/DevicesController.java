package com.wizinno.livgo.manage.controller;
import com.wizinno.livgo.app.data.device.DeviceSimple;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.utils.DeviceTransUtil;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.data.PageData;
import com.wizinno.livgo.data.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
@RestController
@RequestMapping("/api/manage/devices")

public class DevicesController {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    LogRepository logRepository;
    /**
     * 获取设备列表
     * @param  filter
     * @return
     */
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
    public ResponseVO getDeviceList(@RequestParam(value = "page",defaultValue = "0") int page,
                                    @RequestParam(value = "length",defaultValue = "10") int length,
                                    @RequestParam(value = "filter",required = false) String filter){
        Query query = new Query();
        if(!Util.stringNull(filter)){
            query.addCriteria(Criteria.where("deviceName").regex(filter));
        }
        Pageable pageable = new PageRequest(page,length);
        Page<Device> devicePage= deviceRepository.find(query,pageable);
        List<DeviceSimple>devices=DeviceTransUtil.devicesToDeviceSimples(devicePage.getContent());
        return new ResponseVO(new PageData<>(page,length,devices,devicePage.getTotalPages(),devicePage.getTotalElements()));

    }

    /**
     * 根据设备编号获取设备详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseVO getDeviceDetail(@PathVariable(value = "id")String id){
    return new ResponseVO(DeviceTransUtil.deviceToExtend(deviceRepository.findById(Long.parseLong(id))));
    }

}
