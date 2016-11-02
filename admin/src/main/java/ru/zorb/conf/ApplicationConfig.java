package ru.zorb.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.zorb.conf.bean.ApplicationBean;
import ru.zorb.conf.bean.MongoDBConfig;

/**
 * ApplicationConfig from file application.properties
 *
 * @author ashek
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
  /**
   * Environment
   */
  @Autowired
  private Environment env;

  /**
   * @return Bean from file application.properties
   */
  @Bean
  public ApplicationBean application() {
    ApplicationBean testBean = new ApplicationBean();
    // testBean.setName(env.getProperty("testbean.name"));
    testBean.setBdUrl(env.getProperty("bdUrl"));
    testBean.setBdLogin(env.getProperty("bdLogin"));
    testBean.setBdPass(env.getProperty("bdPass"));
    testBean.setBdDriverClassName(env.getProperty("bdDriverClassName"));
    testBean.setBdDatabaseType(env.getProperty("bdDatabaseType"));
    testBean.setMongodb(new MongoDBConfig(env.getProperty("mongodbUrl"),
                                          env.getProperty("mongodbBase"),
                                          env.getProperty("mongodbLogin"),
                                          env.getProperty("mongodbPass")));
    return testBean;
  }

}
