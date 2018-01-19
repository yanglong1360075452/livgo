package com.wizinno.livgo.security;

import com.google.gson.Gson;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyAjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private Gson gson = new Gson();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        if((request.getHeader("accept") != null && request.getHeader("accept").indexOf("application/json") > -1 ||
                ( request.getHeader("X-Requested-With") != null &&
                        request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1 ) )){//AJAX
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            long time = System.currentTimeMillis();
            response.setDateHeader("Last-Modified", time);
            response.setDateHeader("Date", time);
            response.setDateHeader("Expires", time);

            Map<String, Object> data = new HashMap<>();
            data.put("code", 0);
            data.put("data", "登录成功");

            try {
                response.getOutputStream().write(gson.toJson(data).toString().getBytes("UTF-8"));
            }catch (UnsupportedEncodingException e){
                logger.error(e.getMessage(), e);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }else if (!response.isCommitted()) {
            handle(request, response, authentication);
        }
        clearAuthenticationAttributes(request);
    }

}
