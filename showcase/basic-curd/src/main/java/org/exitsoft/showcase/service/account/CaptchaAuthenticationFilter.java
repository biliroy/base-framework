package org.exitsoft.showcase.service.account;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

/**
 * 验证码登录认证Filter
 * 
 * @author vincent
 *
 */
@Component
public class CaptchaAuthenticationFilter extends FormAuthenticationFilter{
	
	/**
	 * 默认验证码参数名称
	 */
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	
	/**
	 * 登录次数超出allowLoginNum时，存储在session记录是否展示验证码的key默认名称
	 */
	public static final String DEFAULT_SHOW_CAPTCHA_KEY_ATTRIBUTE = "showCaptcha";
	
	/**
	 * 默认在session中存储的登录次数名称
	 */
	private static final String DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE = "loginNum";
	//验证码参数名称
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    //在session中的存储验证码的key名称
    private String sessionCaptchaKeyAttribute = DEFAULT_CAPTCHA_PARAM;
    //在session中存储的登录次数名称
    private String loginNumKeyAttribute = DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE;
    //登录次数超出allowLoginNum时，存储在session记录是否展示验证码的key名称
    private String sessionShowCaptchaKeyAttribute = DEFAULT_SHOW_CAPTCHA_KEY_ATTRIBUTE;
    //允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
    private Integer allowLoginNum = 1;
    
    /**
     * 重写父类方法，在shiro执行登录时先对比验证码，正确后在登录，否则直接登录失败
     */
	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		
		Session session = getSubject(request, response).getSession();
		//获取登录次数
		Integer number = (Integer) session.getAttribute(getLoginNumKeyAttribute());
		
		//首次登录，将该数量记录在session中
		if (number == null) {
			number = new Integer(1);
			session.setAttribute(getLoginNumKeyAttribute(), number);
		}
		
		//如果登录次数大于allowLoginNum，需要判断验证码是否一致
		if (number > getAllowLoginNum()) {
			//获取当前验证码
			String currentCaptcha = (String) session.getAttribute(getSessionCaptchaKeyAttribute());
			//获取用户输入的验证码
			String submitCaptcha = getCaptcha(request);
			//如果验证码不匹配，登录失败
			if (StringUtils.isEmpty(submitCaptcha) || !StringUtils.equals(currentCaptcha,submitCaptcha.toLowerCase())) {
				return onLoginFailure(this.createToken(request, response), new AccountException("验证码不正确"), request, response);
			}
		
		}
		
