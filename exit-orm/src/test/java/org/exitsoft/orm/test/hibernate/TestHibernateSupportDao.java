package org.exitsoft.orm.test.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.test.entity.User;
import org.exitsoft.orm.test.simple.hibernate.HibernateDaoSimple;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Transactional
@ActiveProfiles("hibernate")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestHibernateSupportDao {
	
	@Autowired
	private HibernateDaoSimple userDao;
	
	private SessionFactory sessionFactory;
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private DataSource dataSource;
	
	/**
	 * 通过表名计算出表中的总记录数
	 * 
	 * @param tableName 表名
	 * 
	 * @return int
	 */
	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForObject("SELECT COUNT(0) FROM " + tableName,new HashMap<String, Object>(), Integer.class);
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Before
	public void install() throws Exception {
		Fixtures.reloadData(dataSource, "/sample-data.xml");
	}

	@Test
	public void testEntityCount() {
		assertEquals(userDao.entityCount(), 8);
	}
	
	@Test
	public void testFind() {
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_loginName", "m"),
				PropertyFilters.build("EQI_state", "1")
		);
		
		List<User> user = Lists.newArrayList();
		
		user = userDao.findByPropertyFilter(filters);
		assertEquals(user.size(), 4);
		
		user = userDao.findByPropertyFilter(filters, Order.asc("loginName"),Order.desc("state"));
		assertEquals(user.size(), 4);
		assertEquals(user.get(0).getLoginName(), "admin");
		
		user = userDao.findByProperty("loginName", "vincent");
		assertEquals(user.size(), 1);
		
		user = userDao.findByProperty("loginName", "m",RestrictionNames.LIKE);
		assertEquals(user.size(), 4);
		
		user = userDao.findByProperty("loginName", "m",RestrictionNames.LIKE, Order.asc("loginName"),Order.desc("state"));
		assertEquals(user.size(), 4);
		assertEquals(user.get(0).getLoginName(), "admin");
	}

	@Test
	public void testFindUnique() {
		User user = new User();
		
		user = userDao.findUniqueByCriterion(new Criterion[]{
				Restrictions.eq("loginName", "vincent"),
				Restrictions.eq("state", 1)
		});
		assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		user = userDao.findUniqueByProperty("loginName", "vincent");
		assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		user = userDao.findUniqueByProperty("realName", "Mot", RestrictionNames.LIKE);
		assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0003");
	}

	@Test
	public void testFindPage() {
		PageRequest request = new PageRequest();
		request.setOrderBy("loginName,realName,state,id");
		request.setOrderDir(Sort.ASC + "," + Sort.DESC + "," +Sort.ASC + "," + Sort.DESC);
		
		Page<User> page = new Page<User>(request);
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(User.class);
		
		page = userDao.findPage(request, c.add(Restrictions.eq("loginName", "vincent")));
		assertEquals(page.getTotalItems(), 1);
		assertEquals(page.getTotalPages(), 1);
		
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_loginName", "m"),
				PropertyFilters.build("EQI_state", "1")
		);
		
		page = userDao.findPage(request, filters);
		assertEquals(page.getTotalItems(), 4);
		assertEquals(page.getTotalPages(), 1);
		
		page = userDao.findPage(request, "from User u where u.loginName like ?", "%m%");
		assertEquals(page.getTotalItems(), 4);
		assertEquals(page.getTotalPages(), 1);

		Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.loginName like ?1");
		query.setParameter("1", "%m%");
		
		page = userDao.findPage(request, query);
		assertEquals(page.getTotalItems(), 4);
		assertEquals(page.getTotalPages(), 1);

		page = userDao.findPage(request, "from User u where u.loginName like ?1", "%m%");
		assertEquals(page.getTotalItems(), 4);
		assertEquals(page.getTotalPages(), 1);
		
		request.setOrderBy(null);
		request.setOrderDir(null);
		
		page = userDao.findPage(request, "QueryUserMenuJpa","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(page.getTotalItems(), 8);
		assertEquals(page.getTotalPages(), 1);
		
		page = userDao.findPage(request, "QueryUserMenu","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(page.getTotalItems(), 8);
		assertEquals(page.getTotalPages(), 1);
	}

}
