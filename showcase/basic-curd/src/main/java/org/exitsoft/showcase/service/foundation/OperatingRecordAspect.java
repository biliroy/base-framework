package org.exitsoft.showcase.service.foundation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.exitsoft.common.spring.mvc.SpringMvcHolder;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.showcase.common.SessionVariable;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.annotation.OperatingAudit;
import org.exitsoft.showcase.common.enumeration.entity.OperatingState;
import org.exitsoft.showcase.entity.foundation.audit.OperatingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 操作记录Aspect,当执行某个Controller时会判断该Controller是否存在OperatingAudit
 * 注解，如果存在表示该Controller需要执行操作记录，当执行完成后，会在TB_OPERATING_RECORD
 * 表中插入一条数据，当某个Controller需要做操作记录但又重定向到另外一个也是需要做操作记录
 * 时，会在TB_OPERATING_RECORDD表中插入两条数据，以此类推。
 * 
 * @author vincent
 *
 */
@Aspect
@Component
public class OperatingRecordAspect {
	
	
	@Autowired
	private SystemAuditManager systemAuditManager;
	
	/**
	 * 在带有OperatingAudit注解的方法中监控执行过程
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(org.exitsoft.showcase.common.annotation.OperatingAudit)")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		
		Method method = ((MethodSignature)point.getSignature()).getMethod();
		
		OperatingAudit audit = ReflectionUtils.getAnnotation(method, OperatingAudit.class);
		
		OperatingRecord record = new OperatingRecord();
		record.setStartDate(new Date());
		record.setMethod(method.getName());

		HttpServletRequest request = SpringMvcHolder.getRequest();
		StringBuffer sb = new StringBuffer();
		
		if(request != null) {
			record.setIp(getIpAddress(request));
			record.setOperatingTarget(request.getRequestURI());
			//获取本次提交的参数
			Map<String, Object> parameter = request.getParameterMap();
			
			if (parameter.size() > 0) {

				sb.append("<h2>request参数</h2>").append("<hr>");
				//逐个循环参数和值添加到操作记录参数的描述字段中
				for(Entry<String, Object> entry : parameter.entrySet()) {
					sb.append("<p>").
					   append(entry.getKey()).
					   append(":").
					   append("<strong class='text-info'>").
					   append(getParameterValue(entry.getValue())).
					   append("</strong>").
					   append("</p>");
				}
				
				sb.append("<hr>");
			}
		}
		
		//获取当前SessionVariable，通过该变量获取当前用户
		SessionVariable sessionVariable = SystemVariableUtils.getSessionVariable();
		//如果SessionVariable等于null，表示用户未登录，但一样要记录操作
		if (sessionVariable != null) {
			record.setFkUserId(sessionVariable.getUser().getId());
			record.setUsername(sessionVariable.getUser().getUsername());
		}
		
		String function = audit.function();
		String module = "";
		
		if (StringUtils.isEmpty(audit.value())) {
			OperatingAudit classAnnotation = ReflectionUtils.getAnnotation(method.getDeclaringClass(),OperatingAudit.class);
			module = classAnnotation == null ? "" : classAnnotation.value();
		}
		
		record.setFunction(function);
		record.setModule(module);
		
		Object reaultValue = null;
		
		try {
			reaultValue = point.proceed();
			record.setState(OperatingState.Success.getValue());
		} catch (Throwable e) {
			record.setState(OperatingState.Fail.getValue());
			
			StringWriter outputStream = new StringWriter();
			PrintWriter printWriter = new PrintWriter(outputStream);
			
			e.printStackTrace(printWriter);
			
			String message = StringUtils.replace(outputStream.toString(), ">", "&gt;");
			message = StringUtils.replace(message, "<", "&lt;");
			message = StringUtils.replace(message, "\r\n", "<br>");
			
			sb.append("<h2>异常信息</h2>").append("<hr>").append(message).append("<hr>");
			request.setAttribute("record", record);
			throw e;
		} finally {
			record.setEndDate(new Date());
			record.setRemark(sb.toString());
			systemAuditManager.insertOperatingRecord(record);
		}
		
		return reaultValue;
		
	}
	
	/**
	 * 通过Request 获取ip
	 * 
	 * @param request http servlet request
	 * 
	 * @return String
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.trim();
	}
	
	/**
	 * 辅助方法，获取参数的真实值
	 * 
	 * @param value 参数值
	 * 
	 * @return String
	 */
	private String getParameterValue(Object value) {
		//如果该值为数组，将值用逗号分割
		if (value.getClass().isArray()) {
			return StringUtils.join((Object[])value,",");
		} else {
			return ConvertUtils.convert(value);
		}
	}
}
