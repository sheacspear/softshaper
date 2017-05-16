package ru.softshaper.web.admin.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.objlist.IListObjectsView;
import ru.softshaper.web.admin.view.controller.DataSourceFromView;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class LinkAttrController extends AttrControllerBase {


  @Autowired
  @Qualifier("linkedField")
  private FieldConverter fieldConverter;

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.LINK, this);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewAttrController#getValueByObject(java.lang
   * .Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
   * ru.softshaper.view.IViewAttrController#getValueByTable(java.lang.
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
   * ru.softshaper.view.IViewAttrController#getTitle(java.lang.Object,
   * ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
   * ru.softshaper.view.IViewAttrController#getVariants(ru.softshaper.
   * services.meta.MetaField)
   */
  @Override
  public <T> IListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor) {
    ViewSetting fieldView = viewSetting.getView(metaField);
    if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
      IListObjectsView variants;
      DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
      variants = dataSourceFromView
          .getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).setFieldCollection(FieldCollection.TITLE).build());
      return variants;
    }
    return null;
  }

  @Override
  protected FieldConverter getFieldConverter() {
    return fieldConverter;
  }
}
