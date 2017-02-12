package ru.softshaper.web.admin.view.controller.attr;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;

/**
 *
 */
@Component
public class DefaultAttrController extends AttrControllerBase {

  @PostConstruct
  void init() {
    viewObjectController.setDefaultAttrController(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.admin.view.IViewAttrController#getValueByObject(java.lang
   * .Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public <T> Object getValueByObject(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    return objectExtractor.getValue(obj, metaField);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.admin.view.IViewAttrController#getValueByTable(java.lang.
   * Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public <T> Object getValueByTable(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    return getValueByObject(obj, metaField, fieldView, objectExtractor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.admin.view.IViewAttrController#getTitle(java.lang.Object,
   * ru.softshaper.services.meta.MetaField,
   * ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public <T> String getTitle(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    Object valueByObject = getValueByObject(obj, metaField, fieldView, objectExtractor);
    return valueByObject != null ? valueByObject.toString() : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.admin.view.IViewAttrController#getVariants(ru.softshaper.
   * services.meta.MetaField)
   */
  @Override
  public <T> ListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor) {
    return null;
  }
}
