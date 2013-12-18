package org.exitsoft.common.test.utils;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.exitsoft.common.test.entity.User;
import org.exitsoft.common.utils.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * 反射工具类单元测试
 * 
 * @author vincent
 *
 */
public class TestReflectionUtils {
	
	private User user;
	
	@Before
	public void initData() {
		user = new User();
	}
	
	@Test
	public void testInvokeMethod() {
		ReflectionUtils.invokeMethod(user, "setUsername", new Class[]{String.class}, new String[]{"vincent"});
		assertEquals(user.getUsername(), "vincent");
	}

	@Test
	public void testHasField() {
		assertEquals(ReflectionUtils.hasField(user, "username"),true);
		assertEquals(ReflectionUtils.hasField(user, "vicnent"),false);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFieldValue() {
		ReflectionUtils.invokeMethod(user, "setUsername", new Class[]{String.class}, new String[]{"vincent"});
		assertEquals("vincent", ReflectionUtils.getFieldValue(user, "username"));
		ReflectionUtils.getFieldValue(user, "vincent");
		assertEquals(null, ReflectionUtils.getFieldValue(user, "password"));
	}

	@Test
	public void testSetFieldValue() {
		ReflectionUtils.setFieldValue(user, "username", "vincent");
		
		assertEquals(ReflectionUtils.getFieldValue(user, "username"), "vincent");
	}

	@Test
	public void testGetAccessibleField() {
		Field field = ReflectionUtils.getAccessibleField(user, "groupsList");
		assertEquals(field.getType(),List.class);
	}

	@Test
	public void testGetAccessibleFields() {
		List<Field> parends = ReflectionUtils.getAccessibleFields(user.getClass(), false);
		List<Field> fields = ReflectionUtils.getAccessibleFields(user.getClass(), true);
		
		assertEquals(fields.size(),6);
		assertEquals(parends.size(),7);
	}

	@Test
	public void testGetAccessibleMethod() {
		Method method = ReflectionUtils.getAccessibleMethod(user, "getUsername");
		assertEquals(method.getName(),"getUsername");
	}

	@Test
	public void testGetAccessibleMethods() {
		List<Method> methods = ReflectionUtils.getAccessibleMethods(user.getClass(),true);
		List<Method> parents = ReflectionUtils.getAccessibleMethods(user.getClass());
		
		assertEquals(methods.size(),13);
		assertEquals(parents.size(),15);
	}

	@Test
	public void testGetAnnotation() {
		XmlRootElement element = ReflectionUtils.getAnnotation(user.getClass(), XmlRootElement.class);
		
		assertEquals(element.name(),"user");
	}

	@Test
	public void testGetAccessibleFieldNames() {
		List<String> fieldNames = ReflectionUtils.getAccessibleFieldNames(user.getClass(), String.class);
		assertEquals(fieldNames.size(), 4);
	}

}
