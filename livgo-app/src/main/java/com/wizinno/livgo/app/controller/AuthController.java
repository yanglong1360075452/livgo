package com.wizinno.livgo.app.controller;

import com.wizinno.livgo.app.config.Constant;
import com.wizinno.livgo.app.data.user.UserExtend;
import com.wizinno.livgo.app.document.Log;
import com.wizinno.livgo.app.document.User;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.tencent.util.SigUtil;
import com.wizinno.livgo.app.utils.JwtUtil;
import com.wizinno.livgo.app.utils.UserTransUtil;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.controller.BaseController;
import com.wizinno.livgo.data.LogOperation;
import com.wizinno.livgo.data.LogType;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.data.UserStatus;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * Created by LiuMei on 2017-05-03.
 */
@RestController
@RequestMapping("/api/app")
public class AuthController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    /**
     * 登录
     *
     * @param username 用户名或电话
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO authenticateUser(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("appVersions") String appVersions) {
        if(Util.stringNull(username) || Util.stringNull(password)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        User user = userRepository.findByUsernameOrPhone(username, username);

        if (user == null) {
            throw new CustomException(CustomExceptionCode.UserNotExists);
        }
        Integer status = user.getStatus();
        if(!status.equals(UserStatus.active.toCode())){
            throw new CustomException(CustomExceptionCode.StatusError);
        }
        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new CustomException(CustomExceptionCode.WrongPassword);
        }
//        try {
            String subject = JwtUtil.generalSubject(user);
        String token = null;
        try {
            token = jwtUtil.createJWT(Constant.JWT_ID, subject, Constant.JWT_TTL);
        } catch (Exception e) {
            e.printStackTrace();
        }

            UserExtend userExtend = UserTransUtil.UserToExtend(user);
            userExtend.setToken(token);
//            userExtend.setUserSig(SigUtil.generateSig(userId));

            return new ResponseVO(userExtend);
//        } catch (Exception e) {
//            logger.error("登录失败:" + e.getMessage());
//            throw new CustomException(CustomExceptionCode.AuthFailed);
//        }
    }
    /**
     * 登出
     *
     * @param   appVersions app版本
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO logout(@RequestParam("appVersions") String appVersions){
        if(Util.stringNull(appVersions)){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        long userid= (long) request.getAttribute("userId");
        return new ResponseVO(0);

    }

   /* public static void main(String[] args)  throws Exception {
        tls_sigcheck demo = new tls_sigcheck();

        // 使用前请修改动态库的加载路径
        File FileDll = ResourceUtils.getFile("classpath:jnisigcheck.dll");
        String s1 = FileDll.toString();
        demo.loadJniLib(s1);
        //demo.loadJniLib("C:\\Windows\\System32\\jnisigcheck.so");

        // File priKeyFile = new File("private_key");
        File priKeyFile = ResourceUtils.getFile("classpath:private_key");
        StringBuilder strBuilder = new StringBuilder();
        String s = "";

        BufferedReader br = new BufferedReader(new FileReader(priKeyFile));
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String priKey = strBuilder.toString();
        int ret = demo.tls_gen_signature_ex2("1400033525", "sig_"+1, priKey);

        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("sig:\n" + demo.getSig());
        }

        //File pubKeyFile = new File("public.pem");
        File pubKeyFile = ResourceUtils.getFile("classpath:public_key");
        br = new BufferedReader(new FileReader(pubKeyFile));
        strBuilder.setLength(0);
        while ((s = br.readLine()) != null) {
            strBuilder.append(s + '\n');
        }
        br.close();
        String pubKey = strBuilder.toString();
        ret = demo.tls_check_signature_ex2(demo.getSig(), pubKey, "1400033525", "sig_"+1);
        if (0 != ret) {
            System.out.println("ret " + ret + " " + demo.getErrMsg());
        }
        else
        {
            System.out.println("--\nverify ok -- expire time " + demo.getExpireTime() + " -- init time " + demo.getInitTime());
        }
    }*/
}
