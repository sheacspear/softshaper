package ru.softshaper.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.bean.obj.IObjectView;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.controller.DataSourceFromView;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.ViewSetting;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
public class UniversalLinkAttrController extends AttrControllerBase {


  @Autowired
  @Qualifier("universalLink")
  private FieldConverter fieldConverter;

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.UNIVERSAL_LINK, this);
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
   * ru.softshaper.view.IViewAttrController#getValueByTable(java.lang.
   * Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
          IObjectView titleObject = dataSourceFromView.getTitleObject(params);
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
   * ru.softshaper.view.IViewAttrController#getTitle(java.lang.Object,
   * ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
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
   * ru.softshaper.view.IViewAttrController#getVariants(ru.softshaper.
   * services.meta.MetaField)
   */
  @Override
  public <T> IListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected FieldConverter getFieldConverter() {
    return fieldConverter;
  }
}
