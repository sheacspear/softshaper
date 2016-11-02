package ru.zorb.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * CommandConfig for register bpnm service
 *
 * @author ashek
 *
 */
@Configuration
@ComponentScan("ru.zorb.services.workflow")
public class WorkFlowConfig {

}
