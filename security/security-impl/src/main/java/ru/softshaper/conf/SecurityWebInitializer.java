package ru.softshaper.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This class loaded and inicilized SecurityWeb<br/>
 * Created by arostov on 28.04.2016.
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
  public static final Logger log = LoggerFactory.getLogger(SecurityWebInitializer.class);

  /**
  *
  */
  public SecurityWebInitializer() {
  }
}
