package com.wizinno.livgo.controller;

import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseController {

    private Logger log = Logger.getLogger(BaseController.class);

    @ExceptionHandler(CustomException.class)
    public Map exception(HttpServletRequest request, Exception e) {
        Map response = new HashMap();
        if (e instanceof CustomException) {
            CustomException exception= (CustomException) e;
            response.put("code", exception.getCode());
            response.put("reason", CustomExceptionCode.getReasonByCode(exception.getCode(), "错误码未定义"));
            log.debug(e.getMessage(), e);
        }else if(e instanceof AccessDeniedException){
            response.put("code", 1);
            response.put("reason", "权限不足");
            log.warn(e.getMessage(), e);
        }else if(e instanceof HttpClientErrorException){
            log.warn(e.getMessage(), e);
        }else{
            response.put("code", -1);
            response.put("reason", "服务器内部错误");
            response.put("errorMessage",e.getMessage());
            log.error(e.getMessage(), e);
        }
        return response;
    }
}
