package org.exitsoft.showcase.test;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.tool.EnversSchemaGenerator;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 
 * 通过Hibernate配置文件创建生成表的sql语句到src/test/resources/data/h2/create-table.sql文件中，
 * 该类是对单元测试的模拟创建表起辅助作用
 * 
 * @author vincent
 *
 */
public class CreateDataBaseTableByHibernateConfigFile {
	
	
	public static void main(String[] args) {
		Configuration configuration = new Configuration().configure().setNamingStrategy(new ImprovedNamingStrategy());
		EnversSchemaGenerator generator = new EnversSchemaGenerator(configuration);
		SchemaExport export = generator.export();
		
		export.setFormat(false);
		export.setOutputFile("src/test/resources/data/h2/create-table-new.sql");
		export.create(true, false);
	}
	
}
