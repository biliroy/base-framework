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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.type.FieldType;
import org.exitsoft.common.utils.ServletUtils;
import org.springframework.util.Assert;

/**
 * 属性过滤器工具类
 * 
 * @author vincent
 *
 */
public class PropertyFilters {
	
	/**
	 * 通过表达式和对比值创建属性过滤器
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilters.build("EQS_propertyName","vincent")
	 * </code>
	 * 
	 * @param expression 表达式
	 * @param matchValue 对比值
	 * 
	 * @return {@link PropertyFilter}
	 */
	@SuppressWarnings("static-access")
	public static PropertyFilter build(String expression,String matchValue) {
		
		Assert.hasText(expression, "表达式不能为空");
		
		String restrictionsNameAndClassType = StringUtils.substringBefore(expression, "_");
		
		String restrictionsName = StringUtils.substring(restrictionsNameAndClassType, 0,restrictionsNameAndClassType.length() - 1);
		String classType = StringUtils.substring(restrictionsNameAndClassType, restrictionsNameAndClassType.length() - 1, restrictionsNameAndClassType.length());
		
		FieldType FieldType = null;
		try {
			FieldType = FieldType.valueOf(classType);
		} catch (Exception e) {
			throw new IllegalAccessError("[" + expression + "]表达式找不到相应的属性类型,获取的值为:" + classType);
		}
		
		String[] propertyNames = null;
		
		if (StringUtils.contains(expression,"_OR_")) {
			String temp = StringUtils.substringAfter(expression, restrictionsNameAndClassType + "_");
			propertyNames = StringUtils.splitByWholeSeparator(temp, "_OR_");
		} else {
			propertyNames = new String[1];
			propertyNames[0] = StringUtils.substringAfterLast(expression, "_");
		}
		
		return new PropertyFilter(restrictionsName, FieldType, propertyNames,matchValue);
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilters#build(HttpServletRequest, String, boolean)}
	 * 
	 * @param request HttpServletRequest
	 */
	public static List<PropertyFilter> build(HttpServletRequest request) {
		return build(request, "filter");
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}
	 * 该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilters#build(HttpServletRequest, String, boolean)}
	 * 
	 * @param request HttpServletRequest
	 * @param filterPrefix 用于识别是propertyfilter参数的前准
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> build(HttpServletRequest request,String filterPrefix) {
		return build(request, "filter",false);
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}
	 * 该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilters#build(HttpServletRequest, String, boolean)}
	 * 
	 * <pre>
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =build(request,"filter",false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =build(request,"filter",true);
	 * 当前filters:EQS_proerpty1="value"
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param ignoreEmptyValue true表示当存在""值时忽略该PropertyFilter
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> build(HttpServletRequest request,boolean ignoreEmptyValue) {
		return build(request, "filter",ignoreEmptyValue);
	}

	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,例子:
	 * 
	 * <pre>
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =build(request,"filter",false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =build(request,"filter",true);
	 * 当前filters:EQS_proerpty1="value"
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param filterPrefix 用于识别是propertyfilter参数的前准
	 * @param ignoreEmptyValue true表示当存在""值时忽略该PropertyFilter
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> build(HttpServletRequest request,String filterPrefix,boolean ignoreEmptyValue) {

		// 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, filterPrefix + "_");

		return build(filterParamMap,ignoreEmptyValue);
	}
	
	/**
	 * 从Map中创建PropertyFilter列表，如:
	 * 
	 * <pre>
     * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = build(o);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
     * </pre>
	 * 
	 * 
	 * @param filters 过滤器信息
	 * 
	 */
	public static List<PropertyFilter> build(Map<String, Object> filters) {
		
		return build(filters,false);
	}
	
	/**
	 * 从Map中创建PropertyFilter列表，如:
	 * 
	 * <pre>
     * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = build(o,false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = build(o,true);
	 * 当前filters:EQS_proerpty1="value"
     * </pre>
	 * 
	 * 
	 * @param filters 过滤器信息
	 * @param ignoreEmptyValue true表示当存在 null或者""值时忽略该PropertyFilter
	 * 
	 */
	public static List<PropertyFilter> build(Map<String, Object> filters,boolean ignoreEmptyValue) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		// 分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			String expression = entry.getKey();
			Object value = entry.getValue();
			//如果ignoreEmptyValue为true忽略null或""的值
			if (ignoreEmptyValue && (value == null || value.toString().trim().equals(""))) {
				continue;
			}
			//如果ignoreEmptyValue为true忽略null或""的值
			PropertyFilter filter = build(expression, value.toString());
			filterList.add(filter);
			
		}
		return filterList;
	}
	
}
