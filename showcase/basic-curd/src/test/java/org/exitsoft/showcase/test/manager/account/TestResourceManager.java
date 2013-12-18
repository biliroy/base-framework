package org.exitsoft.showcase.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.showcase.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.entity.account.Resource;
import org.exitsoft.showcase.service.account.AccountManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 测试资源管理所有方法
 * 
 * @author vincent
 *
 */
public class TestResourceManager extends ManagerTestCaseSupport{
	
	@Autowired
	private AccountManager accountManager;
	
	@Test
	@Transactional(readOnly=true)
	public void testGetResource() {
		Resource resource = accountManager.getResource("SJDK3849CKMS3849DJCK2039ZMSK0003");
		assertEquals(resource.getName(), "权限管理");
		assertEquals(resource.getChildren().size(), 3);
	}

	@Test
	public void testGetResources() {
		List<String> ids = Lists.newArrayList(
				"SJDK3849CKMS3849DJCK2039ZMSK0007",
				"SJDK3849CKMS3849DJCK2039ZMSK0008",
				"SJDK3849CKMS3849DJCK2039ZMSK0009",
				"SJDK3849CKMS3849DJCK2039ZMSK0010"
		);
		
		List<Resource> result = accountManager.getResources(ids);
		
		assertEquals(result.size(), 4);
	}

	@Test
	public void testSearchResourcePage() {
		PageRequest request = new PageRequest();
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_name", "管理"),
				PropertyFilters.build("LIKES_type", "01")
				
		);
		Page<Resource> page = accountManager.searchResourcePage(request, filters);
		
		assertEquals(page.getTotalItems(), 8);
		assertEquals(page.getTotalPages(), 1);
	}

	@Test
	public void testSaveResource() {
		Resource entity = new Resource();
		entity.setName("test");
		entity.setPermission("prem[test:test]");
		entity.setRemark("...");
		entity.setType(ResourceType.Security.getValue());
		entity.setValue("/test/**");
		
		int before = countRowsInTable("tb_resource");
		accountManager.saveResource(entity);
		int after = countRowsInTable("tb_resource");
		
		assertEquals(before + 1, after);
	}

	@Test
	public void testDeleteResources() {
		
		int before = countRowsInTable("tb_resource");
		accountManager.deleteResources(Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0004"));
		int after = countRowsInTable("tb_resource");
		
		assertEquals(before - 5, after);
	}

	@Test
	public void testGetAllResources() {
		List<Resource> result = accountManager.getResources();
		assertEquals(result.size(), 25);
		
		result = accountManager.getResources("SJDK3849CKMS3849DJCK2039ZMSK0006","SJDK3849CKMS3849DJCK2039ZMSK0007");
		assertEquals(result.size(), 23);
	}
	
	@Test
	public void testGetUserResources() {
		List<Resource> result = accountManager.getUserResources("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(result.size(), 25);
	}

	@Test
	@Transactional(readOnly=true)
	public void testMergeResourcesToParent() {
		List<Resource> result = accountManager.getUserResources("SJDK3849CKMS3849DJCK2039ZMSK0001");
		result = accountManager.mergeResourcesToParent(result, ResourceType.Security);
		assertEquals(result.size(), 2);
		assertEquals(result.get(0).getChildren().size(),3);
		assertEquals(result.get(1).getChildren().size(),3);
	}
}
