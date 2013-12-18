package org.exitsoft.orm.test.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.spring.data.jpa.specification.Specifications;
import org.exitsoft.orm.test.entity.User;
import org.exitsoft.orm.test.simple.jpa.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

@ActiveProfiles("spring-data-jpa")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestJpaSupportRepository {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Before
	public void install() throws Exception {
		Fixtures.reloadData(dataSource, "/sample-data.xml");
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testAllRestriction() {
		List<User> userList = Lists.newArrayList();

		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQD_createTime", "2012-08-12")));
		Assert.assertEquals(userList.size(), 8);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_wubiCode", null)));
		Assert.assertEquals(userList.size(), 1);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_wubiCode", "")));
		Assert.assertEquals(userList.size(),6);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_wubiCode", "123")));
		Assert.assertEquals(userList.size(), 1);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("NES_wubiCode", null)));
		Assert.assertEquals(userList.size(), 7);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("NES_wubiCode", "")));
		Assert.assertEquals(userList.size(), 1);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("NES_wubiCode", "123")));
		Assert.assertEquals(userList.size(), 6);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("LIKES_loginName", "m")));
		Assert.assertEquals(userList.size(), 4);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("RLIKES_loginName", "m")));
		Assert.assertEquals(userList.size(), 3);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("LLIKES_loginName", "n")));
		Assert.assertEquals(userList.size(), 1);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("LEI_state", "1")));
		Assert.assertEquals(userList.size(), 8);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("LTI_state", "2")));
		Assert.assertEquals(userList.size(), 8);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("GEI_state", "1")));
		Assert.assertEquals(userList.size(), 8);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("GTI_state", "0")));
		Assert.assertEquals(userList.size(), 8);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("INS_loginName", "admin,vincent")),new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 2);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("NINS_loginName", "admin,vincent")));
		Assert.assertEquals(userList.size(), 6);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_loginName","admin|vincent")));
		Assert.assertEquals(userList.size(), 2);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_loginName","admin,vincent")));
		Assert.assertEquals(userList.size(),0);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_loginName","admin,null")));
		Assert.assertEquals(userList.size(),0);
		
		userList = userRepository.findAll(Specifications.get(PropertyFilters.build("EQS_loginName_OR_realName","null|admin")));
		Assert.assertEquals(userList.size(), 1);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testFindAll() {
		List<User> userList = Lists.newArrayList();
		
		//---------------------------------------------Expressions test--------------------------------------------------//
		
		userList = userRepository.findAll(Specifications.get(Lists.newArrayList(
				PropertyFilters.build("EQS_loginName", "admin"),
				PropertyFilters.build("EQS_realName", "admin")
		)));

		Assert.assertEquals(userList.size(), 1);
		
		userList = userRepository.findAll(Specifications.get(Lists.newArrayList(
				PropertyFilters.build("LIKES_loginName", "m"),
				PropertyFilters.build("EQI_state", "1")
		)));
		Assert.assertEquals(userList.size(), 4);
		
		userList = userRepository.findAll(Specifications.get(Lists.newArrayList(
				PropertyFilters.build("LIKES_loginName", "m"),
				PropertyFilters.build("EQI_state", "1")
		)),new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 4);
		
		
		Pageable pageable = new PageRequest(1, 2);
		Page<User> page = userRepository.findAll(Specifications.get(Lists.newArrayList(PropertyFilters.build("EQI_state", "1"))),pageable);
		Assert.assertEquals(page.getContent().size(), 2);
		Assert.assertEquals(page.getTotalPages(), 4);
		Assert.assertEquals(page.getTotalElements(), 8);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testFindOne() {
		User user = new User();
		
		user = userRepository.findOne(Specifications.get(Lists.newArrayList(PropertyFilters.build("EQS_loginName", "admin"))));
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
	}
}
