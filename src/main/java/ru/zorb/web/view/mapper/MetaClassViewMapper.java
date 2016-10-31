package ru.zorb.web.view.mapper;

import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.meta.staticcontent.meta.MetaClassStaticContent;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.ViewSettingFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper MetaClass to FormBean <br/>
 * Created by Sunchise on 17.08.2016.
 */
public class MetaClassViewMapper extends ViewMapperBase<MetaClass> {


  private static final Map<String, Extractor<MetaClass, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(MetaClassStaticContent.Field.code, MetaClass::getCode);
    valueExtractorByField.put(MetaClassStaticContent.Field.name, MetaClass::getName);
    valueExtractorByField.put(MetaClassStaticContent.Field.table, MetaClass::getTable);
    valueExtractorByField.put(MetaClassStaticContent.Field.fields, MetaClass::getFields);
    valueExtractorByField.put(MetaClassStaticContent.Field.fixed, MetaClass::isFixed);
    valueExtractorByField.put(MetaClassStaticContent.Field.checkObjectSecurity, MetaClass::isCheckObjectSecurity);
    valueExtractorByField.put(MetaClassStaticContent.Field.checkSecurity, MetaClass::isCheckSecurity);
  }

  public MetaClassViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(MetaClass obj, MetaClass metaClass) {
    return obj.getId();
  }

  @Override
  protected Object getValue(MetaClass obj, MetaField field) {
    Extractor<MetaClass, ?> metaClassExtractor = valueExtractorByField.get(field.getCode());
    return metaClassExtractor == null ? null : metaClassExtractor.value(obj);
  }
}
