package org.exitsoft.showcase.test.manager.foundation.variable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.exitsoft.common.type.FieldType;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.exitsoft.showcase.entity.foundation.variable.DictionaryCategory;
import org.exitsoft.showcase.service.foundation.SystemVariableManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

/**
 * 测试数据字典管理所有方法
 * 
 * @author vincent
 *
 */
public class TestDataDictionaryManager extends ManagerTestCaseSupport{
	
	@Autowired
	private SystemVariableManager systemVariableManager;
	
	@Test
	public void testGetDataDictionariesByCategoryCode() {
		List<DataDictionary> list = Lists.newArrayList();
		list = systemVariableManager.getDataDictionariesByCategoryCode(SystemDictionaryCode.State);
		assertEquals(list.size(),3);
		list = systemVariableManager.getDataDictionariesByCategoryCode(SystemDictionaryCode.State, "1");
		assertEquals(list.size(),2);
		list = systemVariableManager.getDataDictionariesByCategoryCode(SystemDictionaryCode.State, "1","2","3");
		assertEquals(list.size(),0);
		
	}
	
	@Test
	public void testDeleteDataDictionary() {
		
		List<String> ids = new ArrayList<String>();
		CollectionUtils.addAll(ids, new String[]{"402881e437d47b250137d481b6920001","402881e437d47b250137d481dda30002"});
		
		int beforeRow = countRowsInTable("TB_DATA_DICTIONARY");
		systemVariableManager.deleteDataDictionary(ids);
		int afterRow = countRowsInTable("TB_DATA_DICTIONARY");
		
		assertEquals(afterRow, beforeRow - 2);
	}
	
	@Test
	public void testSaveDataDictionary() {
		
		DictionaryCategory category = systemVariableManager.getDictionaryCategory("402881e437d467d80137d46fc0e50001");
		
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setCategory(category);
		dataDictionary.setName("test");
		dataDictionary.setValue("4");
		dataDictionary.setType(FieldType.I.toString());
		dataDictionary.setRemark("*");
		
		int beforeRow = countRowsInTable("TB_DATA_DICTIONARY");
		systemVariableManager.saveDataDictionary(dataDictionary);
		int afterRow = countRowsInTable("TB_DATA_DICTIONARY");
		
		assertEquals(afterRow, beforeRow + 1);
		
	}
	
	
	
}
