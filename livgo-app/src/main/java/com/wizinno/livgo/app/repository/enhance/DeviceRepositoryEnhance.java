package com.wizinno.livgo.app.repository.enhance;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by HP on 2017/5/12.
 */
public interface DeviceRepositoryEnhance {

    void updataDevice(Query query, Update update);
}
