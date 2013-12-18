/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.exitsoft.common.type;

import java.util.Date;

/**
 * 属性数据类型
 * S代表String,I代表Integer,L代表Long, N代表Double, D代表Date,B代表Boolean
 * 
 * @author calvin
 * 
 */
public enum FieldType {
	
	/**
	 * String
	 */
	S(String.class),
	/**
	 * Integer
	 */
	I(Integer.class),
	/**
	 * Long
	 */
	L(Long.class),
	/**
	 * Double
	 */
	N(Double.class), 
	/**
	 * Date
	 */
	D(Date.class), 
	/**
	 * Boolean
	 */
	B(Boolean.class);

	//类型Class
	private Class<?> fieldClass;

	private FieldType(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}
	
	/**
	 * 获取类型Class
	 * 
	 * @return Class
	 */
	public Class<?> getValue() {
		return fieldClass;
	}
}
