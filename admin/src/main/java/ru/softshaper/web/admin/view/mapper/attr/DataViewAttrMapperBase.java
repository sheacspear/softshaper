package ru.softshaper.web.admin.view.mapper.attr;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.DataViewAttrMapper;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.utils.FieldCollection;
import ru.softshaper.web.admin.view.utils.ViewObjectsParams;

public abstract class DataViewAttrMapperBase implements DataViewAttrMapper {

  /**
   * MetaStorage
   */
  protected final MetaStorage metaStorage;

  /**
   * Хранилище Источник данных для формы
   */
  protected final DataSourceFromViewStore dataSourceFromViewStore;
  
  protected final ViewSettingFactory viewSetting;
  
  protected final DefaultViewMapper viewMapperBase;
  

  public DataViewAttrMapperBase(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,ViewSettingFactory viewSetting,DefaultViewMapper viewMapperBase) {
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
