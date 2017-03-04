package ru.softshaper.rest.utils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.rest.utils.bean.UtilBean;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.StepUtil;

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

  public Collection<UtilBean> getUtilsByObject(String metaClazz, String objId) {
    Collection<IUtil> utils = utilsEngine.getAvailableUtilsForObject(metaClazz, objId);
    return utils.stream().map(util -> new UtilBean(util.getName(), util.getCode())).collect(Collectors.toList());
  }

  public StepUtil getNextStep(String utilCode, Map<String, Object> data) {
    StepUtil step = utilsEngine.getNextStep(utilCode, data);
    return step;
  }
}
