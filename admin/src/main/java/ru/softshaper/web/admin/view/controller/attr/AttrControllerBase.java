package ru.softshaper.web.admin.view.controller.attr;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.IViewAttrController;
import ru.softshaper.web.admin.view.controller.ViewObjectController;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

public abstract class AttrControllerBase implements IViewAttrController {

  /**
   * MetaStorage
   */
  protected final MetaStorage metaStorage;

  /**
   * Хранилище Источник данных для формы
   */
  protected final DataSourceFromViewStore dataSourceFromViewStore;
  
  /**
   * 
   */
  protected final ViewSettingStore viewSetting;
  
  /**
   * 
   */
  protected final ViewObjectController<Object> viewMapperBase;
  

  /**
   * @param metaStorage
   * @param dataSourceFromViewStore
   * @param viewSetting
   * @param viewMapperBase
   */
  public AttrControllerBase(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,ViewSettingStore viewSetting,ViewObjectController<Object> viewMapperBase) {
    super();
    this.metaStorage = metaStorage;
    this.dataSourceFromViewStore = dataSourceFromViewStore;
    this.viewMapperBase = viewMapperBase;
    this.viewSetting = viewSetting;
  }

  /**
   * @param obj
   * @param metaField
   * @param fieldCollection
   * @return
   */
  protected IObjectView getLinkedValue(Object obj, MetaField metaField, FieldCollection fieldCollection) {
    Object linkedObjId = viewMapperBase.getValue(obj, metaField);
    if (linkedObjId == null) {
      return null;
    }
    MetaClass linkedMetaClass = metaField.getLinkToMetaClass();
    if (metaField.getType().equals(FieldType.FILE)) {
      linkedMetaClass = metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS);
    }
    ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedMetaClass).ids().add(linkedObjId.toString())
        .setFieldCollection(fieldCollection).build();
    if (FieldCollection.TITLE.equals(fieldCollection)) {
      return dataSourceFromViewStore.get(linkedMetaClass.getCode()).getTitleObject(params);
    } else {
      return dataSourceFromViewStore.get(linkedMetaClass.getCode()).getFullObject(params);
    }
  }
  
}
