package ru.softshaper.conf.meta;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.softshaper.conf.db.JooqConfig;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.impl.MetaInitializerImpl;
import ru.softshaper.services.meta.impl.loader.DynamicContentLoader;
import ru.softshaper.services.meta.impl.loader.StaticContentLoader;

/**
 * Created by Sunchise on 10.08.2016.
 */
@Configuration
@Import({ JooqConfig.class})
@ComponentScan({"ru.softshaper.datasource.meta","ru.softshaper.staticcontent.meta","ru.softshaper.services.meta"})
public class MetaConfig {

  /**
   * JOOQ
   */
  @Autowired
  private DSLContext dslContext;

  /**
   * DataSource by Record
   */
  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> dynamicDataSource;

  /**
   * DataSource by MetaClass
   */
  @Autowired
  @Qualifier("metaClass")
  private ContentDataSource<MetaClass> metaClassDataSource;

  /**
   * DataSource by MetaField
   */
  @Autowired
  @Qualifier("metaField")
  private ContentDataSource<MetaField> metaFieldDataSource;

  /**
   *
   */
  @Autowired
  private DataSourceStorage dataSourceStorage;
  /**
   *
   */
  @Autowired
  private MetaStorage metaStorage;

  /**
  *
  */
  @Autowired
  @Qualifier("StaticContentLoader")
  private StaticContentLoader staticContentLoader;



  /**
   * @return MetaInitializer
   */
  @Bean
  public MetaInitializer metaInitializer() {
    MetaInitializerImpl metaInitializer = new MetaInitializerImpl(metaStorage, dataSourceStorage);
    metaClassDataSource.setMetaInitializer(metaInitializer);
    metaFieldDataSource.setMetaInitializer(metaInitializer);
    metaInitializer.registerLoader(new DynamicContentLoader(dslContext, dynamicDataSource));
    metaInitializer.registerLoader(staticContentLoader);
    metaInitializer.init();
    return metaInitializer;
  }



}
