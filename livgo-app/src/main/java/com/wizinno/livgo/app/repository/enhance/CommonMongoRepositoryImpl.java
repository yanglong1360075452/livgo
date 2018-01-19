package com.wizinno.livgo.app.repository.enhance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-12.
 */
public class CommonMongoRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T,ID>
        implements CommonRepository<T,ID> {
    protected final MongoOperations mongoTemplate;

    protected final MongoEntityInformation<T, ID> entityInformation;

    public CommonMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);

        this.mongoTemplate=mongoOperations;
        this.entityInformation = metadata;
    }

    protected Class<T> getEntityClass(){
        return entityInformation.getJavaType();
    }

    @Override
    public Page<T> find(Query query, Pageable p) {
        long total=mongoTemplate.count(query, getEntityClass());
        List<T> list=mongoTemplate.find(query.with(p), getEntityClass());

        return new PageImpl<T>(list, p, total);
    }

    @Override
    public T findByQuery(Query query) {
        return mongoTemplate.findOne(query,getEntityClass());
    }

    @Override
    public List<T> findByQuerys(Query query){return mongoTemplate.find(query,getEntityClass());}


    @Override
    public void update(Query query, Update update) {
        mongoTemplate.updateFirst(query,update,getEntityClass());
    }

}
