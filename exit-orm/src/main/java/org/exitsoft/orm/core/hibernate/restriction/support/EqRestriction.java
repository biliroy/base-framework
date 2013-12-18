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
package org.exitsoft.orm.core.hibernate.restriction.support;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 等于约束 (from object o where o.value = ?) RestrictionName:EQ
 * <p>
 * 表达式:EQ属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class EqRestriction extends CriterionSingleValueSupport {
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#getRestrictionName()
	 */
	public String getRestrictionName() {
		return RestrictionNames.EQ;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport#getMatchValue(java.lang.String, java.lang.Class)
	 */
	public MatchValue getMatchValue(String matchValue, Class<?> FieldType) {
		
		MatchValue matchValueModel = super.getMatchValue(matchValue, FieldType);
		for (int i = 0; i < matchValueModel.getValues().size(); i++) {
			Object value = matchValueModel.getValues().get(i);
			if (value instanceof String && StringUtils.equals(value.toString(),"null")) {
				matchValueModel.getValues().remove(i);
				matchValueModel.getValues().add(i, null);
			}
		}
		return matchValueModel;
	}
	
	public Criterion build(String propertyName, Object value) {
		
		return value == null ? Restrictions.isNull(propertyName) : Restrictions.eq(propertyName, value);
		
	}
	
}
