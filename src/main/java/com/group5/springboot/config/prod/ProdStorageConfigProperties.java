package com.group5.springboot.config.prod;

import com.group5.springboot.config.StorageConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
@ConfigurationProperties(prefix = "app.prod.storage")
public class ProdStorageConfigProperties extends StorageConfigProperties {}