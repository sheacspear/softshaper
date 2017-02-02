package ru.softshaper.web.admin.view.mapper;

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper FileObject to FormBean <br/>
 *
 */
public class FileViewMapper extends ViewMapperBase<FileObject> {

  private static final Map<String, Extractor<FileObject, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(FileObjectStaticContent.Field.mimeType, FileObject::getMimeType);
    valueExtractorByField.put(FileObjectStaticContent.Field.name, FileObject::getName);
    valueExtractorByField.put(FileObjectStaticContent.Field.modificationDate, FileObject::getModificationDate);
    valueExtractorByField.put(FileObjectStaticContent.Field.size, FileObject::getSize);
  }


  public FileViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(FileObject obj, MetaClass metaClass) {
    return obj.getId();
  }

  @Override
  protected Object getValue(FileObject obj, MetaField field) {
    Extractor<FileObject, ?> metaClassExtractor = valueExtractorByField.get(field.getCode());
    return metaClassExtractor == null ? null : metaClassExtractor.value(obj);
  }
}
