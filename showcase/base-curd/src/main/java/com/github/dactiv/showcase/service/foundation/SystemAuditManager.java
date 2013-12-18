package com.github.dactiv.showcase.service.foundation;

import java.util.List;

import com.github.dactiv.orm.core.Page;
import com.github.dactiv.orm.core.PageRequest;
import com.github.dactiv.orm.core.PropertyFilter;
import com.github.dactiv.showcase.dao.foundation.audit.OperatingRecordDao;
import com.github.dactiv.showcase.entity.foundation.audit.OperatingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统审计业务逻辑
 * 
 * @author maurice
 *
 */
@Service
@Transactional
public class SystemAuditManager {

	@Autowired
	private OperatingRecordDao operatingRecordDao;
	
	//---------------------------------------操作记录管理---------------------------------------//
	
	/**
	 * 获取操作记录实体
	 * 
	 * @param id 操作记录id
	 */
	public OperatingRecord getOperatingRecord(String id) {
		return operatingRecordDao.load(id);
	}
	
	/**
	 * 保存操作记录
	 * 
	 * @param entity 操作记录实体
	 */
	public void insertOperatingRecord(OperatingRecord entity) {
		operatingRecordDao.insert(entity);
	}
	
	/**
	 * 获取操作记录分页对象
	 * 
	 * @param request 分页参数请求
	 * @param filters 属性过滤器集合
	 * 
	 * @return Page
	 */
	public Page<OperatingRecord> searchOperatingRecordPage(PageRequest request,List<PropertyFilter> filters) {
		return operatingRecordDao.findPage(request, filters);
	}
	
}
