package ru.softshaper.services.drools.saver;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.List;

import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.file.FileObjectBean;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.Doc;
import ru.softshaper.services.drools.bean.MetaData;
import ru.softshaper.storage.jooq.tables.daos.DcDcProjectDao;
import ru.softshaper.storage.jooq.tables.daos.DcDocDao;
import ru.softshaper.storage.jooq.tables.pojos.DcDcProject;
import ru.softshaper.storage.jooq.tables.pojos.DcDoc;

@Component
public class Saver {

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
    doc.setProject(project.getId());
    doc.setTags2(StringUtils.join(metaData.getTags().toArray(), ","));
    doc.setDescription(data.getFileName());
    doc.setEdocument(file);
    //doc.setId(filed.);
    doc.setSource("RuleDesigner");
    doc.setDate67(new Date(System.currentTimeMillis()));
    dcDocDao.insert(doc);  
    String string = doc.getId()!=null?doc.getId().toString()  : null;
    return new Doc(string, data.getFileName());
  }
}
