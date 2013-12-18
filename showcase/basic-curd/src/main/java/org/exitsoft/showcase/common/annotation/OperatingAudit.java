package org.exitsoft.showcase.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.exitsoft.showcase.service.foundation.OperatingRecordAspect;
import org.exitsoft.showcase.web.account.UserController;

/**
 * 操作审计注解，通过该注解，在类或方法中使用，当调用到该方法时
 * 会引起一次aop，就是{@link OperatingRecordAspect}类，通过该类在调用
 * 之后都会做一次记录，并个把所有的记录存储在TB_OPERATING_RECORD
 * 表中，详细例子:{@link UserController}
 * 
 * @author vincent
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperatingAudit {
	
	/**
	 * 模块名称，默认为""值，可以在类中说明该类是什么模块
	 * 
	 */
	String value() default "";
	
	/**
	 * 功能名称 默认为""值，主要是在类的方法中说明该方法是什么功能
	 * 
	 */
	String function() default "";
}
