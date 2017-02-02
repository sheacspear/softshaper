package ru.softshaper.web.admin.view.mapper;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;

/**
 * Created by Sunchise on 15.11.2016.
 */
public class DefaultViewMapper<T> extends ViewMapperBase<T> {

  private final ObjectExtractor<T> objectExtractor;

  public DefaultViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore, ObjectExtractor<T> objectExtractor) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
    this.objectExtractor = objectExtractor;
  }

  protected String getId(T obj, MetaClass metaClass) {
    return objectExtractor.getId(obj, metaClass);
  }

  protected Object getValue(T obj, MetaField field) {
    return objectExtractor.getValue(obj, field);
  }
}
