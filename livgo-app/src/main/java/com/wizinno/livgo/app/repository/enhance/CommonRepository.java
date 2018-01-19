package com.wizinno.livgo.app.repository.enhance;

/**
 * Created by LiuMei on 2017-05-12.
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CommonRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

    Page<T> find(Query query, Pageable p);

    T findByQuery(Query query);

    List<T> findByQuerys(Query query);

    void update(Query query, Update update);
}
