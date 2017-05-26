package ru.softshaper.services.drools.saver;

import com.google.common.eventbus.EventBus;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.file.FileObjectBean;
import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.Doc;
import ru.softshaper.services.drools.bean.MetaData;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.security.ContentSecurityManager;
import ru.softshaper.storage.jooq.tables.daos.DcDcProjectDao;
import ru.softshaper.storage.jooq.tables.daos.DcDocDao;
import ru.softshaper.storage.jooq.tables.pojos.DcDcProject;
import ru.softshaper.storage.jooq.tables.pojos.DcDoc;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Saver {

  @Autowired
  private ContentSecurityManager securityManager;

  @Autowired
  private EventBus eventBus;

  @Autowired
  private MetaStorage metaStorage;

  @Autowired
  private DcDocDao dcDocDao;

  @Autowired
  private DcDcProjectDao dcDcProjectDao;
  @Autowired
  private FileObjectDataSource fileObjectDataSource;

  public Doc save(Data data, MetaData metaData, String codeProject) {
    FileObjectBean fileObject = new FileObjectBean(null, data.getFileName(), null, new java.util.Date(), null);
    String file = fileObjectDataSource.createFile(fileObject, new ByteArrayInputStream(data.getData()));
    List<DcDcProject> projects = dcDcProjectDao.fetchByCode(codeProject);
    DcDcProject project = projects != null && projects.size() > 0 ? projects.get(0) : null;
    DcDoc doc = new DcDoc();
    doc.setProject(project != null ? project.getId() : null);
    doc.setTags2(StringUtils.join(metaData.getTags().toArray(), ","));
    doc.setDescription(data.getFileName());
    doc.setEdocument(file);
    // doc.setId(filed.);
    doc.setSource("RuleDesigner");
    doc.setDate67(new Date(System.currentTimeMillis()));
    dcDocDao.insert(doc);
    String string = doc.getId() != null ? doc.getId().toString() : null;
    MetaClass docClass = metaStorage.getMetaClass("doc");
    if (docClass != null) {
      Map<MetaField, Object> values = new HashMap<>();
      values.put(docClass.getField("project"), doc.getProject());
      values.put(docClass.getField("tags"), doc.getTags());
      values.put(docClass.getField("description"), doc.getDescription());
      values.put(docClass.getField("source"), doc.getSource());
      values.put(docClass.getField("date67"), doc.getDate67());
      ObjectCreated event = new ObjectCreated(docClass, string, values, securityManager.getCurrentUserLogin());
      eventBus.post(event);
    }
    return new Doc(string, data.getFileName());
  }
}
