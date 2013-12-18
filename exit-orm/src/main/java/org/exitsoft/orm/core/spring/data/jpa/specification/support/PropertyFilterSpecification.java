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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.JpaRestrictionBuilder;
import org.exitsoft.orm.core.spring.data.jpa.specification.SpecificationEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * 实现spring data jpa的{@link Specification}接口，通过该类支持{@link PropertyFilter}以及表达式查询方法
 * 
 * @author vincent
 *
 * @param <T> orm对象
 */
public class PropertyFilterSpecification<T> implements Specification<T> {
	
	private List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
	
	public PropertyFilterSpecification() {
		
	}
	
	/**
	 * 通过属性过滤器构建
	 * 
	 * @param filter 属性过滤器
	 */
	public PropertyFilterSpecification(PropertyFilter filter) {
		this.filters.add(filter);
	}
	
	/**
	 * 通过属性过滤器集合构建
	 * 
	 * @param filters 集合
	 */
	public PropertyFilterSpecification(List<PropertyFilter> filters) {
		this.filters.addAll(filters);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
	 */
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,CriteriaBuilder builder) {
		
		
		List<Predicate> list = new ArrayList<Predicate>();
		
		for (PropertyFilter filter : filters) {
			list.add(JpaRestrictionBuilder.getRestriction(filter,new SpecificationEntity(root, query, builder)));
		}
		
		return list.size() > 0 ? builder.and(list.toArray(new Predicate[list.size()])) : null;
		
	}

	/**
	 * 获取所有属性过滤器
	 * 
	 * @return List
	 */
	public List<PropertyFilter> getFilters() {
		return filters;
	}

	/**
	 * 设置所有属性过滤器
	 * 
	 * @param filters 属性过滤器
	 */
	public void setFilters(List<PropertyFilter> filters) {
		this.filters = filters;
	}
	
}
