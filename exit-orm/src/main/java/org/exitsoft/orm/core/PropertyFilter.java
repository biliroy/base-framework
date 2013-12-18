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

import java.io.Serializable;

import org.exitsoft.common.type.FieldType;

/**
 * orm属性过滤器，可以通过{@link PropertyFilters#build(String, String)}创建，使用他创建可以直接
 * 写入表达式即可，相关表达式样式查看{@link org.exitsoft.orm.core.hibernate.CriterionBuilder}的实现类,如果直接创建查看{@link org.exitsoft.orm.core.hibernate.CriterionBuilder}
 * 实现类的实际restrictionName值和{@link FieldType}枚举值,如果一个属性对比多个值可以使用逗号(,)分割
 *
 * <p>
 * 	例子:
 * </p>
 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName"},"a,b,c");<code>
 * <p>当使用以上filter到{@link org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where propertyName = 'a' and propertyName = 'b' and propertyName = 'c'</p>
 *
 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a");<code>
 * <p>当使用以上filter到{@link  org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where propertyName1 = 'a' or propertyName2 = 'a'</p>
 *
 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a,b");<code>
 * <p>当使用以上filter到{@link  org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where (propertyName1 = 'a' or propertyName1 = 'b') and (propertyName2 = 'a' or propertyName2 = 'b')</p>
 * 
 * @see PropertyFilters#build(String, String)
 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder
 * 
 * @author vincent
 */
@SuppressWarnings("serial")
public class PropertyFilter implements Serializable{
	
	//约束名称
	private String restrictionName;
	
	//属性类型
	private Class<?> FieldType;
	
	//属性名称
	private String[] propertyNames;
	
	//对比值
	private String matchValue;
	
	public PropertyFilter() {
		
	}
	
	/**
	 * 构造方法，可以通过{@link PropertyFilters#build(String, String)}创建，使用他创建可以直接
	 * 写入表达式即可，相关表达式样式查看{@link org.exitsoft.orm.core.hibernate.CriterionBuilder}的实现类,如果直接创建查看{@link org.exitsoft.orm.core.hibernate.CriterionBuilder}
	 * 实现类的实际restrictionName值和{@link FieldType}枚举值,如果一个属性对比多个值可以使用逗号(,)分割
	 * 
	 * <p>
	 * 	例子:
	 * </p>
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName"},"a,b,c");<code> 
	 * <p>当使用以上filter到{@link org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where propertyName = 'a' and propertyName = 'b' and propertyName = 'c'</p>
	 * 
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a");<code> 
	 * <p>当使用以上filter到{@link  org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where propertyName1 = 'a' or propertyName2 = 'a'</p>
	 * 
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",FieldType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a,b");<code> 
	 * <p>当使用以上filter到{@link  org.exitsoft.orm.core.hibernate.support.HibernateSupportDao#findUniqueByPropertyFilter(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where (propertyName1 = 'a' or propertyName1 = 'b') and (propertyName2 = 'a' or propertyName2 = 'b')</p>
	 * 
	 * @param restrictionName 约束名称
	 * @param FieldType 属性类型
	 * @param propertyNames 属性名称
	 * @param matchValue 对比值
	 */
	public PropertyFilter(String restrictionName, FieldType FieldType,String[] propertyNames,String matchValue) {
		super();
		this.restrictionName = restrictionName;
		this.FieldType = FieldType.getValue();
		this.propertyNames = propertyNames;
		this.matchValue = matchValue;
	}

	/**
	 * 获取约束名称
	 * 
	 * @return String
	 */
	public String getRestrictionName() {
		return restrictionName;
	}

	/**
	 * 设置约束名称
	 * 
	 * @param restrictionName 约束名称
	 */
	public void setRestrictionName(String restrictionName) {
		this.restrictionName = restrictionName;
	}

	/**
	 * 获取属性类型
	 * 
	 * @return Class
	 */
	public Class<?> getFieldType() {
		return FieldType;
	}

	/**
	 * 设置属性类型
	 * 
	 * @param FieldType 属性类型
	 */
	public void setFieldType(Class<?> FieldType) {
		this.FieldType = FieldType;
	}
	
	/**
	 * 获取属性名称
	 * 
	 * @return String[]
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * 设置属性名称
	 * 
	 * @param propertyNames 属性名称
	 */
	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	/**
	 * 获取对比值
	 * 
	 * @return String
	 */
	public String getMatchValue() {
		return matchValue;
	}

	/**
	 * 设置对比值
	 * 
	 * @param matchValue 对比值
	 */
	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}
	
	/**
	 * 是否存在多个属性，如果是返回true,否则返回false
	 * 
	 * @return boolean
	 */
	public boolean hasMultiplePropertyNames() {
		return this.propertyNames != null && this.propertyNames.length > 1;
	}
	
	/**
	 * 获取单个属性名称
	 * 
	 * @return String
	 */
	public String getSinglePropertyName() {
		if (hasMultiplePropertyNames()) {
			throw new IllegalAccessError("PropertyFilter中存在多个propertyName:" + this.propertyNames);
		}
		return this.propertyNames[0];
	}
	
	
}
