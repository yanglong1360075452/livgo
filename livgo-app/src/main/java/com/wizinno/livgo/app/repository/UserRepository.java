package com.wizinno.livgo.app.repository;

import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.enhance.UserRepositoryEnhance;
import com.wizinno.livgo.app.repository.enhance.CommonRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by LiuMei on 2017-05-04.
 */
@Repository
public interface UserRepository  extends CommonRepository<User, String>,UserRepositoryEnhance {

    User findByUsernameOrPhone(String username,String phone);

    User findByUsername(String username);

    User findByPhone(String phone);

    User findById(long id);
}
