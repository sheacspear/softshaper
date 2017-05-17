package ru.softshaper.rest.drools;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.drools.SuitResult;

/**
 * Rest service for Rule Designer
 *
 */
@Path("/pr/rules")
public class RuleDesignerRest {

  private static final Logger log = LoggerFactory.getLogger(RuleDesignerRest.class);

  @Autowired
  private IRuleDisigner ruleDisigner;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  /**
   * 
   * @param id
   * @return
   */
  @GET
  @Path("/validateRule/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String validateRule(@PathParam("id") String id) {
    return ruleDisigner.validateRule(id);
  }

  /**
   * 
   * @param id
   * @return
   */

  @GET
  @Path("/validateSuit/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String validateSuit(@PathParam("id") String id) {
    return ruleDisigner.validateSuit(id);
  }

  /**
   * 
   * @param id
   * @return
   */
  @GET
  @Path("/runSuit/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public SuitResult runSuit(@PathParam("id") String id) {
    return ruleDisigner.runSuit(id);
  }
}
