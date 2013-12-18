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
package org.exitsoft.orm.core.spring.data.jpa.repository.support;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.annotation.StateDelete;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.orm.core.spring.data.jpa.specification.Specifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link BasicJpaRepository}接口实现类，并在{@link SimpleJpaRepository}基础上扩展,包含对{@link PropertyFilter}的支持。或其他查询的支持,
 * 重写了{@link SimpleJpaRepository#save(Object)}和{@link SimpleJpaRepository#delete(Object)}方法，支持@StateDelete注解和@ConvertProperty注解
 * 
 * @author vincent
 *
 * @param <T> ORM对象
 * @param <ID> 主键Id类型
 */
@SuppressWarnings("unchecked")
public class JpaSupportRepository<T, ID extends Serializable>  extends SimpleJpaRepository<T, ID> implements BasicJpaRepository<T, ID>{
	
	private EntityManager entityManager;
	private JpaEntityInformation<T, ?> entityInformation;
	
	public JpaSupportRepository(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}
	
	public JpaSupportRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityInformation = entityInformation;
		this.entityManager = em;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(S)
	 */
	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		
		if (entityInformation.isNew(entity)) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}
	
	 /*
	  * (non-Javadoc)
	  * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#delete(java.lang.Object)
	  */
	@Override
	@Transactional
	public void delete(T entity) {
		Class<?> entityClass = ReflectionUtils.getTargetClass(entity);
		StateDelete stateDelete = ReflectionUtils.getAnnotation(entityClass,StateDelete.class);
		if (stateDelete != null) {
			Object value = ConvertUtils.convertToObject(stateDelete.value(), stateDelete.type().getValue());
			ReflectionUtils.invokeSetterMethod(entity, stateDelete.propertyName(), value);
			save(entity);
		} else {
			super.delete(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.util.List)
	 */
	@Override
	public List<T> findAll(List<PropertyFilter> filters) {
		return findAll(filters,(Sort)null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.util.List, org.springframework.data.domain.Sort)
	 */
	@Override
	public List<T> findAll(List<PropertyFilter> filters, Sort sort) {
		
		return findAll(Specifications.get(filters), sort);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(org.springframework.data.domain.Pageable, java.util.List)
	 */
	@Override
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters) {
		
		return findAll(Specifications.get(filters),pageable);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<T> findAll(String propertyName, Object value) {
		
		return findAll(propertyName, value, (Sort)null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.Object, org.springframework.data.domain.Sort)
	 */
	@Override
	public List<T> findAll(String propertyName, Object value, Sort sort) {
		return findAll(propertyName, value, sort, RestrictionNames.EQ);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public List<T> findAll(String propertyName, Object value,String restrictionName) {
		return findAll(propertyName, value, (Sort)null, restrictionName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.Object, org.springframework.data.domain.Sort, java.lang.String)
	 */
	@Override
	public List<T> findAll(String propertyName, Object value, Sort sort,String restrictionName) {
		
		return findAll(Specifications.get(propertyName, value, restrictionName),sort);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.util.List)
	 */
	@Override
	public T findOne(List<PropertyFilter> filters) {
		return findOne(Specifications.get(filters));
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.lang.String, java.lang.Object)
	 */
	@Override
	public T findOne(String propertyName, Object value) {
		return findOne(propertyName, value, RestrictionNames.EQ);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public T findOne(String propertyName, Object value, String restrictionName) {
		return findOne(Specifications.get(propertyName, value, restrictionName));
	}
	
	
}
