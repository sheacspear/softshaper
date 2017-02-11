package ru.softshaper.web.admin.view.mapper.attr;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.bean.ViewSetting;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.utils.FieldCollection;

public class FileDataViewAttrMapper extends DataViewAttrMapperBase {


  public FileDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, DefaultViewMapper viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    return getLinkedValue(obj, metaField, FieldCollection.TITLE);
  }

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    return viewMapperBase.getValue(obj, metaField);
  }

  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    return null;
  }

  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    // TODO Auto-generated method stub
    return null;
  }

}
