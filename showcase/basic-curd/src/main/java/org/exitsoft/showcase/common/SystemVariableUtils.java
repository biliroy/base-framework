package org.exitsoft.showcase.common;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.common.enumeration.ValueEnum;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.exitsoft.showcase.service.foundation.SystemVariableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * 系统变量工具类
 * 
 * @author vincent
 *
 */
@Component
public class SystemVariableUtils {
	
	static public String DEFAULT_DICTIONARY_VALUE = "无";
	
	static private SystemVariableManager systemVariableManager;
	
	@Autowired
	public void setSystemVariableManager(SystemVariableManager systemDictionaryManager) {
		SystemVariableUtils.systemVariableManager = systemDictionaryManager;
	}
	
	/**
	 * 为了能够借助Spring自动注入systemDictionaryManager这个Bean.写一个空方法借助@PostConstruct注解注入
	 */
	@PostConstruct
	public void init() {
		
	}

	/**
	 * 获取数据字典名称
	 * 
	 * @param systemDictionaryCode 类别代码
	 * @param value 值
	 * 
	 * @return String
	 */
	public static String getName(SystemDictionaryCode systemDictionaryCode,Object value) {
		
		if (value == null || systemDictionaryCode == null) {
			return DEFAULT_DICTIONARY_VALUE;
		}
		
		if (value instanceof String && StringUtils.isEmpty(value.toString())) {
			return DEFAULT_DICTIONARY_VALUE;
		}
		
		List<DataDictionary> dataDictionaries = systemVariableManager.getDataDictionariesByCategoryCode(systemDictionaryCode);
		
		for (Iterator<DataDictionary> iterator = dataDictionaries.iterator(); iterator.hasNext();) {
			DataDictionary dataDictionary = iterator.next();
			
			if (StringUtils.equals(dataDictionary.getValue(), value.toString())) {
				return dataDictionary.getName();
			}
		}
		return DEFAULT_DICTIONARY_VALUE; 
	}
	
	/**
	 * 通过字典枚举获取字典名称
	 * 
	 * @param enumClass 字典枚举class
	 * @param value 值
	 * 
	 * @return String
	 */
	public static String getName(Class<? extends Enum<? extends ValueEnum<?>>> enumClass,Object value) {
		
		if (value == null || enumClass == null) {
			return DEFAULT_DICTIONARY_VALUE;
		}
		
		if (value instanceof String && StringUtils.isEmpty(value.toString())) {
			return DEFAULT_DICTIONARY_VALUE;
		}
	
		Enum<?>[] values = enumClass.getEnumConstants();
		
		for (Enum<?> o : values) {
			ValueEnum<?> ve = (ValueEnum<?>) o;
			
			if (StringUtils.equals(ve.getValue().toString(), value.toString())) {
				return ve.getName();
			}
			
		}
		
		return DEFAULT_DICTIONARY_VALUE;
	}

	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典类别
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	public static List<DataDictionary> getVariables(SystemDictionaryCode code, String... ignoreValue) {
		return systemVariableManager.getDataDictionariesByCategoryCode(code, ignoreValue);
	}
	
	/**
	 * 通过字典枚举获取数据字典集合
	 * 
	 * @param enumClass 字典枚举 class
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	public static List<DataDictionary> getVariables(Class<? extends Enum<? extends ValueEnum<?>>> enumClass, Object... ignoreValue) {
		List<DataDictionary> result = Lists.newArrayList();
		Enum<?>[] values = enumClass.getEnumConstants();
		
		for (Enum<?> o : values) {
			ValueEnum<?> ve = (ValueEnum<?>) o;
			Object value = ve.getValue();
			//判断是否该值的字段要忽略
			if(!ArrayUtils.contains(ignoreValue,value)) {
				String type = value.getClass().getSimpleName();
				if(type.equals("Date")) {
					type = "D";
				} else  if (type.equals("Double")) {
					type = "N";
				}
				result.add(new DataDictionary(ve.getName(),value.toString(),StringUtils.substring(type, 0, 1)));
			}
			
		}
		
		return result;
	}
	
	/**
	 * 获取当前安全模型
	 * 
	 * @return {@link SessionVariable}
	 */
	public static SessionVariable getSessionVariable() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.getPrincipal() != null && subject.getPrincipal() instanceof SessionVariable) {
			return (SessionVariable) subject.getPrincipal();
		}
		
		return null;
	}
	
	/**
	 * 判断当前会话是否登录
	 * 
	 * @return boolean
	 */
	public static boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}
	
}
