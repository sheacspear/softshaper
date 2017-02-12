package ru.softshaper.web.admin.view.controller.attr;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;

/**
 *
 */
@Component
public class LinkAttrController extends AttrControllerBase {

  @PostConstruct
  void init(){
    viewObjectController.registerAttrController(FieldType.LINK, this);    
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
    IObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE, objectExtractor);
    if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
      value = valueLink == null ? null : valueLink.getId();
    } else {
      value = valueLink;
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
    return objectExtractor.getValue(obj, metaField);
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
    IObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE, objectExtractor);
    if (valueLink != null) {
      return valueLink.getTitle();
    }
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
    ViewSetting fieldView = viewSetting.getView(metaField);
    if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
      ListObjectsView variants;
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      variants = dataSourceFromView
          .getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).setFieldCollection(FieldCollection.TITLE).build());
      return variants;
    }
    return null;
  }
}
