package com.wizinno.livgo.app.repository.enhance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by LiuMei on 2017-05-10.
 */
public class UserRepositoryImpl implements UserRepositoryEnhance{

    @Autowired
    MongoTemplate mongoTemplate;




}
