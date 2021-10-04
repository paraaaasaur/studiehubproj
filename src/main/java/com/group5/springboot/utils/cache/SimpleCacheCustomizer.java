package com.group5.springboot.utils.cache;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

public class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

	@Override
	public void customize(ConcurrentMapCacheManager cacheManager) {
		cacheManager.setCacheNames(Arrays.asList("orders", "carts"));
		
	}

}
