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
import ru.softshaper.services.meta.conditions.impl.ConditionFieldImpl;
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
public class BackLinkAttrController extends AttrControllerBase {

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.BACK_REFERENCE, this);
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
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    if (metaField.getBackReferenceField().getType().equals(FieldType.MULTILINK)) {
      DataSourceFromView dataSourceFrom = getDataSourceFromView(metaField.getOwner().getCode());
      DataSourceFromView dataSourceTo = getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
      Collection<String> objectsIds = dataSourceTo.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getBackReferenceField().getCode(),
          objectExtractor.getId(obj, metaClass), true);
      if (objectsIds != null && !objectsIds.isEmpty()) {
        ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(objectsIds);
        if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
          ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TITLE).build();
          ListObjectsView listObjectsView = objectsIds != null ? dataSourceFrom.getListObjects(params) : null;
          value = listObjectsView;
        } else {
          ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TABLE).build();
          TableObjectsView tableObjectsView = objectsIds != null ? dataSourceFrom.getTableObjects(params) : null;
          value = tableObjectsView;
        }
      } else {
        if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
          value = new ListObjectsView(metaField.getLinkToMetaClass().getCode(), 0, Collections.emptyList());
        }
      }
    } else {
      DataSourceFromView dataSourceFromView = getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
      ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
          .setCondition(new ConditionFieldImpl(metaField.getBackReferenceField()).equal(objectExtractor.getId(obj, metaClass)));
      if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
        ListObjectsView listObjects = dataSourceFromView.getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
        value = listObjects;
      } else {
        value = dataSourceFromView.getTableObjects(paramsBuilder.setFieldCollection(FieldCollection.TABLE).build());
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
  public <T> Object getValueByTable(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    if (metaField.getBackReferenceField().getType() == FieldType.MULTILINK) {
      DataSourceFromView dataSourceFromView = getDataSourceFromView(metaClass.getCode());
      Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaField.getLinkToMetaClass().getCode(),
          metaField.getBackReferenceField().getCode(), objectExtractor.getId(obj, metaClass), true);
      if (linkIds != null) {
        DataSourceFromView linkedStore = getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
        value = linkedStore.getTitleObject(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
      }
    } else {
      DataSourceFromView dataSourceFromView = getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
      ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
          // todo: вот тут идентификатор надо приводить к реальному
          // типу
          .setCondition(new ConditionFieldImpl(metaField.getBackReferenceField()).equal(objectExtractor.getId(obj, metaClass)));
      value = dataSourceFromView.getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
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