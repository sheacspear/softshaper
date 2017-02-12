package ru.softshaper.web.admin.view.controller.attr;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.obj.IObjectView;
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
public class LinkAttrController extends AttrControllerBase {

  /**
   * @param metaStorage
   * @param dataSourceFromViewStore
   * @param viewSetting
   * @param viewMapperBase
   */
  public LinkAttrController(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingStore viewSetting, ViewObjectController<Object> viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getValueByObject(java.lang.Object, ru.softshaper.services.meta.MetaField, ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value;
    IObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE);
    if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
      value = valueLink == null ? null : valueLink.getId();
    } else {
      value = valueLink;
    }
    return value;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getValueByTable(java.lang.Object, ru.softshaper.services.meta.MetaField, ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    return viewMapperBase.getValue(obj, metaField);
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getTitle(java.lang.Object, ru.softshaper.services.meta.MetaField, ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    IObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE);
    if (valueLink != null) {
      return valueLink.getTitle();
    }
    return null;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getVariants(ru.softshaper.services.meta.MetaField)
   */
  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    ViewSetting fieldView = viewSetting.getView(metaField);
    if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
      ListObjectsView variants;
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      variants = dataSourceFromView.getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
          .setFieldCollection(FieldCollection.TITLE).build());
      return variants;
    }
    return null;
  }
}
