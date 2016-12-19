package ru.softshaper.conf.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
   * @return CacheManager
   */
  @Bean
  public CacheManager getEhCacheManager() {
    return new EhCacheCacheManager(getEhCacheFactory().getObject());
  }

  /**
   * @return EhCacheManagerFactoryBean
   */
  @Bean
  public EhCacheManagerFactoryBean getEhCacheFactory() {
    EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
    factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
    factoryBean.setShared(true);
    return factoryBean;
  }
}
