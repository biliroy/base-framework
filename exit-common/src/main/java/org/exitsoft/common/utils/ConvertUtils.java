/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.exitsoft.common.utils;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateConverter;

/**
 * 类型转换工具类
 * 
 * @author calvin
 *
 */
public class ConvertUtils extends org.apache.commons.beanutils.ConvertUtils{
	
	static {
		registerDateConverter("yyyy-MM-dd");
	}
	
	/**
	 * 注册一个时间类型的转换器,当前默认的格式为：yyyy-MM-dd
	 * 
	 * @param patterns 日期格式
	 */
	public static void registerDateConverter(String... patterns) {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(patterns);
		register(dc, Date.class);
	}
	
	/**
	 * 基于Apache BeanUtils转换字符串到相应类型.
	 * 
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	public static Object convertToObject(String value, Class<?> toType) {
		try {
			return convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}
	
	/**
	 * 转换字符串数组到相应类型.
	 * 
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	public static Object convertToObject(String[] values,Class<?> toType) {
		try {
			return convert(values, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}
}
