package com.group5.springboot.config.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class TestDataSourceConfig {
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new PathResource("dev/db/sqlserver/01-schema-v1.0.0-snapshot.sql"));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(populator);
		return dataSourceInitializer;
	}
}