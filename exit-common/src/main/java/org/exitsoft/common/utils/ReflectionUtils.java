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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射工具类.
 * 
 * @author vincent
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ReflectionUtils {

	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory
			.getLogger(ReflectionUtils.class);

	/**
	 * 调用Getter方法.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param propertyName
	 *            属性字段名称
	 * 
	 * @return Object
	 */
	public static <T> T invokeGetterMethod(Object target, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return (T) invokeMethod(target, getterMethodName, new Class[] {},
				new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * 
	 */
	public static void invokeSetterMethod(Object target, String propertyName,
			Object value) {
		invokeSetterMethod(target, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param value
	 *            值
	 * @param FieldType
	 *            用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(Object target, String propertyName,
			Object value, Class<?> FieldType) {
		Class<?> type = FieldType != null ? FieldType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(target, setterMethodName, new Class[] { type },
				new Object[] { value });
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.用于一次性调用的情况.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param methodName
	 *            方法名称
	 * @param parameterTypes
	 *            方法参数类型，和args参数一一对应
	 * @param args
	 *            方法参数值，值的类型和parameterTypes参数一一对应
	 * 
	 * @return Object
	 */
	public static Object invokeMethod(final Object target,
			final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {

		Method method = getAccessibleMethod(target, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("找不到 [" + methodName
					+ "] 方法在对象 [" + target + "] 里");
		}

		try {
			return method.invoke(target, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 判断obj参数是否存在fiedlName字段
	 * 
	 * @param target
	 *            要判断的目标对象
	 * @param fieldName
	 *            字段名称
	 * 
	 * @return boolean
	 */
	public static boolean hasField(Object target, String fieldName) {
		return getAccessibleField(target, fieldName) != null;
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param fieldName
	 *            字段名称
	 * 
	 * @return Object
	 */
	public static <T> T getFieldValue(final Object target,
			final String fieldName) {
		Assert.notNull(target, "target不能为空");
		Assert.notNull(fieldName, "fieldName不能为空");

		Field field = getAccessibleField(target, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("找不到字段 [" + fieldName
					+ "] 在对象  [" + target + "] 里");
		}

		Object result = null;
		try {
			result = field.get(target);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return (T) result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param fieldName
	 *            字段名称
	 * @param value
	 *            值
	 */
	public static void setFieldValue(final Object target,
			final String fieldName, final Object value) {
		Assert.notNull(target, "target不能为空");
		Assert.notNull(fieldName, "fieldName不能为空");

		Field field = getAccessibleField(target, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("找不到字段 [" + fieldName
					+ "] 在对象 [" + target + "] 里");
		}

		try {
			field.set(target, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.如向上转型到Object仍无法找到, 返回null.
	 * 
	 * @param target
	 *            目标对象Object
	 * @param fieldName
	 *            字段名称
	 * 
	 * @return {@link Field}
	 */
	public static Field getAccessibleField(final Object target,
			final String fieldName) {
		Assert.notNull(target, "target不能为空");
		return getAccessibleField(getTargetClass(target), fieldName);
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param fieldName
	 *            class中的字段名
	 * 
	 * @return {@link Field}
	 */
	public static Field getAccessibleField(final Class targetClass,
			final String fieldName) {
		Assert.notNull(targetClass, "targetClass不能为空");
		Assert.hasText(fieldName, "fieldName不能为空");
		for (Class<?> superClass = targetClass; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的所有DeclaredField, 并强制设置为可访问.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * 
	 * @return List
	 */
	public static List<Field> getAccessibleFields(final Object target) {
		Assert.notNull(target, "target不能为空");
		return getAccessibleFields(getTargetClass(target));
	}

	/**
	 * 循环向上转型, 获取对象的所有DeclaredField, 并强制设置为可访问.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * 
	 * @return List
	 */
	public static List<Field> getAccessibleFields(final Class targetClass) {
		return getAccessibleFields(targetClass, false);
	}

	/**
	 * 获取对象的所有DeclaredField,并强制设置为可访问.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param ignoreParent
	 *            是否循环向上转型,获取所有父类的Field
	 * 
	 * @return List
	 */
	public static List<Field> getAccessibleFields(final Class targetClass,
			final boolean ignoreParent) {
		Assert.notNull(targetClass, "targetClass不能为空");
		List<Field> fields = new ArrayList<Field>();

		Class<?> sc = targetClass;

		do {
			Field[] result = sc.getDeclaredFields();

			if (!ArrayUtils.isEmpty(result)) {

				for (Field field : result) {
					field.setAccessible(true);
				}

				CollectionUtils.addAll(fields, result);
			}

			sc = sc.getSuperclass();

		} while (sc != Object.class && !ignoreParent);

		return fields;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 * 
	 * @param target
	 *            Object对象
	 * @param methodName
	 *            方法名称
	 * @param parameterTypes
	 *            方法参数类型
	 * 
	 * @return {@link Method}
	 */
	public static Method getAccessibleMethod(final Object target,
			final String methodName, Class<?>... parameterTypes) {
		Assert.notNull(target, "target不能为空");
		return getAccessibleMethod(getTargetClass(target), methodName,
				parameterTypes);
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param parameterTypes
	 *            方法参数类型
	 * 
	 * @return {@link Method}
	 */
	public static Method getAccessibleMethod(final Class targetClass,
			final String methodName, Class<?>... parameterTypes) {
		Assert.notNull(targetClass, "targetClass不能为空");
		Assert.notNull(methodName, "methodName不能为空");

		for (Class<?> superClass = targetClass; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName,
						parameterTypes);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的所有DeclaredMethod 并强制设置为可访问.
	 * 
	 * @param target
	 *            目标对象Object
	 * 
	 * @return List
	 */
	public static List<Method> getAccessibleMethods(final Object target) {
		Assert.notNull(target, "target不能为空");
		return getAccessibleMethods(getTargetClass(target));
	}

	/**
	 * 循环向上转型, 获取对象的所有DeclaredMethod 并强制设置为可访问.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * 
	 * @return List
	 */
	public static List<Method> getAccessibleMethods(final Class targetClass) {
		return getAccessibleMethods(targetClass, false);
	}

	/**
	 * 获取对象的所有DeclaredMethod 并强制设置为可访问.
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param ignoreParent
	 *            是否循环向上转型,获取所有父类的Method
	 * 
	 * @return List
	 */
	public static List<Method> getAccessibleMethods(final Class targetClass,
			boolean ignoreParent) {
		Assert.notNull(targetClass, "targetClass不能为空");
		List<Method> methods = new ArrayList<Method>();

		Class<?> superClass = targetClass;
		do {
			Method[] result = superClass.getDeclaredMethods();

			if (!ArrayUtils.isEmpty(result)) {

				for (Method method : result) {
					method.setAccessible(true);
				}

				CollectionUtils.addAll(methods, result);
			}

			superClass = superClass.getSuperclass();
		} while (superClass != Object.class && !ignoreParent);

		return methods;
	}

	/**
	 * 获取对象中的注解
	 * 
	 * @param target
	 *            目标对象Object
	 * @param annotationClass
	 *            注解
	 * 
	 * @return Object
	 */
	public static <T> T getAnnotation(Object target, Class annotationClass) {
		Assert.notNull(target, "target不能为空");
		return (T) getAnnotation(target.getClass(), annotationClass);
	}

	/**
	 * 获取对象中的注解
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param annotationClass
	 *            注解类型Class
	 * 
	 * @return Object
	 */
	public static <T extends Annotation> T getAnnotation(Class targetClass,
			Class annotationClass) {
		Assert.notNull(targetClass, "targetClass不能为空");
		Assert.notNull(annotationClass, "annotationClass不能为空");

		if (targetClass.isAnnotationPresent(annotationClass)) {
			return (T) targetClass.getAnnotation(annotationClass);
		}

		return null;
	}

	/**
	 * 获取Object对象中所有annotationClass类型的注解
	 * 
	 * @param target
	 *            目标对象Object
	 * @param annotationClass
	 *            Annotation类型
	 * 
	 * @return {@link Annotation}
	 */
	public static <T extends Annotation> List<T> getAnnotations(Object target,
			Class annotationClass) {
		Assert.notNull(target, "target不能为空");
		return getAnnotations(getTargetClass(target), annotationClass);
	}

	/**
	 * 
	 * 获取对象中的所有annotationClass注解
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param annotationClass
	 *            注解类型Class
	 * 
	 * @return List
	 */
	public static <T extends Annotation> List<T> getAnnotations(
			Class targetClass, Class annotationClass) {
		Assert.notNull(targetClass, "targetClass不能为空");
		Assert.notNull(annotationClass, "annotationClass不能为空");

		List<T> result = new ArrayList<T>();
		Annotation annotation = targetClass.getAnnotation(annotationClass);
		if (annotation != null) {
			result.add((T) annotation);
		}
		Constructor[] constructors = targetClass.getDeclaredConstructors();
		// 获取构造方法里的注解
		CollectionUtils.addAll(result,
				getAnnotations(constructors, annotationClass).iterator());

		Field[] fields = targetClass.getDeclaredFields();
		// 获取字段中的注解
		CollectionUtils.addAll(result, getAnnotations(fields, annotationClass)
				.iterator());

		Method[] methods = targetClass.getDeclaredMethods();
		// 获取方法中的注解
		CollectionUtils.addAll(result, getAnnotations(methods, annotationClass)
				.iterator());

		for (Class<?> superClass = targetClass.getSuperclass(); superClass == null
				|| superClass == Object.class; superClass = superClass
				.getSuperclass()) {
			List<T> temp = getAnnotations(superClass, annotationClass);
			if (CollectionUtils.isNotEmpty(temp)) {
				CollectionUtils.addAll(result, temp.iterator());
			}
		}

		return result;
	}

	/**
	 * 获取field的annotationClass注解
	 * 
	 * @param field
	 *            field对象
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return {@link Annotation}
	 */
	public static <T extends Annotation> T getAnnotation(Field field,
			Class annotationClass) {

		Assert.notNull(field, "field不能为空");
		Assert.notNull(annotationClass, "annotationClass不能为空");

		field.setAccessible(true);
		if (field.isAnnotationPresent(annotationClass)) {
			return (T) field.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取field数组中匹配的annotationClass注解
	 * 
	 * @param fields
	 *            field对象数组
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return List
	 */
	public static <T extends Annotation> List<T> getAnnotations(Field[] fields,
			Class annotationClass) {

		if (ArrayUtils.isEmpty(fields)) {
			return null;
		}

		List<T> result = new ArrayList<T>();

		for (Field field : fields) {
			field.setAccessible(true);
			Annotation annotation = getAnnotation(field, annotationClass);
			if (annotation != null) {
				result.add((T) annotation);
			}
		}

		return result;
	}

	/**
	 * 获取method的annotationClass注解
	 * 
	 * @param method
	 *            method对象
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return {@link Annotation}
	 */
	public static <T extends Annotation> T getAnnotation(Method method,
			Class annotationClass) {

		Assert.notNull(method, "method不能为空");
		Assert.notNull(annotationClass, "annotationClass不能为空");

		method.setAccessible(true);
		if (method.isAnnotationPresent(annotationClass)) {
			return (T) method.getAnnotation(annotationClass);
		}
		return null;
	}

	/**
	 * 获取method数组中匹配的annotationClass注解
	 * 
	 * @param methods
	 *            method对象数组
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return List
	 */
	public static <T extends Annotation> List<T> getAnnotations(
			Method[] methods, Class annotationClass) {

		if (ArrayUtils.isEmpty(methods)) {
			return null;
		}

		List<T> result = new ArrayList<T>();

		for (Method method : methods) {

			Annotation annotation = getAnnotation(method, annotationClass);
			if (annotation != null) {
				result.add((T) annotation);
			}
		}

		return result;
	}

	/**
	 * 获取constructor的annotationClass注解
	 * 
	 * @param constructor
	 *            constructor对象
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return {@link Annotation}
	 */
	public static <T extends Annotation> T getAnnotation(
			Constructor constructor, Class annotationClass) {

		Assert.notNull(constructor, "constructor不能为空");
		Assert.notNull(annotationClass, "annotationClass不能为空");

		constructor.setAccessible(true);

		if (constructor.isAnnotationPresent(annotationClass)) {
			return (T) constructor.getAnnotation(annotationClass);
		}

		return null;
	}

	/**
	 * 获取constructors数组中匹配的annotationClass注解
	 * 
	 * @param constructors
	 *            constructor对象数组
	 * @param annotationClass
	 *            annotationClass注解
	 * 
	 * @return List
	 */
	public static <T extends Annotation> List<T> getAnnotations(
			Constructor[] constructors, Class annotationClass) {

		if (ArrayUtils.isEmpty(constructors)) {
			return null;
		}

		List<T> result = new ArrayList<T>();

		for (Constructor constructor : constructors) {
			Annotation annotation = getAnnotation(constructor, annotationClass);
			if (annotation != null) {
				result.add((T) annotation);
			}
		}

		return result;
	}

	/**
	 * 
	 * 更具类型获取o中的所有字段名称
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * @param type
	 *            要获取名称的类型
	 * 
	 * @return List
	 */
	public static List<String> getAccessibleFieldNames(
			final Class targetClass, Class type) {

		Assert.notNull(targetClass, "targetClass不能为空");
		Assert.notNull(type, "type不能为空");

		List<String> list = new ArrayList<String>();

		for (Field field : targetClass.getDeclaredFields()) {
			if (field.getType().equals(type)) {
				list.add(field.getName());
			}
		}

		return list;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class，否则返回首个泛参数型类型
	 * 
	 * <pre>
	 * 例如
	 * public UserDao extends HibernateDao<User>
	 * </pre>
	 * 
	 * @param targetClass
	 *            要反射的目标对象Class
	 * @return Object.clss或者T.class
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class targetClass) {
		return getSuperClassGenricType(targetClass, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.否则返回泛参数型类型
	 * 
	 * <pre>
	 * 例如
	 * public UserDao extends HibernateDao<User,Long>
	 * </pre>
	 * 
	 * @param targetClass
	 *            要反射的目标对象Class
	 * @param index
	 *            反省参数的位置
	 * 
	 * @return class
	 */
	public static Class getSuperClassGenricType(final Class targetClass,
			final int index) {

		Assert.notNull(targetClass, "targetClass不能为空");

		Type genType = targetClass.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(targetClass.getSimpleName()
					+ "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of "
					+ targetClass.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(targetClass.getSimpleName()
					+ " not set the actual Class targetClassn superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 通过Class创建对象
	 * 
	 * @param targetClass
	 *            目标对象Class
	 * 
	 * @return Object
	 */
	public static <T> T newInstance(Class targetClass) {
		Assert.notNull(targetClass, "targetClass不能为空");
		try {
			return (T) targetClass.newInstance();
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}

	}

	/**
	 * 获取对象Class如果被cglib AOP过的对象或对象为CGLIB的Class，将获取真正的Class类型
	 * 
	 * @param target  对象
	 * 
	 * @return Class
	 */
	public static Class<?> getTargetClass(Object target) {
		Assert.notNull(target, "target不能为空");
		return getTargetClass(target.getClass());

	}
	
	/**
	 * 获取Class如果被cglib AOP过的对象或对象为CGLIB的Class，将获取真正的Class类型
	 * 
	 * @param target  对象
	 * 
	 * @return Class
	 */
	public static Class<?> getTargetClass(Class<?> targetClass) {
		
		Assert.notNull(targetClass, "targetClass不能为空");
		
		Class clazz = targetClass;
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 * 
	 * @param e
	 *            checked exception
	 * 
	 *            return {@link RuntimeException}
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(
			Exception e) {
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.",
					((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

}
