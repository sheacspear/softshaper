package test.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"ru.softshaper.web.rest","test.communda.action"})
public class TestConfig {

}