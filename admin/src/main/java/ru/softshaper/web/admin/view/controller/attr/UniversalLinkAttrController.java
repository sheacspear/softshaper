package ru.softshaper.web.admin.view.controller.attr;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;

/**
 *
 */
@Component
public class UniversalLinkAttrController extends AttrControllerBase {

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.UNIVERSAL_LINK, this);
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
    Object value;
    value = objectExtractor.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(linkedClass.getCode());
          if (FieldTypeView.LINK_INNER_OBJECT.equals(fieldView.getTypeView())) {
            ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier).setFieldCollection(FieldCollection.ALL).build();
            value = dataSourceFromView.getFullObject(params);
          } else if (FieldTypeView.LINK_BROWSE.equals(fieldView.getTypeView())) {
            ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier).setFieldCollection(FieldCollection.TITLE).build();
            value = dataSourceFromView.getTitleObject(params);
          }
        }
      }

    }
    return value;
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
    Object value = objectExtractor.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(linkedClass.getCode());

          ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier).setFieldCollection(FieldCollection.TITLE).build();
          TitleObjectView titleObject = dataSourceFromView.getTitleObject(params);
          value = titleObject == null ? value : titleObject.getTitle();
        }
      }
    }
    return value;
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
    Object value = objectExtractor.getValue(obj, metaField);
    return value != null ? value.toString() : null;
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
    // TODO Auto-generated method stub
    return null;
  }
}
