package ru.softshaper.conf.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CacheManagerConfig
 *
 * @author ashek
 *
 */
@Configuration
@EnableCaching
public class CacheManagerConfig {

	/**
	 * @return CacheManager<br/>
	 *         TODO: fix it to ehcash
	 */
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList",
				"fieldObj", "fieldObjListCond", "fieldObjCnt", "userRole","token");
	}
}
