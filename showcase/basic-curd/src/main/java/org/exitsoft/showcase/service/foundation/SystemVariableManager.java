package org.exitsoft.showcase.service.foundation;

import java.util.List;

import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.dao.foundation.variable.DataDictionaryDao;
import org.exitsoft.showcase.dao.foundation.variable.DictionaryCategoryDao;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.exitsoft.showcase.entity.foundation.variable.DictionaryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统字典管理业务逻辑
 * 
 * @author vincent
 *
 */
@Service
@Transactional
public class SystemVariableManager {

	//数据字典数据访问
	@Autowired
	private DataDictionaryDao dataDictionaryDao;
	
	//字典类别数据访问
	@Autowired
	private DictionaryCategoryDao dictionaryCategoryDao;
	
	//---------------------------------------数据字典管理---------------------------------------//
	
	/**
	 * 获取数据字典实体
	 * 
	 * @param id 数据字典id
	 */
	public DataDictionary getDataDictionary(String id) {
		return dataDictionaryDao.load(id);
	}
	
	/**
	 * 保存数据字典
	 * 
	 * @param entity 数据字典实体
	 */
	public void saveDataDictionary(DataDictionary entity) {
		dataDictionaryDao.save(entity);
	}
	
	/**
	 * 删除数据字典
	 * 
	 * @param ids 数据字典id集合
	 */
	public void deleteDataDictionary(List<String> ids) {
		dataDictionaryDao.deleteAll(ids);
	}
	
	/**
	 * 获取数据字典分页对象
	 * 
	 * @param request 分页参数请求
	 * @param filters 属性过滤器集合
	 * 
	 * @return Page
	 */
	public Page<DataDictionary> searchDataDictionaryPage(PageRequest request,List<PropertyFilter> filters) {
		return dataDictionaryDao.findPage(request, filters);
	}
	
	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典列别
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	@Cacheable(value="findByCateGoryCode",
               key="#code.getCode() + '-' + " +
                   "T(org.apache.commons.lang3.StringUtils)." +
                   "join(#ignoreValue, '-')")
	public List<DataDictionary> getDataDictionariesByCategoryCode(SystemDictionaryCode code,String... ignoreValue) {
		return dataDictionaryDao.getByCategoryCode(code, ignoreValue);
	}
	
	//---------------------------------------字典类别管理---------------------------------------//
	
	/**
	 * 获取字典类别实体
	 * 
	 * @param id 数据字典id
	 */
	public DictionaryCategory getDictionaryCategory(String id) {
		return dictionaryCategoryDao.load(id);
	}
	
	/**
	 * 保存字典类别
	 * 
	 * @param entity 字典类别实体
	 */
	public void saveDictionaryCategory(DictionaryCategory entity) {
		dictionaryCategoryDao.save(entity);
	}
	
	/**
	 * 删除字典类别
	 * 
	 * @param ids 字典类别id
	 */
	public void deleteDictionaryCategory(List<String> ids) {
		dictionaryCategoryDao.deleteAll(ids);
	}
	
	/**
	 * 获取字典类别分页对象
	 * 
	 * @param request 分页参数请求
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<DictionaryCategory> searchDictionaryCategoryPage(PageRequest request,List<PropertyFilter> filters) {
		return dictionaryCategoryDao.findPage(request, filters);
	}
	
	/**
	 * 获取所有字典类别
	 * 
	 * @return List
	 */
	public List<DictionaryCategory> getDictionaryCategories() {
		return dictionaryCategoryDao.getAll();
	}
	
	/**
	 * 根据属性过滤器获取或有字典类别
	 * 
	 * @param filters 属性过滤器集合
	 * 
	 * @return List
	 */
	public List<DictionaryCategory> getDictionaryCategories(List<PropertyFilter> filters) {
		return dictionaryCategoryDao.findByPropertyFilter(filters);
	}

}
