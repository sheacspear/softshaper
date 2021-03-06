package ru.softshaper.datasource.file;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.bean.file.FileObjectBean;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.datasource.meta.AbstractObjectExtractor;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import javax.annotation.PostConstruct;
import javax.ws.rs.NotSupportedException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Хранлилище для файлов в MongoDB GridFS
 */
@Component
// @Qualifier("mongoDbFileObject")
@Qualifier("fileObjectDataSource")
public class MangoDBFileObjectDataSourceImpl implements FileObjectDataSource {

  private static final Logger log = LoggerFactory.getLogger(MangoDBFileObjectDataSourceImpl.class);

  private final GridFsTemplate gridFsOperations;

  private final MetaStorage metaStorage;

  private boolean mangoOffline = false;

  @Override
  public FileObject getObj(GetObjectsParams params) {

    if (mangoOffline) {
      return null;
    }

    if (params.getIds() != null && !params.getIds().isEmpty()) {
      GridFSDBFile file = null;
      try {
        file = gridFsOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(params.getIds().iterator().next())));
      } catch (Exception e) {
        mangoOffline = true;
        log.error(e.getMessage(), e);
      }
      if (file == null) {
        return null;
      }
      return map(file);
    }
    throw new UnsupportedOperationException();
  }

  @Autowired
  public MangoDBFileObjectDataSourceImpl(GridFsTemplate gridFsOperations, MetaStorage metaStorage) {
    this.gridFsOperations = gridFsOperations;
    this.metaStorage = metaStorage;
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String s, boolean reverse) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {
    throw new NotSupportedException();
  }

  private FileObject map(GridFSDBFile mongoFile) {
    return new FileObjectBean(mongoFile.getId().toString(), mongoFile.getFilename(), mongoFile.getLength(), mongoFile.getUploadDate(),
        mongoFile.getContentType());
  }

  @Override
  public String createObject(String metaClassCode, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(String contentCode, String id, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(String contentCode, String id) {

  }

  @Override
  public Integer getCntObjList(String contentCode) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  @Override
  public Collection<FileObject> getObjects(GetObjectsParams params) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String createFile(FileObject fileObject, InputStream dataStream) {
    DBObject metaData = new BasicDBObject();
    GridFSFile file = gridFsOperations.store(dataStream, fileObject.getName(), metaData);
    return file.getId().toString();
  }

  @Override
  public FileObject getFileInfo(String id) {
    return getObj(GetObjectsParams.newBuilder(metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS)).ids().add(id).build());
  }

  @Override
  public InputStream getFileData(String id) {
    GridFSDBFile file = gridFsOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(id)));
    return file == null ? null : file.getInputStream();
  }

  @Override
  public ObjectExtractor<FileObject> getObjectExtractor() {
    return new FileObjectExtractor();
  }

  public static class FileObjectExtractor extends AbstractObjectExtractor<FileObject> {

    private FileObjectExtractor() {
      registerFieldExtractor(FileObjectStaticContent.Field.mimeType, FileObject::getMimeType);
      registerFieldExtractor(FileObjectStaticContent.Field.name, FileObject::getName);
      registerFieldExtractor(FileObjectStaticContent.Field.modificationDate, FileObject::getModificationDate);
      registerFieldExtractor(FileObjectStaticContent.Field.size, FileObject::getSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(FileObject obj, MetaClass metaClass) {
      return obj.getId();
    }
  }
}
