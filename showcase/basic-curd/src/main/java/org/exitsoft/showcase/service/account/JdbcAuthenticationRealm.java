package org.exitsoft.showcase.service.account;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.exitsoft.showcase.common.SessionVariable;
import org.exitsoft.showcase.common.enumeration.entity.State;
import org.exitsoft.showcase.entity.account.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * apache shiro 的身份验证类
 * 
 * @author vincent
 *
 */
public class JdbcAuthenticationRealm extends AuthorizationRealm{
	
	@Autowired
	private AccountManager accountManager;

	/**
	 * 用户登录的身份验证方法
	 * 
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();
        
        if (username == null) {
            throw new AccountException("用户名不能为空");
        }
        
        User user = accountManager.getUserByUsername(username);
        
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        
        if (user.getState().equals(State.Disable.getValue())) {
        	 throw new DisabledAccountException("你的账户已被禁用,请联系管理员开通.");
        }
        
        SessionVariable model = new SessionVariable(user);
        
        return new SimpleAuthenticationInfo(model,user.getPassword(),getName());
	}
	

}
