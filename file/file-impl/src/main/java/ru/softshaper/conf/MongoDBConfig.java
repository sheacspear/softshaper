package ru.softshaper.conf;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Created by Sunchise on 17.09.2016.
 */
@Configuration
@ComponentScan("ru.softshaper.services.meta")
@PropertySource("classpath:application.properties")
public class MongoDBConfig extends AbstractMongoConfiguration {


    /**
     * Environment
     */
    @Autowired
    private Environment env;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("mongodbBase");
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(env.getProperty("mongodbUrl"));
    }


}
