package com.github.dactiv.showcase.dao.foundation.audit;

import com.github.dactiv.orm.core.hibernate.support.HibernateSupportDao;
import com.github.dactiv.showcase.entity.foundation.audit.OperatingRecord;
import org.springframework.stereotype.Repository;

/**
 * 操作记录数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class OperatingRecordDao extends HibernateSupportDao<OperatingRecord, String>{
	
}
