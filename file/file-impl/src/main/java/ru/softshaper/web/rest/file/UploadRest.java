package ru.softshaper.web.rest.file;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.softshaper.services.meta.FileObjectDataSource;
import ru.softshaper.services.meta.bean.FileObjectBean;

import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ashek
 *
 */
@Path("/pr/upload")
public class UploadRest {
  private static final Logger log = LoggerFactory.getLogger(UploadRest.class);
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
	@POST
	@Path("/uploadFile")
	@Consumes("multipart/form-data")
	public String uploadFile(List<Attachment> attachments) {
		for (Attachment attachment : attachments) {
			DataHandler handler = attachment.getDataHandler();
			InputStream stream = null;
			try {
				stream = handler.getInputStream();
				MultivaluedMap<String, String> map = attachment.getHeaders();
				FileObjectBean fileObject = new FileObjectBean(null, getFileName(map), null, null, null);
				return fileObjectDataSource.createFile(fileObject, stream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						log.error(e.getMessage(),e);
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param header
	 * @return
	 */
	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "unknown";
	}
}
