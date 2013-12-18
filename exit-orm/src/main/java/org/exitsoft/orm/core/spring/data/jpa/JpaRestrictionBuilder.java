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
package org.exitsoft.orm.core.spring.data.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.EqRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.GeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.GtRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.InRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LLikeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LikeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LtRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.NeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.NinRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.RLikeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.specification.SpecificationEntity;

/**
 * jpa约束捆绑者，将所有的{@link PredicateBuilder}实现类添加到{@link PropertyFilters#getRestrictionsMap()}中，
 * 辅佐PropertyFilterSpecification和RestrictionNameSpecification做创建Predicate操作。
 * 
 * @author vincent
 *
 */
public class JpaRestrictionBuilder{
	
	private static Map<String, PredicateBuilder> predicateBuilders = new HashMap<String, PredicateBuilder>();
	
	static {
		PredicateBuilder eqRestriction = new EqRestriction();
		PredicateBuilder neRestriction = new NeRestriction();
		PredicateBuilder geRestriction = new GeRestriction();
		PredicateBuilder gtRestriction = new GtRestriction();
		PredicateBuilder inRestriction = new InRestriction();
		PredicateBuilder lLikeRestriction = new LLikeRestriction();
		PredicateBuilder leRestriction = new LeRestriction();
		PredicateBuilder likeRestriction = new LikeRestriction();
		PredicateBuilder ltRestriction = new LtRestriction();
		PredicateBuilder notInRestriction = new NinRestriction();
		PredicateBuilder rLikeRestriction = new RLikeRestriction();
		
		predicateBuilders.put(eqRestriction.getRestrictionName(), eqRestriction);
		predicateBuilders.put(neRestriction.getRestrictionName(), neRestriction);
		predicateBuilders.put(geRestriction.getRestrictionName(), geRestriction);
		predicateBuilders.put(inRestriction.getRestrictionName(), inRestriction);
		predicateBuilders.put(gtRestriction.getRestrictionName(), gtRestriction);
		predicateBuilders.put(lLikeRestriction.getRestrictionName(), lLikeRestriction);
		predicateBuilders.put(leRestriction.getRestrictionName(), leRestriction);
		predicateBuilders.put(likeRestriction.getRestrictionName(), likeRestriction);
		predicateBuilders.put(ltRestriction.getRestrictionName(), ltRestriction);
		predicateBuilders.put(rLikeRestriction.getRestrictionName(), rLikeRestriction);
		predicateBuilders.put(notInRestriction.getRestrictionName(), notInRestriction);
	}
	
	/**
	 * 通过属性过滤器创建Predicate
	 * 
	 * @param filter 属性过滤器
	 * @param restrictionModel jpa查询绑定载体
	 * 
	 * @return {@link Predicate}
	 */
	public static Predicate getRestriction(PropertyFilter filter,SpecificationEntity entity) {
		if (!predicateBuilders.containsKey(filter.getRestrictionName())) {
			throw new IllegalArgumentException("找不到约束名:" + filter.getRestrictionName());
		}
		PredicateBuilder predicateBuilder  = predicateBuilders.get(filter.getRestrictionName());
		return predicateBuilder.build(filter,entity);
	}

	/**
	 * 通过属性名称，值，约束条件创建Predicate
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束条件
	 * @param model jpa查询绑定载体
	 * 
	 * @return {@link Predicate}
	 */
	public static Predicate getRestriction(String propertyName, Object value,String restrictionName,SpecificationEntity entity) {
		if (!predicateBuilders.containsKey(restrictionName)) {
			throw new IllegalArgumentException("找不到约束名:" + restrictionName);
		}
		PredicateBuilder predicateBuilder  = predicateBuilders.get(restrictionName);
		return predicateBuilder.build(propertyName, value, entity);
	}

	/**
	 * 获取所有的条件约束
	 * 
	 * @return Map
	 */
	public static Map<String, PredicateBuilder> getPredicateBuilders() {
		return predicateBuilders;
	}

	/**
	 * 设置所有的条件约束
	 * 
	 * @param 条件约束
	 */
	public static void setPredicateBuilders(Map<String, PredicateBuilder> predicateBuilders) {
		JpaRestrictionBuilder.predicateBuilders = predicateBuilders;
	}
	
	
}
