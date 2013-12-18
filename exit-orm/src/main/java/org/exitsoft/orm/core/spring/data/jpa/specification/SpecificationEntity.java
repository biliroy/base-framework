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
package org.exitsoft.orm.core.spring.data.jpa.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * jpa查询绑定载体，辅助属性过滤器查询时需要的jpa属性
 * 
 * @author vincent
 *
 */
public class SpecificationEntity {

	private Root<?> root;
	private CriteriaQuery<?> query;
	private CriteriaBuilder builder;
	
	public SpecificationEntity() {
		
	}

	public SpecificationEntity(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		super();
		this.root = root;
		this.query = query;
		this.builder = builder;
	}

	public Root<?> getRoot() {
		return root;
	}

	public void setRoot(Root<?> root) {
		this.root = root;
	}

	public CriteriaQuery<?> getQuery() {
		return query;
	}

	public void setQuery(CriteriaQuery<?> query) {
		this.query = query;
	}

	public CriteriaBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(CriteriaBuilder builder) {
		this.builder = builder;
	}
	
	
	
}
