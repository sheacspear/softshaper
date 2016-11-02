package ru.zorb.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * CommandConfig for register Report service
 *
 * @author ashek
 *
 */
@Configuration
@Import(MetaConfig.class)
@ComponentScan("ru.zorb.services.report")
public class ReportConfig {

}
