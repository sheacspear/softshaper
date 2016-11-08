package ru.softshaper.services.meta.impl.datasource;

import org.apache.commons.io.IOUtils;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.*;
import ru.softshaper.services.meta.bean.FileObject;
import ru.softshaper.services.meta.bean.FileObjectBean;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.staticcontent.meta.FileObjectStaticContent;

import javax.ws.rs.NotSupportedException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 08.09.2016.
 */
@Component
@Qualifier("dbFileObject")
public class FileObjectDataSourceImpl implements FileObjectDataSource {

  public static final ru.softshaper.storage.jooq.tables.FileObject FILE_OBJECT_TABLE = ru.softshaper.storage.jooq.tables.FileObject.FILE_OBJECT;
  private final DSLContext dsl;

  private final MetaStorage metaStorage;

  @Autowired
  public FileObjectDataSourceImpl(DSLContext dsl, MetaStorage metaStorage) {
    this.dsl = dsl;
    this.metaStorage = metaStorage;
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String s, boolean reverse) {
    throw new UnsupportedOperationException();
  }

  private FileObjectBean mapRecordToFieldObject(Record record) {
    java.sql.Date modificationDate = record.get(FILE_OBJECT_TABLE.MODIFY_DATE, java.sql.Date.class);
    return new FileObjectBean(record.get(FILE_OBJECT_TABLE.ID, String.class), record.get(FILE_OBJECT_TABLE.NAME, String.class), record.get(FILE_OBJECT_TABLE.SIZE, Long.class),
        modificationDate == null ? null : new Date(modificationDate.getTime()), record.get(FILE_OBJECT_TABLE.MIME_TYPE, String.class));
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {
    throw new NotSupportedException();
  }

  @Override
  public FileObject getObj(GetObjectsParams params) {
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      Collection<Field<?>> metaInfoFields = metaInfoJooqFields();
      Record record = dsl.select(metaInfoFields).from(FILE_OBJECT_TABLE).where(FILE_OBJECT_TABLE.ID.equal(params.getIds().iterator().next())).fetchOne();
      return mapRecordToFieldObject(record);
    }
    throw new UnsupportedOperationException();
  }

  private Set<org.jooq.Field<?>> metaInfoJooqFields() {
    MetaClass metaClass = metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS);
    final Set<Field<?>> collect = metaClass.getFields().stream().filter(field -> !field.getCode().equals(FileObjectStaticContent.Field.data))
        .map(field -> DSL.field(field.getColumn())).collect(Collectors.toSet());
    collect.add(FILE_OBJECT_TABLE.ID);
    return collect;
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
    ru.softshaper.storage.jooq.tables.FileObject table = ru.softshaper.storage.jooq.tables.FileObject.FILE_OBJECT;
    dsl.delete(table).where(table.ID.equal(id));
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    return dsl.select(DSL.count()).from(FILE_OBJECT_TABLE).fetchOne().value1();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  @Override
  public Collection<FileObject> getObjects(GetObjectsParams params) {
    throw new RuntimeException();
  }

  @Override
  public String createFile(FileObject fileObject, InputStream dataStream) {
    byte[] data;
    try {
      data = IOUtils.toByteArray(dataStream);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return dsl.insertInto(FILE_OBJECT_TABLE).set(FILE_OBJECT_TABLE.ID, UUID.randomUUID().toString()).set(FILE_OBJECT_TABLE.FILE_DATA, data)
        .set(FILE_OBJECT_TABLE.NAME, fileObject.getName()).set(FILE_OBJECT_TABLE.MIME_TYPE, fileObject.getMimeType())
        .set(FILE_OBJECT_TABLE.MODIFY_DATE, fileObject.getModificationDate() == null ? null : new java.sql.Date(fileObject.getModificationDate().getTime()))
        .set(FILE_OBJECT_TABLE.SIZE, fileObject.getSize()).returning(FILE_OBJECT_TABLE.ID).fetch().getValue(0, FILE_OBJECT_TABLE.ID);
  }

  @Override
  public FileObject getFileInfo(String id) {
    return getObj(GetObjectsParams.newBuilder(metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS)).ids().add(id).build());
  }

  @Override
  public InputStream getFileData(String id) {
    MetaField dataField = metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS).getField(FileObjectStaticContent.Field.data);
    Record record = dsl.select(DSL.field(dataField.getColumn())).from(FILE_OBJECT_TABLE).where(FILE_OBJECT_TABLE.ID.equal(id)).fetchOne();
    if (record == null) {
      return null;
    }
    byte[] data = record.get(dataField.getColumn(), byte[].class);
    return data == null ? null : new ByteArrayInputStream(data);
  }
}
