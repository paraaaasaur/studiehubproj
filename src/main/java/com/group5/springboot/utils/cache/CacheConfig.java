package com.group5.springboot.utils.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import com.group5.springboot.model.cart.OrderInfo;

@Configuration // Implies that this class belongs to configuration group of Spring Bean.
@EnableCaching
public class CacheConfig {

	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("addresses");
	}
	
	@Cacheable(cacheNames = "addresses")
	public String getAddress001(OrderInfo orderInfo) {
		return "cachedAddress001";
	}
	
	@Cacheable(cacheNames = {"addresses", "directory"})
	public String getAddress002(OrderInfo orderInfo) {
		return "cachedAddressWithMultipleCaches002";
	}
	
	@CacheEvict(value = "addresses", allEntries = true)
	// 依據什麼去判定「陳舊的」值？
	public String getAddress003(OrderInfo orderInfo) {
		return "cachedAddressForEviction003";
	}
}