		return super.executeLogin(request, response);
	}

	/**
	 * 设置验证码提交的参数名称
	 * 
	 * @param captchaParam 验证码提交的参数名称
	 */
	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	/**
	 * 获取验证码提交的参数名称
	 * 
	 * @return String
	 */
	public String getCaptchaParam() {
		return captchaParam;
	}

	/**
	 * 设置在session中的存储验证码的key名称
	 * 
	 * @param sessionCaptchaKeyAttribute 存储验证码的key名称
	 */
	public void setSessionCaptchaKeyAttribute(String sessionCaptchaKeyAttribute) {
		this.sessionCaptchaKeyAttribute = sessionCaptchaKeyAttribute;
	}
	
	/**
	 * 获取设置在session中的存储验证码的key名称
	 * 
	 * @return Sting
	 */
	public String getSessionCaptchaKeyAttribute() {
		return sessionCaptchaKeyAttribute;
	}

	/**
	 * 获取在session中存储的登录次数名称
	 * 
	 * @return Stromg
	 */
	public String getLoginNumKeyAttribute() {
		return loginNumKeyAttribute;
	}

	/**
	 * 设置在session中存储的登录次数名称
	 * 
	 * @param loginNumKeyAttribute 登录次数名称
	 */
	public void setLoginNumKeyAttribute(String loginNumKeyAttribute) {
		this.loginNumKeyAttribute = loginNumKeyAttribute;
	}

	/**
	 * 获取用户输入的验证码
	 * 
	 * @param request ServletRequest
	 * 
	 * @return String
	 */
	public String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}
	
	/**
	 * 获取登录次数超出allowLoginNum时，存储在session记录是否展示验证码的key名称
	 * 
	 * @return String
	 */
	public String getSessionShowCaptchaKeyAttribute() {
		return sessionShowCaptchaKeyAttribute;
	}
	
	/**
	 * 设置登录次数超出allowLoginNum时，存储在session记录是否展示验证码的key名称
	 * 
	 * @param sessionShowCaptchaKeyAttribute 是否展示验证码的key名称
	 */
	public void setSessionShowCaptchaKeyAttribute(String sessionShowCaptchaKeyAttribute) {
		this.sessionShowCaptchaKeyAttribute = sessionShowCaptchaKeyAttribute;
	}

	/**
	 * 获取允许登录次数
	 * 
	 * @return Integer
	 */
	public Integer getAllowLoginNum() {
		return allowLoginNum;
	}

	/**
	 * 设置允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
	 * 
	 * @param allowLoginNum 允许登录次数
	 */
	public void setAllowLoginNum(Integer allowLoginNum) {
		this.allowLoginNum = allowLoginNum;
	}

	/**
	 * 重写父类方法，当登录失败将异常信息设置到request的attribute中
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request,AuthenticationException ae) {
		if (ae instanceof IncorrectCredentialsException) {
			request.setAttribute(getFailureKeyAttribute(), "用户名密码不正确");
		} else {
			request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
		}
	}
	
	/**
	 * 重写父类方法，当登录失败次数大于allowLoginNum（允许登录次）时，将显示验证码
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,ServletResponse response) {
		Session session = getSubject(request, response).getSession(false);
		
		Integer number = (Integer) session.getAttribute(getLoginNumKeyAttribute());
		
		//如果失败登录次数大于allowLoginNum时，展示验证码
		if (number > getAllowLoginNum() - 1) {
			session.setAttribute(getSessionShowCaptchaKeyAttribute(), true);
			session.setAttribute(getLoginNumKeyAttribute(), ++number);
		}
		
		session.setAttribute(getLoginNumKeyAttribute(),++number);
		
		return super.onLoginFailure(token, e, request, response);
	}
	
	/**
	 * 重写父类方法，当登录成功后，将allowLoginNum（允许登录次）设置为0，重置下一次登录的状态
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		Session session = subject.getSession(false);
		
		session.removeAttribute(getLoginNumKeyAttribute());
		session.removeAttribute(getSessionShowCaptchaKeyAttribute());

		session.setAttribute("sv", subject.getPrincipal());
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 重写父类方法，创建一个自定义的{@link UsernamePasswordTokeExtend}
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		
		String username = getUsername(request);
	    String password = getPassword(request);
        String host = getHost(request);

	    boolean rememberMe = false;
	    String rememberMeValue = request.getParameter(getRememberMeParam());
	    Integer rememberMeCookieValue = null;
	    //如果提交的rememberMe参数存在值,将rememberMe设置成true
	    if(StringUtils.isNotEmpty(rememberMeValue)) {
	    	rememberMe = true;
	    	
	    	rememberMeCookieValue = NumberUtils.toInt(rememberMeValue);
	    }
	    
		return new UsernamePasswordTokeExtend(username, password, rememberMe, host,rememberMeCookieValue);
	}
	
	/**
	 * UsernamePasswordToke扩展，添加一个rememberMeValue字段，获取提交上来的rememberMe值
	 * 根据该rememberMe值去设置Cookie的有效时间。
	 * 
	 * @author vincent
	 *
	 */
	@SuppressWarnings("serial")
	protected class UsernamePasswordTokeExtend extends UsernamePasswordToken {
		
		//rememberMe cookie的有效时间
		private Integer rememberMeCookieValue;
		
		public UsernamePasswordTokeExtend() {
			
		}
		
		public UsernamePasswordTokeExtend(String username,String password,boolean rememberMe, String host,Integer rememberMeCookieValue) {
			super(username, password, rememberMe, host);
			this.rememberMeCookieValue = rememberMeCookieValue;
		}

		/**
		 * 获取rememberMe cookie的有效时间
		 * 
		 * @return Integer
		 */
		public Integer getRememberMeCookieValue() {
			return rememberMeCookieValue;
		}

		/**
		 * 设置rememberMe cookie的有效时间
		 * 
		 * @param rememberMeCookieValue cookie的有效时间
		 */
		public void setRememberMeCookieValue(Integer rememberMeCookieValue) {
			this.rememberMeCookieValue = rememberMeCookieValue;
		}
		
		
	}
}