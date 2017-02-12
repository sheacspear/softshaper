package ru.softshaper.web.admin.view.controller.attr;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.controller.ViewObjectController;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

/**
 *
 */
public class FileAttrController extends AttrControllerBase {


  /**
   * @param metaStorage
   * @param dataSourceFromViewStore
   * @param viewSetting
   * @param viewMapperBase
   */
  public FileAttrController(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingStore viewSetting, ViewObjectController<Object> viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getValueByObject(java.lang.Object, ru.softshaper.services.meta.MetaField, ru.softshaper.web.admin.bean.obj.impl.ViewSetting)
   */
  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    return getLinkedValue(obj, metaField, FieldCollection.TITLE);
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
    return null;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewAttrController#getVariants(ru.softshaper.services.meta.MetaField)
   */
  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    return null;
  }
}
