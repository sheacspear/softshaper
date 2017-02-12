package ru.softshaper.web.admin.view;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;

/**
 *
 */
public interface IViewAttrController {

  /**
   * @param obj
   * @param metaField
   * @param fieldView
   * @return
   */
  Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView);

  /**
   * @param obj
   * @param metaField
   * @param fieldView
   * @return
   */
  Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView);

  /**
   * @param obj
   * @param metaField
   * @param fieldView
   * @return
   */
  String getTitle(Object obj, MetaField metaField, ViewSetting fieldView);
  
  /**
   * @param metaField
   * @return
   */
  ListObjectsView getVariants(MetaField metaField);
  
}
