/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.exitsoft.common.bundle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;


/**
 * jackson 工具类
 * 
 * @author calvin
 *
 */
@SuppressWarnings("unchecked")
public class JacksonBundle {
	
	private static Logger logger = LoggerFactory.getLogger(JacksonBundle.class);
	
	private ObjectMapper mapper;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public JacksonBundle() {
		this(null);
	}

	/**
	 * 构造方法，根据Jackson的{@link Include}类创建ObjectMapper
	 * 
	 */
	public JacksonBundle(Include include) {
		mapper = new ObjectMapper();
		if (include != null) {
			mapper.setSerializationInclusion(include);
		}
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 创建序列化属性为所有对象认为可能是空值的ObjectMapper
	 * 
	 * <p>
     * 默认是的空值判断有以下类型:
     * <ul>
     * 	<li>
     * 		 如果是 {@link java.util.Collections} 或 {@link java.util.Map} 。根据 <code>isEmpty()</code>方法返回值判断是否序列化
     * 	</li>
     * 	<li>
     * 		  如果是Java数组，长度等于0都为空值的情况
     * 	</li>
     * 	<li>
     * 		如是String类型根据 <code>length()</code>方法返回值判断是否序列化,如果为0表示该值为空
     * 	</li>
     * </ul>
     * </p>
     * 其他类型的默认处理为：空值将一样加入。
     * <p>
     * 提示：其他类型的默认处理可以自定义{@link JsonSerializer}的{@link JsonSerializer#isEmpty(Object)}如果被重写，
     * 序列化json会调用该方法，根据返回值判断是否序列化
	 * </p>
	 * @return {@link JacksonBundle}
	 */
	public static JacksonBundle nonEmptyMapper() {
		
		return new JacksonBundle(Include.NON_EMPTY);
	}
	
	/**
	 * 创建不管任何值都序列化成json的ObjectMapper
	 * 
	 * @return {@link JacksonBundle}
	 */
	public static JacksonBundle alwaysMapper() {
		return new JacksonBundle(Include.ALWAYS);
	}
	
	/**
	 * 创建序列化属性为非空(null)值的ObjectMapper
	 * 
	 * @return {@link JacksonBundle}
	 */
	public static JacksonBundle nonNullMapper() {
		return new JacksonBundle(Include.NON_NULL);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的ObjectMapper, 最节约的存储方式。
	 * @return
	 */
	public static JacksonBundle nonDefaultMapper() {
		return new JacksonBundle(Include.NON_DEFAULT);
	}
	
	/** 
	 * 将json字符串转换为对象.
     * <pre>
     * 如果json字符串为null或"null"字符串,返回null. 如果json字符串为"[]",返回空集合. 
     * 如需读取集合如List/Map,且不是List&lt;String&gt;这种简单类型时使用如下语句: 
     * List&lt;MyBean&gt; beanList = binder.getMapper().readValue(listString, new TypeReference&lt;List&lt;MyBean&gt;&gt;() {});
     * </pre>
     * 
     * @param json json字符串
     * @param tragetClass 转换对象的Class
     * 
     * @return <T>
     */  
    public <T> T fromJson(String json, Class<T> tragetClass) {  
        if (StringUtils.isEmpty(json)) {  
            return null;  
        }  
  
        try {  
            return mapper.readValue(json, tragetClass);  
        } catch (IOException e) {  
            logger.warn("parse json string error:" + json, e);  
            return null;  
        }  
    }
    
    /**
	 * 反序列化复杂Collection如List<Bean>, 先使用函数createCollectionType构造类型,然后调用本函数.
	 * @see #createCollectionType(Class, Class...)
	 */
	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return (T) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 构造泛型的Collection Type如:
	 * <p>
	 * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
	 * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
	 * </p>
	 */
	public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 当JSON里只含有Bean的部分属性时，更新一个已存在Bean，只覆盖部分属性.
	 */
	public <T> T update(String jsonString, T object) {
		try {
			return (T) mapper.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return null;
	}
  
    /**
     * 将对象转换成json字符串,如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
     * 
     * @param target 转换为json的对象
     */  
    public String toJson(Object target) {  
  
        try {  
            return mapper.writeValueAsString(target);  
        } catch (IOException e) {  
            logger.warn("write to json string error:" + target, e);  
            return null;  
        }
    }
    
    /** 
     * 设置转换日期类型的时间科室,如果不设置默认打印Timestamp毫秒数.
     * 
     * @param pattern 时间格式化字符串
     */  
    public void setDateFormat(String pattern) {  
        if (StringUtils.isNotBlank(pattern)) {  
            DateFormat dateFormat = new SimpleDateFormat(pattern);  
            mapper.getSerializationConfig().with(dateFormat);
            mapper.getDeserializationConfig().with(dateFormat);  
        }  
    }  
  
    /**
     * 将对象转换成JSONP格式字符串
     * 
     * @param function jsonp回调方法名
     * @param target 转换为jsonp的对象
     */
	public String toJsonP(String function, Object target) {
		return toJson(new JSONPObject(function, target));
	}

	/**
	 * 设置是否使用Enum的toString函数来读取Enum,为false时使用Enum的name()函数类读取Enum, 默认为false.
	 */
	public void enableEnumUseToString() {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	/**
	 * 支持使用Jaxb的Annotation,使得POJO上的annotation不用与Jackson藕合.
	 * 默认会先查找jaxb的annotation,如果找不到再找jackson的.
	 */
	public void enableJaxbAnnotation() {
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		mapper.registerModule(module);
	}

    /** 
     * 取出Mapper做进一步的设置或使用其他序列化API. 
     * 
     * @return {@link ObjectMapper}
     */  
    public ObjectMapper getMapper() {  
        return mapper;  
    }
}
