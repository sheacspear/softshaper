package ru.softshaper.web.admin.view.mapper.attr;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.bean.ViewSetting;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.ViewMapperBase;

public class DefaultDataViewAttrMapper extends DataViewAttrMapperBase {

  public DefaultDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, ViewMapperBase viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
  }

  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    return viewMapperBase.getValue(obj, metaField);
  }

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    return getValueByObject(obj, metaField, fieldView);
  }

  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object valueByObject = getValueByObject(obj, metaField, fieldView);
    return valueByObject != null ? valueByObject.toString() : null;
  }

  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    return null;
  }
}
