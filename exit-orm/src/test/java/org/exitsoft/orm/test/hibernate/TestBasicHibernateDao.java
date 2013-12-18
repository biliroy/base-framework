package org.exitsoft.orm.test.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.test.entity.User;
import org.exitsoft.orm.test.simple.hibernate.BasicHiberanteDaoSimple;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
import com.google.common.collect.Maps;

@Transactional
@ActiveProfiles("hibernate")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestBasicHibernateDao {

	@Autowired
	private BasicHiberanteDaoSimple userDao;
	
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
		return jdbcTemplate.queryForObject("SELECT COUNT(0) FROM " + tableName,new HashMap<String, Object>(),Integer.class);
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
	public void testInsert() {
		User entity = new User();
		
		entity.setCreateTime(new Date());
		entity.setLoginName("test");
		entity.setPassword("123456");
		entity.setPinyinCode("pinyin");
		entity.setRealName("测试");
		entity.setState(new Integer(1));
		entity.setWubiCode("wubi");
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		
		userDao.insert(entity);
		sessionFactory.getCurrentSession().flush();
		
		int after = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before + 1, after);
		
	}

	@Test
	public void testInsertAll() {
		User entity1 = new User();
		
		entity1.setCreateTime(new Date());
		entity1.setLoginName("test");
		entity1.setPassword("123456");
		entity1.setPinyinCode("pinyin");
		entity1.setRealName("测试");
		entity1.setState(new Integer(1));
		entity1.setWubiCode("wubi");
		
		User entity2 = new User();
		
		entity2.setCreateTime(new Date());
		entity2.setLoginName("test");
		entity2.setPassword("123456");
		entity2.setPinyinCode("pinyin");
		entity2.setRealName("测试");
		entity2.setState(new Integer(1));
		entity2.setWubiCode("wubi");
		
		List<User> data = Lists.newArrayList(entity1,entity2);
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		
		userDao.insertAll(data);
		sessionFactory.getCurrentSession().flush();
		
		int after = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before + 2, after);
	}

	@Test
	public void testUpdate() {
		User user = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001");
		user.setLoginName("update login name");
		
		userDao.update(user);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
		user = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getLoginName(), "update login name");
	}

	@Test
	public void testUpdateAll() {
		User user1 = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001");
		User user2 = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user1.setLoginName("update user 1 login name");
		user2.setLoginName("update user 2 login name");
		
		List<User> data = Lists.newArrayList(user1,user2);
		
		userDao.updateAll(data);
		
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
		user1 = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001");
		user2 = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		assertEquals(user1.getLoginName(), "update user 1 login name");
		assertEquals(user2.getLoginName(), "update user 2 login name");
	}

	@Test
	public void testSave() {
		User entity = new User();
		
		entity.setCreateTime(new Date());
		entity.setLoginName("test");
		entity.setPassword("123456");
		entity.setPinyinCode("pinyin");
		entity.setRealName("测试");
		entity.setState(new Integer(1));
		entity.setWubiCode("wubi");
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		
		userDao.save(entity);
		sessionFactory.getCurrentSession().flush();
		
		int after = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before + 1, after);
		
		entity.setLoginName("update login name");
		
		userDao.save(entity);
		
		entity = userDao.load(entity.getId());
		
		assertEquals(entity.getLoginName(), "update login name");
	}

	@Test
	public void testSaveAll() {
		User entity1 = new User();
		
		entity1.setCreateTime(new Date());
		entity1.setLoginName("test");
		entity1.setPassword("123456");
		entity1.setPinyinCode("pinyin");
		entity1.setRealName("测试");
		entity1.setState(new Integer(1));
		entity1.setWubiCode("wubi");
		
		User entity2 = new User();
		
		entity2.setCreateTime(new Date());
		entity2.setLoginName("test");
		entity2.setPassword("123456");
		entity2.setPinyinCode("pinyin");
		entity2.setRealName("测试");
		entity2.setState(new Integer(1));
		entity2.setWubiCode("wubi");
		
		List<User> data = Lists.newArrayList(entity1,entity2);
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		
		userDao.saveAll(data);
		sessionFactory.getCurrentSession().flush();
		
		int after = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before + 2, after);
		
		entity1.setLoginName("update user 1 login name");
		entity2.setLoginName("update user 2 login name");
		
		userDao.saveAll(data);
		
		entity1 = userDao.load(entity1.getId());
		entity2 = userDao.load(entity2.getId());
		
		assertEquals(entity1.getLoginName(), "update user 1 login name");
		assertEquals(entity2.getLoginName(), "update user 2 login name");
	}

	@Test
	public void testDelete() {
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		
		userDao.delete("SJDK3849CKMS3849DJCK2039ZMSK0001");
		sessionFactory.getCurrentSession().flush();
		userDao.delete(userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0002"));
		sessionFactory.getCurrentSession().flush();
		
		int aflter = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before, aflter);
	}

	@Test
	public void testDeleteAll() {
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		userDao.deleteAll(Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0001","SJDK3849CKMS3849DJCK2039ZMSK0002"));
		sessionFactory.getCurrentSession().flush();
		int aflter = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before, aflter);
	}

	@Test
	public void testDeleteAllByEntities() {
		
		int before = countRowsInTable("TB_ACCOUNT_USER");
		userDao.deleteAllByEntities(Lists.newArrayList(userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001"),userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0002")));
		sessionFactory.getCurrentSession().flush();
		int aflter = countRowsInTable("TB_ACCOUNT_USER");
		
		assertEquals(before, aflter);
	}

	@Test
	public void testGet() {
		List<User> data = userDao.get(Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0001","SJDK3849CKMS3849DJCK2039ZMSK0002"));
		assertEquals(data.size(), 2);
		
		User user = userDao.get("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getLoginName(), "vincent");
		
		data = userDao.get(new String[] {"SJDK3849CKMS3849DJCK2039ZMSK0001","SJDK3849CKMS3849DJCK2039ZMSK0002"});
		assertEquals(data.size(), 2);
		
	}

	@Test
	public void testLoad() {
		User user = userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getLoginName(), "vincent");
	}

	@Test
	public void testGetAll() {
		List<User> data = userDao.getAll();
		assertEquals(data.size(), 8);
		
		data = userDao.getAll(Order.asc("loginName"),Order.desc("realName"));
		assertEquals(data.size(), 8);
		assertEquals(data.get(0).getLoginName(), "admin");
	}

	@Test
	public void testFindByQuery() {
		List<User> user = Lists.newArrayList();
		
		user = userDao.findByQuery("from User u where u.id=?", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.size(), 1);
		user = userDao.findByQuery("from User u where u.id=?1", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.size(), 1);
		
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		user = userDao.findByQuery("from User u where u.id=:id", map);
		assertEquals(user.size(), 1);
		user = userDao.findByQuery("QueryUserJpa", "vincent");
		assertEquals(user.size(), 1);
		user = userDao.findByQuery("QueryUser", "vincent");
		assertEquals(user.size(), 1);
	}

	@Test
	public void testFindUniqueByQuery() {
		
		User user = new User();
		
		user = userDao.findUniqueByQuery("from User u where u.id=?", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getLoginName(), "vincent");
		user = userDao.findUniqueByQuery("from User u where u.id=?1", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getLoginName(), "vincent");
		
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		user = userDao.findUniqueByQuery("from User u where u.id=:id", map);
		assertEquals(user.getLoginName(), "vincent");
		user = userDao.findUniqueByQuery("QueryUserJpa", "vincent");
		assertEquals(user.getLoginName(), "vincent");
		user = userDao.findUniqueByQuery("QueryUser", "vincent");
		assertEquals(user.getLoginName(), "vincent");
	}

	@Test
	public void testDistinctQuery() {
		List<User> data = Lists.newArrayList();
		
		data = userDao.distinct("select ml from " +
				"User u left join u.roleList rl " +
				"left join rl.menuList ml " +
				"where u.id=? and u.state=?","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
		
		data = userDao.distinct("select ml from " +
				"User u left join u.roleList rl " +
				"left join rl.menuList ml " +
				"where u.id=?1 and u.state=?2","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		map.put("state", 1);
		
		data = userDao.distinct("select ml from " +
				"User u left join u.roleList rl " +
				"left join rl.menuList ml " +
				"where u.id=:id and u.state=:state","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
		
		data = userDao.distinct("select ml from " +
				"User u left join u.roleList rl " +
				"left join rl.menuList ml " +
				"where u.id=:id and u.state=:state","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
		
		data = userDao.distinct("QueryUserMenuJpa","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
		
		data = userDao.distinct("QueryUserMenu","SJDK3849CKMS3849DJCK2039ZMSK0001",1);
		assertEquals(data.size(), 4);
	}

	@Test
	public void testExecuteUpdate() {
		userDao.executeUpdate("update User u set u.password=?,u.state=? where u.id=?", "123456",2,"SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001").getPassword(), "123456");
		sessionFactory.getCurrentSession().clear();
		
		userDao.executeUpdate("update User u set u.password=?1,u.state=?2 where u.id=?3", "23456",2,"SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001").getPassword(), "23456");
		sessionFactory.getCurrentSession().clear();
		
		Map<String, Object> map = Maps.newHashMap();
		map.put("password", "34567");
		map.put("state", 2);
		map.put("id", "SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		userDao.executeUpdate("update User u set u.password=:password,u.state=:state where u.id=:id",map);
		assertEquals(userDao.load("SJDK3849CKMS3849DJCK2039ZMSK0001").getPassword(), "34567");
		
	}
}
