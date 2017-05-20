package ru.softshaper.conf.drools;

import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.kie.api.KieServices;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = { "ru.softshaper.services.drools", "ru.softshaper.utils.rules" })
public class RulesConfig {

  @Bean
  public KieServices kieServices() {
    return new KieServicesImpl();
  }
}
