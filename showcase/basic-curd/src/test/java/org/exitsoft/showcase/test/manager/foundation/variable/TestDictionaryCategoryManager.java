package org.exitsoft.showcase.test.manager.foundation.variable;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.exitsoft.showcase.entity.foundation.variable.DictionaryCategory;
import org.exitsoft.showcase.service.foundation.SystemVariableManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试字典类别管理所有方法
 * 
 * @author vincent
 *
 */
public class TestDictionaryCategoryManager extends ManagerTestCaseSupport {

	@Autowired
	private SystemVariableManager systemVariableManager;
	
	@Test
	public void testSaveDictionaryCategory() {
		DictionaryCategory category = new DictionaryCategory();
		category.setCode("test");
		category.setName("测试");
		category.setRemark("*");
		
		int beforeRow = countRowsInTable("TB_DICTIONARY_CATEGORY");
		systemVariableManager.saveDictionaryCategory(category);
		int afterRow = countRowsInTable("TB_DICTIONARY_CATEGORY");
		
		assertEquals(afterRow, beforeRow + 1);
	}

	@Test
	public void testDeleteDictionaryCategory() {
		
		int beforeRow = countRowsInTable("TB_DICTIONARY_CATEGORY");
		List<String> ids = new ArrayList<String>();
		ids.add("402881e437d47b250137d485274b0005");
		systemVariableManager.deleteDictionaryCategory(ids);
		int afterRow = countRowsInTable("TB_DICTIONARY_CATEGORY");
		
		assertEquals(beforeRow, afterRow + 1);
	}

	@Test
	public void testGetAllDictionaryCategories() {
		List<DictionaryCategory> result = systemVariableManager.getDictionaryCategories();
		assertEquals(5, result.size());
	}

}
