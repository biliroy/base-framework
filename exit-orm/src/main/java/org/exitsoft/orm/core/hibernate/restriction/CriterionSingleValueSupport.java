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

import java.util.ArrayList;
import java.util.List;

import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.CriterionBuilder;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * 处理{@link PropertyFilter#getMatchValue()}的基类，本类对3种值做处理
 * <p>
 * 1.值等于正常值的，如："amdin"，会产生的squall为:property = 'admin'
 * </p>
 * <p>
 * 2.值等于或值的，如："admin_OR_vincent"，会产生的sql为:property = 'admin' or property = 'vincent'
 * </p>
 * <p>
 * 3.值等于与值的,如:"admin_AND_vincent"，会产生的sql为:property = 'admin' and property = 'vincent'
 * </p>
 * 
 * @author vincent
 *
 */
public abstract class CriterionSingleValueSupport implements CriterionBuilder{
	
	//or值分隔符
	private String orValueSeparator = "|";
	//and值分隔符
	private String andValueSeparator = ",";
	
	public CriterionSingleValueSupport() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#build(org.exitsoft.orm.core.PropertyFilter)
	 */
	public Criterion build(PropertyFilter filter) {
		String matchValue = filter.getMatchValue();
		Class<?> FieldType = filter.getFieldType();
		
		MatchValue matchValueModel = getMatchValue(matchValue, FieldType);
		
		Junction criterion = null;
		
		if (matchValueModel.hasOrOperate()) {
			criterion = Restrictions.disjunction();
		} else {
			criterion = Restrictions.conjunction();
		}
		
		for (Object value : matchValueModel.getValues()) {
			
			if (filter.hasMultiplePropertyNames()) {
				List<Criterion> disjunction = new ArrayList<Criterion>();
				for (String propertyName:filter.getPropertyNames()) {
					disjunction.add(build(propertyName,value));
				}
				criterion.add(Restrictions.or(disjunction.toArray(new Criterion[disjunction.size()])));
			} else {
				criterion.add(build(filter.getSinglePropertyName(),value));
			}
			
		}
		
		return criterion;
	}
	
	
	/**
	 * 获取值对比模型
	 * 
	 * @param matchValue 值
	 * @param FieldType 值类型
	 * 
	 * @return {@link MatchValue}
	 */
	public MatchValue getMatchValue(String matchValue,Class<?> FieldType) {
		return MatchValue.createMatchValueModel(matchValue, FieldType,andValueSeparator,orValueSeparator);
	}

	/**
	 * 获取and值分隔符
	 * 
	 * @return String
	 */
	public String getAndValueSeparator() {
		return andValueSeparator;
	}

	/**
	 * 设置and值分隔符
	 * @param andValueSeparator and值分隔符
	 */
	public void setAndValueSeparator(String andValueSeparator) {
		this.andValueSeparator = andValueSeparator;
	}
	
}
