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
package org.exitsoft.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.exitsoft.common.type.FieldType;



/**
 * Hibernate状态删除，如果在orm实体配置该注解，将不会物理删除数据，会根据该配置来进行对orm实体的update操作
 * 
 * 
 * <pre>
 * &#064;Entity
 * &#064;Table(name="tb_user")
 * &#064;StateDelete(propertyName = "state",type = FieldType.S,value="2")
 * public class User{
 * 	private String username;
 * 	private String state;
 * 
 * 	public User() {
 * 	}
 * 	getter/setter.....
 * }
 * User user = dao.get(1);
 * dao.delete(user);
 * ----------------------------------
 * sql:update tb_user set state = ? where id = ?
 * </pre>
 * 
 * @author vincent
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateDelete {
	
	/**
	 * 属性名称
	 * 
	 * @return String
	 */
	public String propertyName();
	
	/**
	 * 要改变的值
	 * 
	 * @return String
	 */
	public String value();
	
	/**
	 * 改变值的类型
	 * 
	 * @return {@link FieldType}
	 */
	public FieldType type() default FieldType.I;
}
