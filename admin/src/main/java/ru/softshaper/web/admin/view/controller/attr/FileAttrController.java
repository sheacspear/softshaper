package ru.softshaper.web.admin.view.controller.attr;

import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class FileAttrController extends AttrControllerBase {

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.FILE, this);
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
    return getLinkedValue(obj, metaField, FieldCollection.TITLE, objectExtractor);
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
    return objectExtractor.getValue(obj, metaField);
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
    return null;
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
