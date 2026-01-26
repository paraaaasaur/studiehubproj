package com.group5.springboot.config.test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class AnalysisConfig {
	/** visit {@code http://localhost:8080/swagger-ui/index.html}*/
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("SpringDoc example")
				.description("SpringDoc application")
				.version("1.0.0-snapshot"));
	}
}
