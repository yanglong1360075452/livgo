package com.wizinno.livgo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * 自定义的filter，必须包含authenticationManager,accessDecisionManager,
 * securityMetadataSource三个属性
 */
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
	private static Logger logger = LoggerFactory.getLogger(MyFilterSecurityInterceptor.class);

	@Autowired
	private MyInvocationSecurityMetadataSource myInvocationSecurityMetadataSource;
	
	@Autowired
	private MyAccessDecisionManager myAccessDecisionManager;
	
	@Autowired
	@Qualifier("myAuthenticationManager")
	private AuthenticationManager authenticationManager;
	
	@PostConstruct
	public void init(){
		super.setAuthenticationManager(authenticationManager);
		super.setAccessDecisionManager(myAccessDecisionManager);
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		FilterInvocation fi = new FilterInvocation(request, response, chain);

		logger.debug("requestURL:" + fi.getRequestUrl());
		
		//在执行doFilter之前，进行权限的检查		
        InterceptorStatusToken statusToken = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(statusToken, null);
        }
	}

	
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	
	public void destroy() {
	}

	
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.myInvocationSecurityMetadataSource;
	}

	public MyInvocationSecurityMetadataSource getMyInvocationSecurityMetadataSource() {
		return myInvocationSecurityMetadataSource;
	}

	public void setMyInvocationSecurityMetadataSource(MyInvocationSecurityMetadataSource myInvocationSecurityMetadataSource) {
		this.myInvocationSecurityMetadataSource = myInvocationSecurityMetadataSource;
	}
}
