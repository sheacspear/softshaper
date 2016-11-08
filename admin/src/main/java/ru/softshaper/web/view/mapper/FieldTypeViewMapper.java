package ru.softshaper.web.view.mapper;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.staticcontent.meta.FieldTypeStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;

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
