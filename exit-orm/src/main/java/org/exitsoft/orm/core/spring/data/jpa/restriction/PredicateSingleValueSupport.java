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
package org.exitsoft.orm.core.spring.data.jpa.restriction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder;
import org.exitsoft.orm.core.spring.data.jpa.specification.SpecificationEntity;
import org.exitsoft.orm.core.spring.data.jpa.specification.Specifications;


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
public abstract class PredicateSingleValueSupport implements PredicateBuilder{
	
	//or值分隔符
	private String orValueSeparator = "|";
	//and值分隔符
	private String andValueSeparator = ",";
	
	public PredicateSingleValueSupport() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder#build(org.exitsoft.orm.core.PropertyFilter, javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
	 */
	public Predicate build(PropertyFilter filter,SpecificationEntity entity) {

		String matchValue = filter.getMatchValue();
		Class<?> FieldType = filter.getFieldType();
		
		MatchValue matchValueModel = getMatchValue(matchValue, FieldType);
		
		Predicate predicate = null;
		
		if (matchValueModel.hasOrOperate()) {
			predicate = entity.getBuilder().disjunction();
		} else {
			predicate = entity.getBuilder().conjunction();
		}
		
		for (Object value : matchValueModel.getValues()) {
			if (filter.hasMultiplePropertyNames()) {
				for (String propertyName:filter.getPropertyNames()) {
					predicate.getExpressions().add(build(propertyName, value, entity));
				}
			} else {
				predicate.getExpressions().add(build(filter.getSinglePropertyName(), value, entity));
			}
		}
		
		return predicate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder#build(java.lang.String, java.lang.Object, org.exitsoft.orm.core.spring.data.jpa.JpaBuilderModel)
	 */
	public Predicate build(String propertyName, Object value,SpecificationEntity entity) {
		
		return build(Specifications.getPath(propertyName, entity.getRoot()),value,entity.getBuilder());
	}
	
	/**
	 * 
	 * 获取Jpa的约束标准
	 * 
	 * @param expression 属性路径表达式
	 * @param value 值
	 * @param builder CriteriaBuilder
	 * 
	 * @return {@link Predicate}
	 */
	public abstract Predicate build(Path<?> expression,Object value,CriteriaBuilder builder);
	
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
