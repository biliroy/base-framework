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
package org.exitsoft.orm.core.hibernate.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.hibernate.CriterionBuilder;
import org.exitsoft.orm.core.hibernate.HibernateRestrictionBuilder;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.AbstractQueryImpl;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * {@link BasicHibernateDao}基础扩展类。包含对{@link PropertyFilter}的支持。或其他查询的支持
 * 
 * @author vincent
 *
 * @param <T> ORM对象
 * @param <PK> 主键Id类型
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class HibernateSupportDao<T,PK extends Serializable> extends BasicHibernateDao<T, PK>{

	public HibernateSupportDao(){

	}

	public HibernateSupportDao(Class<T> entityClass){
		super(entityClass);
	}

	
	/**
	 * 获取实体的总记录数
	 * 
	 * @return long
	 */
	public long entityCount(PropertyFilter...filters) {
		return (Long) createCriteria(Lists.newArrayList(filters)).setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 * 
	 * @param c Criteria对象
	 * 
	 * @return long
	 */
	protected long countCriteriaResult( Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) ReflectionUtils.getFieldValue(impl,"orderEntries");
			ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}

		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return totalCount;
	}

	/**
	 * 根据{@link PropertyFilter}创建Criteria
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(List<PropertyFilter> filters,Order ...orders) {

		Criteria criteria = createCriteria();

		if (CollectionUtils.isEmpty(filters)) {
			return criteria;
		}
		for (PropertyFilter filter : filters) {
			criteria.add(createCriterion(filter));
		}
		setOrderToCriteria(criteria, orders);
		return criteria;
	}

	/**
	 * 通过{@link PropertyFilter} 创建 Criterion
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Criterion}
	 */
	protected Criterion createCriterion(PropertyFilter filter) {
		if (filter == null) {
			return null;
		}
		return HibernateRestrictionBuilder.getRestriction(filter);
	}

	/**
	 * 根据{@link PropertyFilter} 查询全部
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return List
	 */
	public List<T> findByPropertyFilter(List<PropertyFilter> filters,Order... orders) {
		
		return createCriteria(filters,orders).list();
	}

	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * 
	 * @return List
	 */
	public List<T> findByProperty(String propertyName,Object value) {
		return findByProperty(propertyName, value, RestrictionNames.EQ);
	}

	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param restrictionName 约束名称,参考{@link CriterionBuilder}的实现类
	 * 
	 * @return List
	 */
	public List<T> findByProperty(String propertyName,Object value,String restrictionName,Order ...orders) {
		Criterion criterion = HibernateRestrictionBuilder.getRestriction(propertyName, value, restrictionName);
		Criteria criteria = createCriteria(criterion);
		setOrderToCriteria(criteria, orders);
		return createCriteria(criterion).list();
	}

	/**
	 * 通过criterion数组查询全部
	 * 
	 * @param criterions criterion数组
	 * 
	 * @return Object
	 */
	public T findByCriterion(Criterion[] criterions,Order ...orders){
		Criteria criteria = createCriteria(criterions);
		setOrderToCriteria(criteria, orders);
		return (T)criteria.uniqueResult();
	}

	/**
	 * 通过{@link PropertyFilter} 查询单个orm实体
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return Object
	 * 
	 */
	public T findUniqueByPropertyFilter(List<PropertyFilter> filters) {
		return (T) createCriteria(filters).uniqueResult();
	}

	/**
	 * 通过criterion数组查询单个orm实体
	 * 
	 * @param criterions criterion数组
	 * 
	 * @return Object
	 */
	public T findUniqueByCriterion(Criterion[] criterions){
		Criteria criteria = createCriteria(criterions);
		return (T)criteria.uniqueResult();
	}

	/**
	 * 通过orm实体的属性名称查询单个orm实体
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * 
	 * @return Object
	 */
	public T findUniqueByProperty(String propertyName,Object value) {
		return findUniqueByProperty(propertyName,value,RestrictionNames.EQ);
	}

	/**
	 * 通过orm实体的属性名称查询单个orm实体
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束名称 参考{@link CriterionBuilder}的所有实现类
	 * 
	 * @return Object
	 */
	public T findUniqueByProperty(String propertyName,Object value,String restrictionName) {
		Criterion criterion = HibernateRestrictionBuilder.getRestriction(propertyName, value, restrictionName);
		Criteria criteria = createCriteria(criterion);
		return (T) criteria.uniqueResult();
	}

	/**
	 * 通过{@link PropertyFilter}和分页请求参数获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param filters 属性过滤器集合
	 *
	 * @return {@link Page}
	 */
	public Page<T> findPage(PageRequest request,List<PropertyFilter> filters) {
		Criteria c = createCriteria(filters);
		return findPage(request,c);
	}

	/**
	 * 根据分页参数与Criteria获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param c Criteria对象
	 * 
	 * @return {@link Page}
	 */
	public Page<T> findPage(PageRequest request, Criteria c) {

		Page<T> page = new Page<T>(request);

		if (request == null) {
			return page;
		}

		if (request.isCountTotal()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalItems(totalCount);
		}

		setPageRequestToCriteria(c, request);

		List result = c.list();
		page.setResult(result); 

		return page;
	}

	/**
	 * 通过分页参数与HQL语句获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param queryOrNamedQuery hql 或者Hibernate的NamedQuery
	 * @param values 值
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request,String queryOrNamedQuery,Object... values) {

		Query query = createQuery(queryOrNamedQuery, values);

		return findPage(request,query);
	}

	/**
	 * 通过分页参数与HQL语句获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param queryString HQL语句
	 * @param values 值
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request, String queryString,Map<String,Object> values) {

		Query query = createQuery(queryString, values);

		return findPage(request, query);
	}

	
	/**
	 * 根据分页请求参数与Query获取分页请求对象
	 * 
	 * @param request 分页请求参数对象
	 * @param query Hibernate Query
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request, Query query) {
		Page<X> page = new Page<X>(request);

		if (request == null) {
			return page;
		}
		
		AbstractQueryImpl impl = (AbstractQueryImpl) query;
		
		String queryString = setPageRequestToHql(impl.getQueryString(),request);
		ReflectionUtils.setFieldValue(impl, "queryString", queryString);
		
		if (request.isCountTotal()) {
			long totalCount = 0;
			
			if (impl.hasNamedParameters()) {
				Map<String,TypedValue> map = ReflectionUtils.getFieldValue(impl, "namedParameters");
				Map<String, Object> values = new HashMap<String, Object>();
				
				for (Map.Entry<String, TypedValue> entry:map.entrySet()) {
					values.put(entry.getKey(), entry.getValue().getValue());
				}
				
				totalCount = countHqlResult(impl.getQueryString(), values);
				
			} else {
				List<Object> values = ReflectionUtils.invokeGetterMethod(impl, "values");
				totalCount = countHqlResult(impl.getQueryString(), values.toArray());
				
			}
			
			page.setTotalItems(totalCount);
		}

		impl.setFirstResult(request.getOffset());
		impl.setMaxResults(request.getPageSize());
		
		List result = impl.list();
		page.setResult(result);

		return page;
	}
	
	/**
	 * 在HQL的后面添加分页参数定义的orderBy, 辅助函数.
	 */
	protected String setPageRequestToHql( String hql, PageRequest pageRequest) {
		
		if (CollectionUtils.isEmpty(pageRequest.getSort())) {
			return hql;
		}
		
		StringBuilder builder = new StringBuilder(hql);
		builder.append(" order by");

		for (Sort orderBy : pageRequest.getSort()) {
			builder.append(String.format(" %s %s,", orderBy.getProperty(), orderBy.getDir()));
		}

		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}


	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 * 
	 * @param c Hibernate Criteria
	 * @param pageRequest 分页请求参数
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria setPageRequestToCriteria( Criteria c,  PageRequest pageRequest) {
		Assert.isTrue(pageRequest.getPageSize() > 0, "分页大小必须大于0");

		c.setFirstResult(pageRequest.getOffset());
		c.setMaxResults(pageRequest.getPageSize());

		if (pageRequest.isOrderBySetted()) {
			for (Sort sort : pageRequest.getSort()) {
				Order order = null;
				if (sort.getDir().equals(Sort.ASC)) {
					order = Order.asc(sort.getProperty());
				} else {
					order = Order.desc(sort.getProperty());
				}
				c.addOrder(order);
			}
		}
		
		return c;
	}

}