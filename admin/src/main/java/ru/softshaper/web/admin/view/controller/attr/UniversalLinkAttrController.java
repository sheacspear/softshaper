package ru.softshaper.web.admin.view.controller.attr;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.controller.ViewObjectController;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

/**
 *
 */
public class UniversalLinkAttrController extends AttrControllerBase {

  /**
   * @param metaStorage
   * @param dataSourceFromViewStore
   * @param viewSetting
   * @param viewMapperBase
   */
  public UniversalLinkAttrController(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore, ViewSettingStore viewSetting,
      ViewObjectController<Object> viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
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
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value;
    value = viewMapperBase.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());
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
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = viewMapperBase.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());

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
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = viewMapperBase.getValue(obj, metaField);
    return value != null ? viewMapperBase.toString() : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.admin.view.IViewAttrController#getVariants(ru.softshaper.
   * services.meta.MetaField)
   */
  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    // TODO Auto-generated method stub
    return null;
  }
}
