package com.group5.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.group5.springboot.config.StorageConfigProperties.MEDIA_URL_BASE;
import static com.group5.springboot.config.StorageConfigProperties.UPLOAD_NODE;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final String STORAGE_ROOT_PATH;


	@Autowired
	public WebConfig(StorageConfigProperties props) {
		this.STORAGE_ROOT_PATH = props.getRootAbs();
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/" + MEDIA_URL_BASE + "/**")
				.addResourceLocations("file:" + STORAGE_ROOT_PATH + "/" + UPLOAD_NODE + "/");
	}
}