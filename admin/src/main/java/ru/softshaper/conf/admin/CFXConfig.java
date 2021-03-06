package ru.softshaper.conf.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import ru.softshaper.conf.meta.MetaConfig;

/**
 * CFXConfig for JAX-WS JAX-RS
 *
 * @author ashek
 *
 */
@Configuration
@Import({ MetaConfig.class })
public class CFXConfig {

  /**
   * @return Json Parser Provider
   */
  @Bean(name = "jsonProvider")
  public JacksonJsonProvider getJsonProvider() {
    return new JacksonJsonProvider();
  }

}
