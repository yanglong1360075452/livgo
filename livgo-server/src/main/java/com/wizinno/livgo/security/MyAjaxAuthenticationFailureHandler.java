package com.wizinno.livgo.security;

import com.google.gson.Gson;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MyAjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Gson gson = new Gson();
    private String defaultFailureUrl;
    private boolean forwardToDestination;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException exception)
            throws IOException, ServletException {

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

            ResponseVO responseVO = new ResponseVO(CustomExceptionCode.UsernameOrPasswordError, "用户名或密码错误");

            try {
                response.getOutputStream().write(gson.toJson(responseVO).toString().getBytes("UTF-8"));
            }catch (UnsupportedEncodingException e){
                logger.error(e.getMessage(), e);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }else if(this.defaultFailureUrl == null) {
                this.logger.debug("No failure URL set, sending 401 Unauthorized error");
                response.sendError(401, "Authentication Failed: " + exception.getMessage());
        } else {
            this.saveException(request, exception);
            if(this.forwardToDestination) {
                this.logger.debug("Forwarding to " + this.defaultFailureUrl);
                request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
            } else {
                this.logger.debug("Redirecting to " + this.defaultFailureUrl);
                super.getRedirectStrategy().sendRedirect(request, response, this.defaultFailureUrl);
            }
        }

    }

    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

    public boolean isForwardToDestination() {
        return forwardToDestination;
    }

    public void setForwardToDestination(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }
}
