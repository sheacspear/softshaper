package ru.zorb.conf;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import ru.zorb.services.meta.*;
import ru.zorb.services.meta.impl.MetaInitializerImpl;
import ru.zorb.services.meta.impl.loader.DynamicContentLoader;
import ru.zorb.services.meta.impl.loader.StaticContentLoader;

/**
 * Created by Sunchise on 10.08.2016.
 */
@Configuration
@Import({ JooqConfig.class,WebSecurityConfig.class, MongoDBConfig.class})
@ComponentScan("ru.zorb.services.meta")
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
  private ContentDataSource<Record> dynamicDataSource;

  /**
   * DataSource by MetaClass
   */
  @Autowired
  private ContentDataSource<MetaClass> metaClassDataSource;

  /**
   * DataSource by MetaField
   */
  @Autowired
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

  @Autowired
  @Qualifier("mongoDbFileObject")
  private FileObjectDataSource fileObjectDataSource;

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

  @Bean(autowire = Autowire.NO)
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  FileObjectDataSource fileObjectDataSource() {
    return fileObjectDataSource;
  }

}
