/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exitsoft.orm.core;

/**
 * 所有约束名称
 * 
 * @author vincent
 *
 */
public interface RestrictionNames {
	
	/**
	 * 等于查询（from Object o where o.property = ?）
	 */
	public static String EQ = "EQ";
	
	/**
	 * 非等于查询（from Object o where o.property <> ?）
	 */
	public static String NE = "NE";
	
	/**
	 * 大于等于查询（from Object o where o.property >= ?）
	 */
	public static String GE = "GE";
	
	/**
	 * 大于查询（from Object o where o.property > ?）
	 */
	public static String GT = "GT";
	
	/**
	 * 小于等于查询（from Object o where o.property <= ?）
	 */
	public static String LE = "LE";
	
	/**
	 * 小于查询（from Object o where o.property < ?）
	 */
	public static String LT = "LT";
	
	/**
	 * 包含查询（from Object o where o.property in(?,?,?)）
	 */
	public static String IN = "IN";
	
	/**
	 * 非包含查询（from Object o where o.property not in(?,?,?)）
	 */
	public static String NIN = "NIN";
	
	/**
	 * 左模糊查询（from Object o where o.property like %?）
	 */
	public static String LLIKE = "LLIKE";
	
	/**
	 * 右模糊查询（from Object o where o.property like ?%)
	 */
	public static String RLIKE= "RLIKE";
	
	/**
	 * 模糊查询（from Object o where o.property like %?%)
	 */
	public static String LIKE = "LIKE";
}
