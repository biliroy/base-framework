package org.exitsoft.showcase.dao.foundation.variable;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.hibernate.support.HibernateSupportDao;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

/**
 * 数据字典数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class DataDictionaryDao extends HibernateSupportDao<DataDictionary, String>{
	
	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典列别
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	public List<DataDictionary> getByCategoryCode(SystemDictionaryCode code,String... ignoreValue) {
		StringBuffer hql = new StringBuffer("from DataDictionary dd where dd.category.code = ?");
		
		List<String> args = Lists.newArrayList(code.getCode());
		
		if (ArrayUtils.isNotEmpty(ignoreValue)) {
			
			String[] qm = new String[ignoreValue.length];
			
			for (int i = 0; i < ignoreValue.length; i++) {
				qm[i] = "?";
			}
			
			CollectionUtils.addAll(args, ignoreValue);
			hql.append(MessageFormat.format(" and dd.value not in({0})", StringUtils.join(qm,",")));
			
		}
		
		return findByQuery(hql.toString(), args.toArray());
	}
	
}
