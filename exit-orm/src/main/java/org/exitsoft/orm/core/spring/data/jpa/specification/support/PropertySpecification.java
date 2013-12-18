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
package org.exitsoft.orm.core.spring.data.jpa.specification.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.spring.data.jpa.JpaRestrictionBuilder;
import org.exitsoft.orm.core.spring.data.jpa.specification.SpecificationEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * 实现spring data jpa的{@link Specification}接口，通过该类支持对象属性名查询方法
 * 
 * @author vincent
 *
 * @param <T> orm 对象
 */
public class PropertySpecification<T> implements Specification<T>{
	
	//属性名称
	private String propertyName;
	//值
	private Object value;
	//约束名称
	private String restrictionName;
	
	public PropertySpecification() {
		
	}

	/**
	 * 构造属性名查询Specification
	 * 
	 * @param propertyName 对象属性名
	 * @param value 值
	 */
	public PropertySpecification(String propertyName, Object value) {
		this.propertyName = propertyName;
		this.value = value;
		this.restrictionName = RestrictionNames.EQ;
	}
	
	/**
	 * 构造属性名查询Specification
	 * 
	 * @param propertyName 对象属性名
	 * @param value 值
	 * @param restrictionName 约束名称
	 */
	public PropertySpecification(String propertyName, Object value,String restrictionName) {
		this.propertyName = propertyName;
		this.value = value;
		this.restrictionName = restrictionName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
	 */
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,CriteriaBuilder builder) {
		
		
		Predicate predicate = builder.and(JpaRestrictionBuilder.getRestriction(propertyName, value, restrictionName, new SpecificationEntity(root, query, builder)));
		
		
		return predicate;
	}

}
