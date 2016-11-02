package ru.zorb.web.rest.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.zorb.services.meta.MetaInitializer;
import ru.zorb.web.view.impl.ViewSettingFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by arostov on 20.10.2016.
 */
@Path("/pb/utils")
public class ApplicationUtilsRest {

  @Autowired
  private MetaInitializer metaInitializer;

  @Autowired
  private ViewSettingFactory viewSettingFactory;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void postConstruct() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  @GET
  @Path("/init")
  @Produces("application/json")
  public void init() {
    metaInitializer.init();
    viewSettingFactory.init();
  }
}
