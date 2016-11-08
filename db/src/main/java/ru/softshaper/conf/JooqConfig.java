package ru.softshaper.conf;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.util.Database;
import org.jooq.util.Databases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.softshaper.conf.bean.ApplicationBean;

/**
 * JooqConfig for database
 *
 * @author ashek
 *
 */
@Configuration
@Import({ ApplicationConfig.class })
@EnableTransactionManagement
@ComponentScan("ru.softshaper.storage")
public class JooqConfig {

  /**
   * max alive database connection
   */
  private static final int MAX_IDLE = 20;

  /**
   * max database connection
   */
  private static final int MAX_ACTIOVE = 20;

  /**
   * Bean from file application.properties
   */
  @Autowired
  private ApplicationBean applicationBean;

  /**
   * Формирование настроек источника данных
   *
   * @return описание источника данных
   */
  @Bean
  public DataSource dataSource() {
    PoolProperties poolProperties = new PoolProperties();
    poolProperties.setMaxActive(MAX_ACTIOVE);
    poolProperties.setMaxIdle(MAX_IDLE);
    poolProperties.setRemoveAbandoned(true);
    poolProperties.setRemoveAbandonedTimeout(3600);   
    poolProperties.setValidationQuery("select 1");
    poolProperties.setTestOnBorrow(true);
    poolProperties.setTestWhileIdle(true);
    poolProperties.setTestOnReturn(true);
    org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
    ds.setDriverClassName(applicationBean.getBdDriverClassName());
    ds.setUrl(applicationBean.getBdUrl());
    ds.setUsername(applicationBean.getBdLogin());
    ds.setPassword(applicationBean.getBdPass());
    
    
    
    return ds;
  }

  /**
   * DataSourceTransactionManager
   *
   * @param dataSource
   * @return
   */
  @Bean
  @Qualifier("jooq")
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  /**
   * TransactionAwareDataSourceProxy
   *
   * @param dataSource
   * @return
   */
  @Bean
  public TransactionAwareDataSourceProxy transactionAwareDataSource(DataSource dataSource) {
    return new TransactionAwareDataSourceProxy(dataSource);
  }

  /**
   * DataSourceConnectionProvider
   *
   * @param transactionAwareDataSourceProxy
   * @return
   */
  @Bean
  public DataSourceConnectionProvider connectionProvider(
      TransactionAwareDataSourceProxy transactionAwareDataSourceProxy) {
    return new DataSourceConnectionProvider(transactionAwareDataSourceProxy);
  }

  /**
   * DefaultConfiguration
   *
   * @param dataSourceConnectionProvider
   * @return
   */
  @Bean
  public DefaultConfiguration configuration(DataSourceConnectionProvider dataSourceConnectionProvider) {
    DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
    jooqConfiguration.set(dataSourceConnectionProvider);
    final String bdDatabaseType = applicationBean.getBdDatabaseType();
    jooqConfiguration
        .set(bdDatabaseType.equals("POSTGRESQL") ? SQLDialect.POSTGRES_9_5 : SQLDialect.valueOf(bdDatabaseType));
    return jooqConfiguration;
  }

  /**
   * DSLContext
   *
   * @param defaultConfiguration
   * @return
   */
  @Bean
  public DSLContext dsl(DefaultConfiguration defaultConfiguration) {
    return new DefaultDSLContext(defaultConfiguration);
  }

  /**
   * Database for control database
   *
   * @param defaultConfiguration
   * @param dataSource
   * @return
   */
  @Bean
  public Database database(DefaultConfiguration defaultConfiguration, DataSource dataSource) {
    final String bdDatabaseType = applicationBean.getBdDatabaseType();
    final Database database = Databases
        .database(bdDatabaseType.equals("POSTGRESQL") ? SQLDialect.POSTGRES_9_5 : SQLDialect.valueOf(bdDatabaseType));
    try {
      database.setConnection(dataSource.getConnection());
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return database;
  }
}
