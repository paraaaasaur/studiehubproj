package com.group5.springboot.utils.cache;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.group5.springboot.model.cart.OrderInfo;

@Configuration // Implies that this class belongs to configuration group of Spring Bean.
@EnableCaching
@CacheConfig
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
          new ConcurrentMapCache("directory"), 
          new ConcurrentMapCache("addresses")));
        return cacheManager;
    }
	
	// ex 1 simple case
	@Cacheable(cacheNames = "addresses")
	public String getAddress001(OrderInfo orderInfo) {
		return "cachedAddress001";
	}
	// ex 2: multiple caches 
	@Cacheable(cacheNames = {"addresses", "directory"})
	public String getAddress002(OrderInfo orderInfo) {
		return "cachedAddressWithMultipleCaches002";
	}
	
	@CacheEvict(cacheNames = "addresses", allEntries = true)
	// 依據什麼去判定「陳舊的」值？
	public String getAddress003(OrderInfo orderInfo) {
		return "cachedAddressForEviction003";
	}

	// This method will always be executed and the result cached:
	@CachePut(cacheNames = "addresses")
	public String getAddress004(OrderInfo orderInfo) {
		return "cachedAddressPutting004";
	}
	
	// More precise control - by cache parameter
	// SpEL
	@CachePut(cacheNames = "addresses", condition = "#customer.name=='Tom'")
	public String getAddress00401(OrderInfo orderInfo) {
		return "cachedAddressPutting00401";
	}
	
	// More precise control - by cache parameter
	@CachePut(cacheNames = "addresses", unless = "#result.length()<64")
	public String getAddress00402(OrderInfo orderInfo) {
		return "cachedAddressPutting00402";
	}
	
	@Caching(evict = {
			@CacheEvict(cacheNames = "addresses"),
			@CacheEvict(cacheNames = "directory", key = "customer.name")
	})
	public String getAddress005(OrderInfo orderInfo) {
		return "groupingAnnotationsForCache";
	}
	
	
}
