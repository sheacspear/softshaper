package ru.softshaper.conf.admin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.softshaper.conf.db.JooqConfig;
import ru.softshaper.conf.meta.MetaConfig;

/**
 * CommandConfig for register web component
 *
 * @author ashek
 */
@Configuration
@Import({ JooqConfig.class, MetaConfig.class })
@ComponentScan(basePackages = { "ru.softshaper.web", "ru.softshaper.view" })
public class WebConfig {

}
