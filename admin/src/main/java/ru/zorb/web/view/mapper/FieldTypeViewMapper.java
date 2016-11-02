package ru.zorb.web.view.mapper;

import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.meta.staticcontent.meta.FieldTypeStaticContent;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.ViewSettingFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper FieldType to FormBean <br/>
 * Created by Sunchise on 21.08.2016.
 */
public class FieldTypeViewMapper extends ViewMapperBase<FieldType> {



  private static final Map<String, Extractor<FieldType, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(FieldTypeStaticContent.Field.code, FieldType::getCode);
    valueExtractorByField.put(FieldTypeStaticContent.Field.name, FieldType::getName);
  }

  public FieldTypeViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(FieldType obj, MetaClass metaClass) {
    return obj.getId().toString();
  }

  @Override
  protected Object getValue(FieldType obj, MetaField field) {
    Extractor<FieldType, ?> metaClassExtractor = valueExtractorByField.get(field.getCode());
    return metaClassExtractor == null ? null : metaClassExtractor.value(obj);
  }
}
