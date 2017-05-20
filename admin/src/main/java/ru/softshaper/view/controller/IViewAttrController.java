package ru.softshaper.view.controller;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.viewsettings.ViewSetting;

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
  <T> Object getValueByObject(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor);

  /**
   * @param obj
   * @param metaField
   * @param fieldView
   * @return
   */
  <T> Object getValueByTable(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor);

  /**
   * @param obj
   * @param metaField
   * @param fieldView
   * @return
   */
  <T> String getTitle(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor);

  /**
   * @param metaField
   * @return
   */
  <T> IListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor);

  <T> Object convertViewValueToObjectValue(T viewValue, MetaField metaField, ViewSetting fieldView);
}
