package com.wizinno.livgo.app.repository.enhance;

import com.wizinno.livgo.app.document.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by HP on 2017/5/12.
 */
public class DeviceRepositoryImpl implements DeviceRepositoryEnhance {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updataDevice(Query query, Update update) {
        mongoTemplate.updateFirst(query,update,Device.class);
    }
}
