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
package org.exitsoft.orm.core.spring.data.jpa.factory;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.exitsoft.orm.core.spring.data.jpa.repository.support.JpaSupportRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.LockModeRepositoryPostProcessor;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * JPA specific generic repository factory.
 * 
 * @author vincent
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BasicJpaRepositoryFactory extends JpaRepositoryFactory{

	private EntityManager entityManager;
	
	/**
	 * Creates a new {@link BasicJpaRepositoryFactory}.
	 * 
	 * @param entityManager must not be {@literal null}
	 */
	public BasicJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
		
	}
	
	public void init() {}
	
	/**
	 * Returns whether the given repository interface requires a QueryDsl specific implementation to be chosen.
	 * 
	 * @param repositoryInterface
	 * 
	 * @return boolean
	 */
	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

		return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getTargetRepository(org.springframework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected Object getTargetRepository(RepositoryMetadata metadata) {
		
		Class<?> repositoryInterface = metadata.getRepositoryInterface();
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

		SimpleJpaRepository<?, ?> repo = null;
		
		if (isQueryDslExecutor(repositoryInterface)) {
			repo = new QueryDslJpaRepository(entityInformation, entityManager);
		} else {
			repo = new JpaSupportRepository(entityInformation, entityManager);
		}
		
		repo.setLockMetadataProvider(LockModeRepositoryPostProcessor.INSTANCE.getLockMetadataProvider());

		return repo;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getRepositoryBaseClass(org.springframework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
			return QueryDslJpaRepository.class;
		} else {
			return JpaSupportRepository.class;
		}
	}
}
