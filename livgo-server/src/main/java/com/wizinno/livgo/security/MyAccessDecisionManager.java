package com.wizinno.livgo.security;

import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * 访问决策器，决定某个用户（具有的角色）是否有足够的权限去访问某个资源
 */
public class MyAccessDecisionManager implements AccessDecisionManager {

	private boolean allowIfAllAbstainDecisions = false;  
    
    public boolean isAllowIfAllAbstainDecisions() {  
        return allowIfAllAbstainDecisions;  
    }  
  
    public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {  
        this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;  
    }
    
    protected final void checkAllowIfAllAbstainDecisions() {  
        if (!this.isAllowIfAllAbstainDecisions()) {
            throw new AccessDeniedException(CustomExceptionCode.AccessDenied.toString());
        }
    } 
    
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
		if(configAttributes == null){
            return ;
        }
		
        Iterator<ConfigAttribute> ite=configAttributes.iterator();
        while(ite.hasNext()){
            ConfigAttribute ca=ite.next();
            String needRole= ca.getAttribute();
			for (GrantedAuthority ga : authentication.getAuthorities()) {
                if(needRole.equals(ga.getAuthority())){
                    return;
                }
            }
        }
        checkAllowIfAllAbstainDecisions();  
	}

	@Override
	public boolean supports(ConfigAttribute configAttributes) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
