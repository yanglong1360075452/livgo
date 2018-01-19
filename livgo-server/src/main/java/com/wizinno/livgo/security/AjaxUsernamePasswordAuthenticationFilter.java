package com.wizinno.livgo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


public class AjaxUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static Logger log = LoggerFactory.getLogger(AjaxUsernamePasswordAuthenticationFilter.class);

	private String username = "username";
	
	private String password = "password";
	
	/**
	 * 是否只接收POST方式提交的验证数据
	 */
	private boolean postOnly = true;
	
	/**
	 * 是否需要验证码
	 */
	private boolean checkValidateCode = false;
	
	/**
	 * 验证成功，跳转的页面
	 * 注意：地址必须是 / 或 http 开头的URL地址
	 */
	private AuthenticationSuccessHandler successHandler;

	private AuthenticationFailureHandler failureHandler;

	private MyRequestMatcher myRequestMatcher;

	/**
	 * 记住用户的实现
	 */
	private RememberMeServices rememberMeServices;
	
	public void init(){
		//配置接收参数的表单名称，默认是 j_username 和 j_password
		//可以在这里手工指定，也可以在Spring配置中注入属性
		this.setUsername(username);
		this.setPassword(password);
		super.setRequiresAuthenticationRequestMatcher(myRequestMatcher);
		
		//验证成功，跳转的页面
		this.setAuthenticationSuccessHandler(successHandler);

		this.setAuthenticationFailureHandler(failureHandler);
	}
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
		
        if (postOnly && !request.getMethod().equals("POST")) {
        	//这里可以直接抛出异常，也可以直接跳转
			log.error("Authentication method not supported: " + request.getMethod());
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);
		try{
			username=new String(username.getBytes("iso-8859-1"), "UTF-8");
		}catch (UnsupportedEncodingException ex){

		}
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        
        return this.getAuthenticationManager().authenticate(authRequest);
    }

	public boolean isPostOnly() {
		return postOnly;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
	
	@Override
	protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(username);
    }
	
	@Override
	protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(password);
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String usernameParameter) {
		this.username = usernameParameter;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String passwordParameter) {
		this.password = passwordParameter;
	}

	public boolean isCheckValidateCode() {
		return checkValidateCode;
	}

	public void setCheckValidateCode(boolean checkValidateCode) {
		this.checkValidateCode = checkValidateCode;
	}

	@Override
	public AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}

	@Override
	public AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}

	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	@Override
	public RememberMeServices getRememberMeServices() {
		return rememberMeServices;
	}

	@Override
	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}

	public MyRequestMatcher getMyRequestMatcher() {
		return myRequestMatcher;
	}

	public void setMyRequestMatcher(MyRequestMatcher myRequestMatcher) {
		this.myRequestMatcher = myRequestMatcher;
	}
}
