package ru.zorb.web.rest.report;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.zorb.services.report.IReport;
import ru.zorb.services.report.IReportService;

/**
 * @author ashek
 *
 */
@Path("/reportservice")
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class ReportServiceRest {

	/**
	 *
	 */
	@Autowired
	private IReportService reportService;

	/**
	 *
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * @param code
	 * @return
	 */
	@GET
	@Path("/report")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getReport(@QueryParam("code") String code) {
		IReport iReport = reportService.getReport(code);
		byte[] data = iReport.build(new HashMap<>());
		ResponseBuilder response = Response.ok((Object) data);
		response.header("Content-Disposition", "attachment; filename=test.pdf");
		return response.build();
	}
}
