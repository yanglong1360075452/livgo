package com.wizinno.livgo.manage.controller;

import com.wizinno.livgo.app.data.user.UserExtend;
import com.wizinno.livgo.app.data.user.UserSimple;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.UserTransUtil;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.PageData;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * Created by LiuMei on 2017-05-15.
 */
@RestController
@RequestMapping("/api/manage/users")
public class UsersController extends BaseController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 获取用户列表
     * @param page
     * @param length
     * @param username
     * @param  nickname
     * @param  phone
     * @param status
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseVO getUsers(@RequestParam(value = "page",defaultValue = "0") int page,
                               @RequestParam(value = "length",defaultValue = "10") int length,
                               @RequestParam(value = "username",required = false) String username,
                               @RequestParam(value = "nickname",required = false) String nickname,
                               @RequestParam(value = "phone",required = false) String phone,
                               @RequestParam(value = "status",required = false) Integer status) {
        Query query = new Query();
        if(!Util.stringNull(username)){
            query.addCriteria(Criteria.where("username").regex(username));
        }
        if(!Util.stringNull(nickname)){
            query.addCriteria(Criteria.where("nickname").regex(nickname));
        }
        if(!Util.stringNull(phone)){
            query.addCriteria(Criteria.where("phone").regex(phone));
        }
        if(status != null){
            query.addCriteria(Criteria.where("status").is(status));
        }
        Pageable pageable = new PageRequest(page,length);
        Page<User> userPage = userRepository.find(query,pageable);
        List<UserSimple> userSimples = UserTransUtil.UsersToSimples(userPage.getContent());
        return new ResponseVO(new PageData<>(page,length,userSimples,userPage.getTotalPages(),userPage.getTotalElements()));
    }

    /**
     * 根据用户ID获取用户详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseVO getUser(@PathVariable("id") Long id) {
        return new ResponseVO(UserTransUtil.UserToExtend(userRepository.findById(id)));
    }

    /**
     * 更新用户信息
     * @param userExtend
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateUser(@RequestBody  UserExtend userExtend) {
        User user = userRepository.findById(userExtend.getId());
        if(user == null){
            throw new CustomException(CustomExceptionCode.UserNotExists);
        }
        Query query = new Query();
        query.addCriteria( Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("phone",userExtend.getPhone());
        update.set("nickname",userExtend.getNickname());
        update.set("sex",userExtend.getSex());
        update.set("status",userExtend.getStatus());
        update.set("editTime",new Date());
        userRepository.update(query,update);
        return new ResponseVO();
    }

    /**
     * 根据用户ID获取好友列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public ResponseVO getUserFriends(@RequestParam("id") Long id,
                                     @RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "length",defaultValue = "10") int length) {
        User user = userRepository.findById(id);
        if(user == null){
            throw new CustomException(CustomExceptionCode.UserNotExists);
        }
        List<Long> friends = user.getFriends();
        if(friends != null && friends.size() > 0){
            Query query = new Query();
            query.addCriteria(Criteria.where("friends").in(user.getId()));
            Pageable pageable = new PageRequest(page,length);
            Page<User> userPage = userRepository.find(query,pageable);
            List<UserSimple> userSimples = UserTransUtil.UsersToSimples(userPage.getContent());
            return new ResponseVO(new PageData<>(page,length,userSimples,userPage.getTotalPages(),userPage.getTotalElements()));
        }
        return new ResponseVO();
    }

    /**
     * 重置密码
     * @return
     */
    @RequestMapping(value = "/reset",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO resetPassword(@RequestBody User user){
        if(user == null){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long id = user.getId();
        String password = user.getPassword();
        if(Util.stringNull(password)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        User user1 = userRepository.findById(id);
        if(user1 == null){
            throw new CustomException(CustomExceptionCode.UserNotExists);
        }
        Query query = new Query();
        query.addCriteria( Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("password",bCryptPasswordEncoder.encode(password));
        update.set("editTime",new Date());
        userRepository.update(query,update);
        return new ResponseVO();
    }

}
