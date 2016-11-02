package ru.zorb.conf;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import ru.zorb.conf.bean.ApplicationBean;

/**
 * Created by Sunchise on 17.09.2016.
 */
@Configuration
@Import({ ApplicationConfig.class })
public class MongoDBConfig extends AbstractMongoConfiguration {

  @Autowired
  private ApplicationBean applicationBean;

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
