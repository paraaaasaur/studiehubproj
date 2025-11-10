package com.group5.springboot.config.dev;

import com.group5.springboot.config.StorageConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
@ConfigurationProperties(prefix = "app.dev.storage")
public class DevStorageConfigProperties extends StorageConfigProperties {}