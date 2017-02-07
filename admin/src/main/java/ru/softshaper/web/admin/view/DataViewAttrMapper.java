package ru.softshaper.web.admin.view;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.bean.ViewSetting;

/**
 *
 */
public interface DataViewAttrMapper {

  Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView);

  Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView);

  String getTitle(Object obj, MetaField metaField, ViewSetting fieldView);
  
  ListObjectsView getVariants(MetaField metaField);
  
}
