package ru.softshaper.conf.report;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.softshaper.conf.meta.MetaConfig;

/**
 * CommandConfig for register Report service
 *
 * @author ashek
 *
 */
@Configuration
@Import(MetaConfig.class)
@ComponentScan("ru.softshaper.services.report")
public class ReportConfig {

}
