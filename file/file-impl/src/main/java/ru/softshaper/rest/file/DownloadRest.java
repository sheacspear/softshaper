package ru.softshaper.rest.file;

import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.datasource.file.FileObjectDataSource;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Path("/pr/download")
public class DownloadRest {
  private static final Logger log = LoggerFactory.getLogger(DownloadRest.class);
	@Autowired
	private FileObjectDataSource fileObjectDataSource;

	/**
	 * inject this from spring context
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * @param attachments
	 * @param request
	 * @return
	 */
	@GET
	@Path("/{fileId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("fileId") String fileId) {
		FileObject fileObject = fileObjectDataSource.getFileInfo(fileId);
		if (fileObject != null) {
			InputStream io = null;
			try {
				io = fileObjectDataSource.getFileData(fileId);
				if (io != null) {
					byte[] data = IOUtils.readBytesFromStream(io);
					ResponseBuilder response = Response.ok((Object) data);
					String name = fileObject.getName();					
		      name = new String(name.getBytes(Charset.forName("utf-8")), Charset.forName("ISO-8859-1"));
		      response.header("Content-Disposition", "attachment; filename=" + name);
					return response.build();
				}
			} catch (IOException e) {
			  log.error(e.getMessage(),e);
      } finally {
				if (io != null) {
					try {
						io.close();
					} catch (IOException e) {
						log.error(e.getMessage(),e);
					}
				}
			}
		}
		return Response.serverError().build();
	}
}
