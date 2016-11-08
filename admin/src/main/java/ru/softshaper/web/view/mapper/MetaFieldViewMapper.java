package ru.softshaper.web.view.mapper;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.staticcontent.meta.MetaFieldStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper MetaField to FormBean <br/>
 * Created by Sunchise on 17.08.2016.
 */
// todo
public class MetaFieldViewMapper extends ViewMapperBase<MetaField> {


  private static final Map<String, Extractor<MetaField, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(MetaFieldStaticContent.Field.code, MetaField::getCode);
    valueExtractorByField.put(MetaFieldStaticContent.Field.name, MetaField::getName);
    valueExtractorByField.put(MetaFieldStaticContent.Field.column, MetaField::getColumn);
    valueExtractorByField.put(MetaFieldStaticContent.Field.owner, field -> field.getOwner().getId());
    valueExtractorByField.put(MetaFieldStaticContent.Field.type, field -> field.getType().getId());
    valueExtractorByField.put(MetaFieldStaticContent.Field.linkToMetaClass, field -> field.getLinkToMetaClass() == null ? null : field.getLinkToMetaClass().getId());
    valueExtractorByField.put(MetaFieldStaticContent.Field.backReferenceField, field -> field.getBackReferenceField() == null ? null : field.getBackReferenceField().getId());
  }

  public MetaFieldViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(MetaField obj, MetaClass metaClass) {
    return obj.getId();
  }

  @Override
  protected Object getValue(MetaField obj, MetaField field) {
    Extractor<MetaField, ?> metaFieldValueExtractor = valueExtractorByField.get(field.getCode());
    return metaFieldValueExtractor == null ? null : metaFieldValueExtractor.value(obj);
  }

}
