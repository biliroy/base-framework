package org.exitsoft.showcase.dao.foundation.audit;

import org.exitsoft.orm.core.hibernate.support.HibernateSupportDao;
import org.exitsoft.showcase.entity.foundation.audit.OperatingRecord;
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
