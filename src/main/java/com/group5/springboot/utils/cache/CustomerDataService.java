package com.group5.springboot.utils.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataService {

	@Cacheable(cacheNames = "addresses", key = "#customer.name")
	public String getAddress() {
		return "";
	}
}
