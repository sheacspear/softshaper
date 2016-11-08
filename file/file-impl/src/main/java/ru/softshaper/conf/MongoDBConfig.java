package ru.softshaper.conf;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import ru.softshaper.conf.bean.ApplicationBean;
import ru.softshaper.services.meta.FileObjectDataSource;

/**
 * Created by Sunchise on 17.09.2016.
 */
@Configuration
@Import({ ApplicationConfig.class })
@ComponentScan("ru.softshaper.services.meta")
public class MongoDBConfig extends AbstractMongoConfiguration {

  @Autowired
  private ApplicationBean applicationBean;

  @Autowired
  @Qualifier("mongoDbFileObject")
  private FileObjectDataSource fileObjectDataSource;

  @Bean(autowire = Autowire.NO)
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  FileObjectDataSource fileObjectDataSource() {
    return fileObjectDataSource;
  }
  @Bean
  public GridFsTemplate gridFsTemplate() throws Exception {
    return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
  }

  @Override
  protected String getDatabaseName() {
    return applicationBean.getMongodb().getBase();
  }

  @Override
  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient(applicationBean.getMongodb().getUrl());
  }





}
