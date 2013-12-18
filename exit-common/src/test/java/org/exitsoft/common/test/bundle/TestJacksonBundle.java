package org.exitsoft.common.test.bundle;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.bundle.JacksonBundle;
import org.exitsoft.common.test.entity.Group;
import org.exitsoft.common.test.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * JacksonMapper单元测试
 * 
 * @author vincent
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestJacksonBundle {
	
	private User user;
	
	@Before
	public void reloadUser() {
		user = new User();
	}
	
	@Test
	public void testToJson() {
		String json = null;
		
		//测试不管任何属性的值是否为空，只要存在就序列化成json
		json = JacksonBundle.alwaysMapper().toJson(user);
		assertTrue(StringUtils.contains(json,"\"groupsList\""));
		
		//测试紧存在属性的值不为空的json
		user.setGroupsList(new ArrayList<Group>());
		json = JacksonBundle.nonEmptyMapper().toJson(user);
		assertFalse(StringUtils.contains(json,"\"groupsList\""));
		assertFalse(StringUtils.contains(json,"\"email\""));
		
		//测试紧存在属性的值不为null的json
		json = JacksonBundle.nonNullMapper().toJson(user);
		assertTrue(StringUtils.contains(json,"\"groupsList\""));
		assertTrue(StringUtils.contains(json,"\"groupNames\""));
		
		//测试初始值被改变的json user.setGroupsList(new ArrayList<Group>());
		user = new User();
		json = JacksonBundle.nonDefaultMapper().toJson(user);
		assertTrue(StringUtils.contains(json,"{}"));
		
		user.setEmail("vincent@es.com");
		user.setUsername("vincent");
		user.setGroupsList(new ArrayList<Group>());
		json = JacksonBundle.nonDefaultMapper().toJson(user);
		
		assertTrue(StringUtils.contains(json,"\"email\""));
		assertTrue(StringUtils.contains(json,"\"groupsList\""));
		assertTrue(StringUtils.contains(json,"\"groupNames\""));
	}
}
