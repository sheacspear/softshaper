package ru.softshaper.web.admin.view.controller.attr;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.TableObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.params.FieldCollection;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;
import ru.softshaper.web.admin.view.params.ViewObjectsParams.ViewObjectParamsBuilder;

/**
 *
 */
@Component
public class MultyLinkAttrController extends AttrControllerBase {

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.MULTILINK, this);
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
    MetaClass metaClass = metaField.getOwner();
    Object value;
    DataSourceFromView dataSourceFrom = dataSourceFromViewStore.get(metaField.getOwner().getCode());
    DataSourceFromView dataSourceTo = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
    Collection<String> objectsIds = dataSourceFrom.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getCode(), objectExtractor.getId(obj, metaClass),
        false);
    ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(objectsIds);
    if (FieldTypeView.MULTILINK_CHECKBOX.equals(fieldView.getTypeView())) {
      ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TITLE).build();
      value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getListObjects(params) : null;
    } else {
      ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TABLE).build();
      value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getTableObjects(params)
          : emptyTableObjectsView(metaField.getLinkToMetaClass(), objectExtractor);
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
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClass.getCode());
    Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getCode(), objectExtractor.getId(obj, metaClass),
        false);
    if (linkIds != null && !linkIds.isEmpty()) {
      DataSourceFromView linkedStore = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      value = linkedStore.getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
    }
    return value;
  }

  /**
   * @param metaClass
   * @return
   */
  /**
   * @param metaClass
   * @return
   */
  private <T> TableObjectsView emptyTableObjectsView(MetaClass metaClass, ObjectExtractor<T> objectExtractor) {
    return new TableObjectsView(metaClass.getCode(), 0, viewObjectController.constructColumnsView(metaClass), Collections.emptyList());
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
    // TODO Auto-generated method stub
    return null;
  }
}
