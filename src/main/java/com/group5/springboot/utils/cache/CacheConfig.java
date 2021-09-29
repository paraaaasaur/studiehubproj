package com.group5.springboot.utils.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration // Implies that this class belongs to configuration group of Spring Bean.
@EnableCaching
public class CacheConfig {
	
	public static final String CAFFEINE_CACHE_MANAGER = "caffeineCacheManager";
	
	public static final String REDIS_CACHE_MANAGER = "redisCacheManager";
	
	/**
	 * Caffeine Cache Manager.
	 **/
	public CacheManager caffeineCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(Caffeine.newBuilder());
		
		return cacheManager;
	}
	
}
