package ru.softshaper.rest.util;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.services.util.IUtilsEngine;

@Path("/pr/utils")
public class UtilsServiceRest {

  @Autowired
  private IUtilsEngine utilsEngine;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  getUtilsByObject(){
    
  }

  getStep(){
    
  }

}
