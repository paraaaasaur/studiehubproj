package com.group5.springboot.config.dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DevDataSourceConfig {
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new PathResource("dev/db/sqlserver/01-schema-v1.0.0-snapshot.sql"));
		populator.addScript(new PathResource("dev/db/sqlserver/02-data-v1.0.0-release.sql"));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(populator);
		return dataSourceInitializer;
	}
}