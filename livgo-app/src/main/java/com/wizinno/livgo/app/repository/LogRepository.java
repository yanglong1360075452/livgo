package com.wizinno.livgo.app.repository;

import com.wizinno.livgo.app.repository.enhance.CommonRepository;
import com.wizinno.livgo.app.document.Log;
import org.springframework.stereotype.Repository;

/**
 * Created by LiuMei on 2017-05-09.
 */
@Repository
public interface LogRepository extends CommonRepository<Log, String> {
}
