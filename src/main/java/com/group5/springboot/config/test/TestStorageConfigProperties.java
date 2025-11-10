package com.group5.springboot.config.test;

import com.group5.springboot.config.StorageConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
@ConfigurationProperties(prefix = "app.test.storage")
public class TestStorageConfigProperties extends StorageConfigProperties {}