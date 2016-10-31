package ru.zorb.web.view.mapper;

import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.meta.bean.FileObject;
import ru.zorb.services.meta.staticcontent.meta.FileObjectStaticContent;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.ViewSettingFactory;

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
