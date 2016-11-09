package ru.softshaper.web.view.mapper;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 12.10.2016.
 */
public class FieldTypeViewViewMapper extends ViewMapperBase<FieldTypeView> {

  private static final Map<String, Extractor<FieldTypeView, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(FieldTypeViewStaticContent.Field.code, FieldTypeView::getCode);
    valueExtractorByField.put(FieldTypeViewStaticContent.Field.name, FieldTypeView::getName);
  }

  public FieldTypeViewViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(FieldTypeView obj, MetaClass metaClass) {
    return obj.getCode();
  }

  @Override
  protected Object getValue(FieldTypeView obj, MetaField field) {
    Extractor<FieldTypeView, ?> metaClassExtractor = valueExtractorByField.get(field.getCode());
    return metaClassExtractor == null ? null : metaClassExtractor.value(obj);
  }
}
