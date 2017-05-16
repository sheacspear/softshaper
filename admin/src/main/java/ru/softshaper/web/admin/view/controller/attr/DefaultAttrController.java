package ru.softshaper.web.admin.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.IListObjectsView;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class DefaultAttrController extends AttrControllerBase {

  @Autowired
  @Qualifier("simpleField")
  private FieldConverter fieldConverter;

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
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
  public <T> IListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor) {
    return null;
  }

  @Override
  protected FieldConverter getFieldConverter() {
    return fieldConverter;
  }
}
