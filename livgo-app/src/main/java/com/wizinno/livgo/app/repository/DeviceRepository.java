package com.wizinno.livgo.app.repository;

import com.wizinno.livgo.app.repository.enhance.DeviceRepositoryEnhance;
import com.wizinno.livgo.app.document.Device;
import com.wizinno.livgo.app.repository.enhance.CommonRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by HP on 2017/5/9.
 */
@Repository
public interface DeviceRepository extends DeviceRepositoryEnhance, MongoRepository<Device, String>, CommonRepository<Device,String> {
    Device findById(Long id);
}

