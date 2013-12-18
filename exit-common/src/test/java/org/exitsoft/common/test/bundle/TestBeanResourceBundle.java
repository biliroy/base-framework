package org.exitsoft.common.test.bundle;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

import org.apache.commons.collections.MapUtils;
import org.exitsoft.common.bundle.BeanResourceBundle;
import org.exitsoft.common.test.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.common.collect.Maps;

/**
 * 单元测试BeanResourceBundle
 * 
 * @author Administrator
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestBeanResourceBundle {
	private User user = new User();
	
	@Test
	@SuppressWarnings("unchecked")
	public void testToMap() {
		user.setEmail(null);
		user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		user.setPassword("123456");
		user.setRealname("chenxiaobo");
		user.setState(new Integer(1));
		user.setUsername("vincent");
		
		Map<String, Object> map = Maps.newHashMap();
		
		//测试include属性
		map = MapUtils.toMap(new BeanResourceBundle(user,new String[]{"username","password","realname"}));
		
		assertEquals(map.size(), 3);
		assertEquals(map.get("username"), user.getUsername());
		assertEquals(map.get("realname"), user.getRealname());
		assertEquals(map.get("password"), user.getPassword());
		
		assertFalse(map.containsKey("email"));
		
		//测试exclude属性
		map = MapUtils.toMap(new BeanResourceBundle(user,null,new String[]{"username","password","realname"}));
		
		assertEquals(map.size(), 2);
		assertEquals(map.get("id"), user.getId());
		assertEquals(map.get("state"),user.getState());
		
		assertFalse(map.containsKey("email"));
		
		//测试ignoreEmptyValue属性
		map = MapUtils.toMap(new BeanResourceBundle(user,null,null,false));

		assertEquals(map.get("id"), user.getId());
		assertEquals(map.get("state"),user.getState());
		assertEquals(map.get("username"), user.getUsername());
		assertEquals(map.get("realname"), user.getRealname());
		assertEquals(map.get("password"), user.getPassword());
		
		assertTrue(map.containsKey("email"));
		
	}
}
