/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exitsoft.orm.core.hibernate.restriction;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

/**
 * 对{@link PropertyFilter#getMatchValue()}的特殊情况值做处理，例如 in, not in, between的多值情况,
 * 该类值处理一种情况
 * 
 * <p>
 * 	例如:
 * </p>
 * 
 * INI_property = "1,2,3,4";
 * <p>
 * 会产生的sql为: property in (1,2,3,4)
 * 
 * @author vincent
 *
 */
public abstract class CriterionMultipleValueSupport extends CriterionSingleValueSupport{
	
	/**
	 * 将得到值与指定分割符号,分割,得到数组
	 *  
	 * @param value 值
	 * @param type 值类型
	 * 
	 * @return Object
	 */
	public Object convertMatchValue(String value, Class<?> type) {
		Assert.notNull(value,"值不能为空");
		String[] result = StringUtils.splitByWholeSeparator(value, getAndValueSeparator());
		
		return  ConvertUtils.convertToObject(result,type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport#build(org.exitsoft.orm.core.PropertyFilter)
	 */
	public Criterion build(PropertyFilter filter) {
		Object value = convertMatchValue(filter.getMatchValue(), filter.getFieldType());
		Criterion criterion = null;
		if (filter.hasMultiplePropertyNames()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String propertyName:filter.getPropertyNames()) {
				disjunction.add(build(propertyName,value));
			}
			criterion = disjunction;
		} else {
			criterion = build(filter.getSinglePropertyName(),value);
		}
		return criterion;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#build(java.lang.String, java.lang.Object)
	 */
	public Criterion build(String propertyName, Object value) {
		
		return buildRestriction(propertyName, (Object[])value);
	}
	
	
	/**
	 * 
	 * 获取Hibernate的约束标准
	 * 
	 * @param propertyName 属性名
	 * @param values 值
	 * 
	 * @return {@link Criterion}
	 */
	public abstract Criterion buildRestriction(String propertyName,Object[] values);
}
