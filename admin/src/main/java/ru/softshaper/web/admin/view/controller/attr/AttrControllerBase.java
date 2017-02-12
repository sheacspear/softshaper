package ru.softshaper.web.admin.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.view.IViewAttrController;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

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


}
