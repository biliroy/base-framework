package org.exitsoft.showcase.test.manager;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * 业务单元测试基类
 * 
 * @author vincent
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-core.xml")
public class ManagerTestCaseSupport {
	
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setDataSource(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 通过表名计算出表中的总记录数
	 * 
	 * @param tableName 表名
	 * 
	 * @return int
	 */
	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForObject("SELECT COUNT(0) FROM " + tableName,Integer.class);
	}
	
	/**
	 * 
	 * 每个单元测试用例开始先把模拟数据加载到dataSource中
	 * 
	 * @throws Exception
	 */
	@Before
	public void install() throws Exception {
		executeScript(dataSource,"classpath:data/h2/cleanup-data.sql","classpath:data/h2/insert-data.sql");
	}
	
	/**
	 * 批量执行sql文件
	 * 
	 * @param dataSource　dataSource
	 * @param sqlResourcePaths sql文件路径
	 * 
	 * @throws DataAccessException
	 */
	public void executeScript(DataSource dataSource, String... sqlResourcePaths) throws DataAccessException {

		for (String sqlResourcePath : sqlResourcePaths) {
			JdbcTestUtils.executeSqlScript(jdbcTemplate, resourceLoader, sqlResourcePath, true);
		}
	}
	
	@Test
	public void emptyTestMethod() {
		
	}
	
}
