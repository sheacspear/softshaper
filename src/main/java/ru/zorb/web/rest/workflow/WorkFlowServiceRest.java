package ru.zorb.web.rest.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.helpers.IOUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.zorb.security.dynamiccontent.DynamicContentSecurityManager;

/**
 * @author ashek
 *
 */
@Path("/pr/workflowservice")
public class WorkFlowServiceRest {
 // @Autowired
 // private RepositoryService repositoryService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private DynamicContentSecurityManager securityManager;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  @POST
  @Path("/start/{processCode}/{objectId}")
  public void startProcess(@PathParam("processCode") String processCode, @PathParam("objectId") String objectId) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("data", 0);
    //ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionCategory(processCode).latestVersion().singleResult();
    runtimeService.startProcessInstanceByKey(processCode, objectId, dataMap);
  }

  @POST
  @Path("/claimTask/{taskId}")
  public void claimTask(@PathParam("taskId") String taskId) {
    taskService.claim(taskId, securityManager.getCurrentUserLogin());
  }

  @POST
  @Path("/complateTask/{taskId}/{data}")
  public void complateTask(@PathParam("taskId") String taskId,@PathParam("data") String data) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("data", Integer.valueOf(data));
    dataMap.put("status", Integer.valueOf(data));
    taskService.complete(taskId,dataMap);
  }



	@GET
	@Path("/bpmn")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getBPNMBin(@QueryParam("code") String code) throws IOException {
		byte[] data = IOUtils.readBytesFromStream(this.getClass().getClassLoader().getResourceAsStream("processes/doc.bpmn"));
		ResponseBuilder response = Response.ok((Object) data);
		response.header("Content-Disposition", "attachment; filename=doc.bpmn");
		return response.build();
	}


	@GET
	@Path("/bpmn_xml")
	@Produces(MediaType.TEXT_XML)
	public String getBPNMXml(@QueryParam("code") String code) throws IOException {
		byte[] data = IOUtils.readBytesFromStream(this.getClass().getClassLoader().getResourceAsStream("processes/doc.bpmn"));
		return new String(data,"UTF-8");
	}
}
