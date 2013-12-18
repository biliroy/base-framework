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
package org.exitsoft.common.bundle;

import java.beans.PropertyDescriptor;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.springframework.beans.BeanUtils;

/**
 * 辅助类，将对象转换为map对象,以对象的属性名称做key，属性值做value构造map对象，
 * 主要目的用在{@link MapUtils#toMap(ResourceBundle)}将对象转换为map时使用。
 * 
 * @author vincent
 *
 */
public class BeanResourceBundle extends ResourceBundle{
	
	//需要转换成map的bean对象
	private Object bean;
	//仅仅引入bean的某些属性
	private String[] include;
	//要忽略bean的某些属性
	private String[] exclude;
	//是否忽略null值的属性
	private boolean ignoreNullValue;
	
	/**
	 * 辅助类，将对象转换为map对象,以对象的属性名称做key，属性值做value构造map对象，
	 * 主要目的用在{@link MapUtils#toMap(ResourceBundle)}将对象转换为map时使用。
	 * 
	 * @param bean 要转换为map的bean对象
	 */
	public BeanResourceBundle(Object bean) {
		this(bean,null);
	}
	
	/**
	 * 辅助类，将对象转换为map对象,以对象的属性名称做key，属性值做value构造map对象，
	 * 主要目的用在{@link MapUtils#toMap(ResourceBundle)}将对象转换为map时使用。
	 * 
	 * @param bean 要转换为map的bean对象
	 * @param include 仅仅将bean的某些属性转换成map对象
	 * 
	 */
	public BeanResourceBundle(Object bean,String[] include) {
		this(bean,include,null);
	}
	
	/**
	 * 辅助类，将对象转换为map对象,以对象的属性名称做key，属性值做value构造map对象，
	 * 主要目的用在{@link MapUtils#toMap(ResourceBundle)}将对象转换为map时使用。
	 * 
	 * @param bean 要转换为map的bean对象
	 * @param include 仅仅将bean的某些属性转换成map对象
	 * @param exclude bean的哪些属性不转换为map
	 */
	public BeanResourceBundle(Object bean,String[] include,String[] exclude) {
		this(bean,include,exclude,true);
	}
	
	/**
	 * 辅助类，将对象转换为map对象,以对象的属性名称做key，属性值做value构造map对象，
	 * 主要目的用在{@link MapUtils#toMap(ResourceBundle)}将对象转换为map时使用。
	 * 
	 * @param bean 要转换为map的bean对象
	 * @param include 仅仅将bean的某些属性转换成map对象
	 * @param exclude bean的哪些属性不转换为map
	 * @param ignoreNullValue 是否忽略nul值的属性
	 */
	public BeanResourceBundle(Object bean,String[] include,String[] exclude,boolean ignoreNullValue) {
		this.bean = bean;
		this.include = include;
		this.exclude = exclude;
		this.ignoreNullValue = ignoreNullValue;
	}

	/**
	 * 重写父类方法，通过key名称获取bean的值
	 */
	@Override
	protected Object handleGetObject(String key) {
		Object result = ReflectionUtils.invokeGetterMethod(bean, key);
		return  result == null ? "" : result;
	}

	/**
	 * 重写父类方法，将bean对象转换为map的key
	 */
	@Override
	public Enumeration<String> getKeys() {
		
		Vector<String> vector = new Vector<String>();
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
		//逐个遍历bean的属性
		for (PropertyDescriptor pd : propertyDescriptors) {
			//如果该属性没有get/set方法，将不做转换
			if ((pd.getWriteMethod() == null) || pd.getReadMethod() == null) {
				continue;
			}
			
			String key = pd.getName();
			
			/*
			 * 判断该属性是否能够添加到map中，判断条件如下:
			 * 1.该属性必须存在include属性中
			 * 2.该属性不能存在exclude属性中
			 * 3.如果ignoreNullValue为true,该属性的值不能为null值
			 */
			if (isIncludeProperty(key) && !isExcludeProperty(key)) {
				
				if (ignoreNullValue && ReflectionUtils.invokeGetterMethod(bean, key) == null) {
					continue;
				}
				
				vector.addElement(key);
			}
		}
		
		//返回能够转换为map的key值
		return vector.elements();
	}
	
	/**
	 * 通过属性名称判断是否该属性为引入属性，返回true表示是,false表示不是
	 * 
	 * @param name 属性名称
	 * 
	 * @return boolean
	 */
	private boolean isIncludeProperty(String name) {
		return include != null && include.length > 0 ? ArrayUtils.contains(include, name) : true;
	}
	
	/**
	 * 通过属性名称判断是否该属性为忽略属性，返回true表示是,false表示不是
	 * 
	 * @param name 属性名称
	 * 
	 * @return boolean
	 */
	private boolean isExcludeProperty(String name) {
		return exclude != null && exclude.length > 0 ? ArrayUtils.contains(exclude, name) : false;
	}
}
