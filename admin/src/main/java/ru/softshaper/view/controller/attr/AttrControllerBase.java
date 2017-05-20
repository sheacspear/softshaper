package ru.softshaper.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.*;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.view.bean.obj.IObjectView;
import ru.softshaper.view.controller.IViewAttrController;
import ru.softshaper.view.controller.IViewObjectController;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;

public abstract class AttrControllerBase implements IViewAttrController {



  /**
   * MetaStorage
   */
  @Autowired
  protected MetaStorage metaStorage;

  /**
   *
   */
  @Autowired
  protected ViewSettingStore viewSetting;

  @Autowired
  protected IViewObjectController viewObjectController;

  /**
   * @param obj
   * @param metaField
   * @param fieldCollection
   * @return
   */
  protected <T> IObjectView getLinkedValue(T obj, MetaField metaField, FieldCollection fieldCollection, ObjectExtractor<T> objectExtractor) {
    Object linkedObjId = objectExtractor.getValue(obj, metaField);
    if (linkedObjId == null) {
      return null;
    }
    MetaClass linkedMetaClass = metaField.getLinkToMetaClass();
    if (metaField.getType().equals(FieldType.FILE)) {
      linkedMetaClass = metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS);
    }
    ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedMetaClass).ids().add(linkedObjId.toString()).setFieldCollection(fieldCollection).build();
    if (FieldCollection.TITLE.equals(fieldCollection)) {
      return viewObjectController.getDataSourceFromView(linkedMetaClass.getCode()).getTitleObject(params);
    } else {
      return viewObjectController.getDataSourceFromView(linkedMetaClass.getCode()).getFullObject(params);
    }
  }

  protected abstract FieldConverter getFieldConverter();

  @Override
  public <T> Object convertViewValueToObjectValue(T viewValue, MetaField metaField, ViewSetting fieldView) {
    return getFieldConverter().convert(metaField, viewValue);
  }
}
