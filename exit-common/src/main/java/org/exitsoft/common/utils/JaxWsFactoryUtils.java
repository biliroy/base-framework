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
package org.exitsoft.common.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * 借助Cxf对JaxWs生成接口的工具类
 * 
 * @author vincent
 *
 */
@SuppressWarnings("unchecked")
public class JaxWsFactoryUtils {
	
	//Ws代理工厂
	private static JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	
	//Ws动态客服端工厂
	private static JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
	
	//记录动态客户端的map，如果存在将不会创建。
	private static Map<String, Client> clientMap = new ConcurrentHashMap<String, Client>();
	
	/**
	 * 根据serviceClass类型和ws地址,创建一个可以调用的接口类
	 * 
	 * <pre>
	 * 例子:
	 * WebService ws = JaxWsProxyFactoryBeanMapper.getProxyFactoryBean(WebService.class,"http://192.168.0.63:8080/CXF_Server_01/cxf/WebService");
	 * ws.method();
	 * </pre>
	 * 
	 * @param serviceClass ws的接口类
	 * @param address ws地址
	 * 
	 * @return Object
	 */
	public static <T> T getProxyFactoryBean(Class<T> serviceClass,String address) {
		jaxWsProxyFactoryBean.setServiceClass(serviceClass);
		jaxWsProxyFactoryBean.setAddress(address);
		return (T) jaxWsProxyFactoryBean.create(); 
	}
	
	/**
	 * 执行web service方法
	 * <pre>
	 * 例子:
	 * Object result = JaxWsProxyFactoryBeanMapper.invoke("http://192.168.0.63:8080/CXF_Server_01/cxf/WebService","method");
	 * System.out.println(result[0]);
	 * </pre>
	 * 
	 * @param wsdlUrl web service地址
	 * @param operationName 要执行的方法名
	 * @param params 方法中的参数值
	 * 
	 * @return Object[]
	 * 
	 * @throws Exception
	 */
	public static Object[] invoke(String wsdlUrl, String operationName, Object...params) throws Exception {
		return createDynamicClient(wsdlUrl).invoke(operationName, params);
	}
	
	/**
	 * 通过wsdlUrl刷新对应的动态客户端
	 * 
	 * @param wsdlUrl web service地址
	 */
	public static void refreshClient(String wsdlUrl) {
		clientMap.put(wsdlUrl, createDynamicClient(wsdlUrl));
	}
	
	/**
	 * 刷新所有动态客户端
	 */
	public static void refreshClientMap() {
		for (Entry<String, Client> entry : clientMap.entrySet()) {
			entry.setValue(createDynamicClient(entry.getKey()));
		}
	}
	
	/**
	 * 创建动态客户端对象
	 * 
	 * @param wsdlUrl web service地址
	 * 
	 * @return {@link Client}
	 */
	public static Client createDynamicClient(String wsdlUrl) {
		
		if (clientMap.containsKey(wsdlUrl)) {
			return clientMap.get(wsdlUrl);
		}
		
		Client client = jaxWsDynamicClientFactory.createClient(wsdlUrl);
		clientMap.put(wsdlUrl, client);
		
		return client;
	}
	
	
	
	/**
	 * 获取JaxWs代理工厂
	 * 
	 * @return {@link JaxWsProxyFactoryBean}
	 */
	public static JaxWsProxyFactoryBean getJaxWsProxyFactoryBean() {
		return jaxWsProxyFactoryBean;
	}

	/**
	 * 获取JaxWs动态客服端工厂
	 * 
	 * @return {@link JaxWsDynamicClientFactory}
	 */
	public static JaxWsDynamicClientFactory getJaxWsDynamicClientFactory() {
		return jaxWsDynamicClientFactory;
	}
	
	
}
