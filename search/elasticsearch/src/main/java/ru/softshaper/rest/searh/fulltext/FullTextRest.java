package ru.softshaper.rest.searh.fulltext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.softshaper.search.elasticsearch.Indexator;
import ru.softshaper.search.elasticsearch.Searcher;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/pr/fulltext")
public class FullTextRest {

  private static final Logger log = LoggerFactory.getLogger(FullTextRest.class);

  @Autowired
  Searcher searcher;

  @Autowired
  private Indexator indexator;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }
  
  
  @GET
  @Path("/search/{text}")
  @Produces(MediaType.APPLICATION_JSON)
  public Object search(@PathParam("text") String text) {
    return searcher.search(text);
  }


  @GET
  @Path("/init")
  @Produces(MediaType.APPLICATION_JSON)
  public String fullInit() {
    indexator.fullIndex();
    return "success";
  }
}
