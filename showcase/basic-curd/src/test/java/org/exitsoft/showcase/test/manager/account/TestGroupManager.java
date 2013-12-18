package org.exitsoft.showcase.test.manager.account;

import static org.junit.Assert.*;

import java.util.List;

import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.service.account.AccountManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 测试组管理所有方法
 * 
 * @author vincent
 *
 */
public class TestGroupManager extends ManagerTestCaseSupport{

	@Autowired
	private AccountManager accountManager;
	
	@Test
	@Transactional(readOnly=true)
	public void testGetGroup() {
		Group group = accountManager.getGroup("SJDK3849CKMS3849DJCK2039ZMSK0002");
		assertEquals(group.getName(), "超级管理员");
	}

	@Test
	public void testGetGroupsListOfString() {
		List<String> ids = Lists.newArrayList("402881c4408c7d2301408c86b7a80001",
						   					  "402881c4408c7d2301408c870ed10002",
						   					  "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		List<Group> result = accountManager.getGroups(ids);
		
		assertEquals(result.size(), 3);
	}

	@Test
	public void testSaveGroup() {
		Group entity = new Group();
		entity.setName("test");
		entity.setRemark("...");
		entity.setRole("role[test]");
		entity.setType(GroupType.RoleGorup.getValue());
		entity.setValue("/**");
		
		int before = countRowsInTable("tb_group");
		accountManager.saveGroup(entity);
		int after = countRowsInTable("tb_group");
		
		assertEquals(before + 1, after);
	}

	@Test
	public void testDeleteGroups() {
		int before = countRowsInTable("tb_group");
		accountManager.deleteGroups(Lists.newArrayList("402881c4408c7d2301408c870ed10002"));
		int after = countRowsInTable("tb_group");
		
		assertEquals(before - 1, after);
	}

	@Test
	public void testSearchGroupPage() {
		PageRequest request = new PageRequest();
		
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_name", "员"),
				PropertyFilters.build("EQS_type", "03")
		);
		
		Page<Group> page = accountManager.searchGroupPage(request, filters);
		
		assertEquals(page.getTotalItems(), 2);
		assertEquals(page.getTotalPages(), 1);
	}

	@Test
	public void testGetAllGroupGroupType() {
		List<Group> result = accountManager.getGroup(GroupType.RoleGorup);
		assertEquals(result.size(), 3);
		
		result = accountManager.getGroup(GroupType.RoleGorup,"402881c4408c7d2301408c870ed10002","SJDK3849CKMS3849DJCK2039ZMSK0002");
		assertEquals(result.size(), 1);
	}

	@Test
	public void testGetUserGroups() {
		List<Group> result = accountManager.getUserGroups("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(result.size(), 1);
	}
	
}
