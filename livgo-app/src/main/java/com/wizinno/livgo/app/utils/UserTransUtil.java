package com.wizinno.livgo.app.utils;

import com.wizinno.livgo.app.data.user.UserBrief;
import com.wizinno.livgo.app.data.user.UserExtend;
import com.wizinno.livgo.app.data.user.UserSimple;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.DeviceRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.data.Sex;
import com.wizinno.livgo.data.UserStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-15.
 * <p>
 * user 数据转换类
 * <p>
 * 以下操作解决UserRepository无法注入情况
 * 具体原因待研究
 */
@Component
public class UserTransUtil {

    @Autowired
    private UserRepository userRepository;  //添加所需注入的私有成员

    @Autowired
    private DeviceRepository deviceRepository;
    private static UserTransUtil userTransUtil;  //  关键点1   静态初使化 一个工具类  这样是为了在spring初使化之前

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    @PostConstruct     //关键二   通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    public void init() {
        userTransUtil = this;
        userTransUtil.userRepository = this.userRepository;   // 初使化时将已静态化的userRepository实例化
        userTransUtil.deviceRepository = this.deviceRepository;
    }

    public static UserExtend UserToExtend(User user) {
        if (user != null) {
            UserExtend userExtend = new UserExtend();
            BeanUtils.copyProperties(user, userExtend);
            userExtend.setStatusDesc(UserStatus.getNameByCode(user.getStatus()));
            userExtend.setSexDesc(Sex.getNameByCode(user.getSex()));
            List<Long> friends = user.getFriends();
            if (friends != null && friends.size() > 0) {
                List<UserBrief> friendsBack = new ArrayList<>();
                User fri = null;
                for (Long friendId : friends) {
                    User full = userTransUtil.userRepository.findById(friendId);
                    friendsBack.add(UserToBrief(full));
                }
                userExtend.setFriends(friendsBack);
            }
//            Device device = userTransUtil.deviceRepository.findByUser(user);
//            if(device != null){
//                List<Device> devices = new ArrayList<>();
//                devices.add(device);
//                userExtend.setDevices(devices);
//            }
            return userExtend;
        } else {
            return null;
        }
    }

    public static List<UserExtend> UsersToExtends(List<User> users) {
        if (users != null && users.size() > 0) {
            List<UserExtend> userExtends = new ArrayList<>();
            for (User user : users) {
                userExtends.add(UserToExtend(user));
            }
            return userExtends;
        } else {
            return null;
        }
    }

    public static UserSimple UserToSimple(User user) {
        if (user != null) {
            UserSimple userSimple = new UserSimple();
            BeanUtils.copyProperties(user, userSimple);
            userSimple.setStatusDesc(UserStatus.getNameByCode(user.getStatus()));
            userSimple.setSexDesc(Sex.getNameByCode(user.getSex()));
            return userSimple;
        } else {
            return null;
        }
    }

    public static UserBrief UserToBrief(User user) {
        if (user != null) {
            UserBrief userBrief = new UserBrief();
            userBrief.setUsername(user.getUsername());
            userBrief.setId(user.getId());
            userBrief.setNickname(user.getNickname());
            return userBrief;
        } else {
            return null;
        }
    }

    public static List<UserSimple> UsersToSimples(List<User> users) {
        if (users != null && users.size() > 0) {
            List<UserSimple> userSimples = new ArrayList<>();
            for (User user : users) {
                userSimples.add(UserToSimple(user));
            }
            return userSimples;
        } else {
            return null;
        }
    }
}
