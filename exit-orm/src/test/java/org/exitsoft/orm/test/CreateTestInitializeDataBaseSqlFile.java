package org.exitsoft.orm.test;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.tool.EnversSchemaGenerator;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class CreateTestInitializeDataBaseSqlFile {
	
	public static void main(String[] args) {
		
		Configuration configuration = new Configuration().configure().setNamingStrategy(new ImprovedNamingStrategy());
		EnversSchemaGenerator generator = new EnversSchemaGenerator(configuration);
		SchemaExport export = generator.export();
		
		export.setFormat(false);
		export.setOutputFile("src/test/resources/h2schma.sql");
		export.create(true, false);
	}
}
