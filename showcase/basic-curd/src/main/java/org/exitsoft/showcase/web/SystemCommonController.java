package org.exitsoft.showcase.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.bundle.BeanResourceBundle;
import org.exitsoft.common.spring.mvc.SpringMvcHolder;
import org.exitsoft.common.utils.CaptchaUtils;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.annotation.OperatingAudit;
import org.exitsoft.showcase.entity.account.User;
import org.exitsoft.showcase.service.account.AccountManager;
import org.exitsoft.showcase.service.account.CaptchaAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;


/**
 * 系统安全控制器
 * 
 * @author vincent
 *
 */
@Controller
@OperatingAudit("系统管理")
public class SystemCommonController {
	
	@Autowired
	private AccountManager accountManager;
	
	/**
	 * 空头像图片文件
	 */
	public final String EMPTY_PORTRAIT_PATH = "\\resource\\image\\empty.png";
	
	//上传文件存放的真实路径
	@Value("${file.upload.path}")
	private String fileUploadPath;
	
	/**
	 * 登录C，返回登录页面。当C发现当前用户已经登录名且认真后，会自动跳转到index页面
	 * 
	 * @return String
	 */
	@RequestMapping("/login")
	public String login() {
		
		if (!SystemVariableUtils.isAuthenticated()) {
			return "login";
		}
		return "redirect:/index";
	}

    /**
     * 默认进入首页的C
     */
    @RequestMapping("/index")
    public void index(){}
	
	/**
	 * 当前用户修改密码C.修改成功将会注销用户，重新登录
	 * 
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * 
	 * @return String
	 */
    @OperatingAudit(function="修改密码")
	@RequestMapping("/change-password")
	public String changePassword(String oldPassword,String newPassword) {
		
		accountManager.updateUserPassword(oldPassword,newPassword);
			
		return "redirect:/logout";
		
	}
	
	/**
	 * 修改用户头像C
	 * 
	 * @param request HttpServletRequest
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping("/change-portrait")
	public Map<String, Object> changePortrait(HttpServletRequest request) throws IOException {
		//获取当前用户
		User entity = SystemVariableUtils.getSessionVariable().getUser();
		
		Map<String, Object> result = Maps.newHashMap();
		
		//获取传进来的流
		InputStream is = request.getInputStream();
		//读取流内容到ByteArrayOutputStream中
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		
		bytestream.close();
		
		File uploadDirectory = new File(fileUploadPath);
		
		//如果没有创建上传文件夹就创建文件夹
		if (!uploadDirectory.exists() || !uploadDirectory.isDirectory()) {
			uploadDirectory.mkdirs();
		}
		
		entity.setPortrait(fileUploadPath + entity.getId());
		
		File portraitFile = new File(fileUploadPath + entity.getId());
		//如果当前用户没有创建头像，就创建头像
		if (!portraitFile.exists()) {
			portraitFile.createNewFile();
		}
		//拷贝到指定路径里
		FileUtils.writeByteArrayToFile(portraitFile, bytestream.toByteArray());
		accountManager.updateUser(entity);
		SystemVariableUtils.getSessionVariable().setUser(entity);
		//设置状态值，让FaustCplus再次触发jsfunc的js函数
		result.put("status","success");
		
		return result;
	}
	
	/**
	 * 修改个人信息C，修改成功将会重定向到主页
	 * 
	 * @param user 用户实体
	 * 
	 * @return String
	 * @throws IOException 
	 */
	@ResponseBody
    @OperatingAudit(function="改修个人信息")
	@SuppressWarnings("unchecked")
	@RequestMapping("/change-profile")
	public Map<String, Object> changeProfile(String realname,String email,@RequestParam(required = false)String portrait) throws IOException {
		//获取当前用户
		User entity = SystemVariableUtils.getSessionVariable().getUser();
		
		entity.setRealname(realname);
		entity.setEmail(email);
		
		accountManager.updateUser(entity);
		SystemVariableUtils.getSessionVariable().setUser(entity);
		
		return MapUtils.toMap(new BeanResourceBundle(entity,new String[]{"realname"}));
	}
	
	/**
	 * 生成验证码C
	 * 
	 * @throws IOException 
	 */
	@RequestMapping("/get-captcha")
	public ResponseEntity<byte[]> getCaptcha(HttpSession session) throws IOException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String captcha = CaptchaUtils.getCaptcha(80, 32, 5, outputStream).toLowerCase();
		
		session.setAttribute(CaptchaAuthenticationFilter.DEFAULT_CAPTCHA_PARAM,captcha);
		byte[] bs = outputStream.toByteArray();
		outputStream.close();
		return new ResponseEntity<byte[]>(bs,headers, HttpStatus.OK);
	}
	
	/**
	 * 获取当前用户头像C
	 * 
	 * @return {@link ResponseEntity}
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/get-current-user-portrait")
	public ResponseEntity<byte[]> getCurrentUserPortrait() throws IOException {
		
		String portrait = SystemVariableUtils.getSessionVariable().getUser().getPortrait();
		
		//如果头像为空，设置默认空头像
		if (StringUtils.isEmpty(portrait)) {
			portrait = SpringMvcHolder.getRealPath("") + EMPTY_PORTRAIT_PATH;
		}
		
		File f = new File(portrait);
		
		byte[] b = FileUtils.readFileToByteArray(f);
		
		return new ResponseEntity<byte[]>(b, HttpStatus.OK);
	}
	
	/**
	 * 没有权限C
	 */
	@RequestMapping("/unauthorized")
	public String unauthorized() {
		return "exception/unauthorized";
	}
}
