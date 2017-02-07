package ru.softshaper.web.admin.view.mapper.attr;

import java.util.Collection;
import java.util.Collections;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.conditions.impl.ConditionFieldImpl;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.TableObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.bean.ViewSetting;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.ViewMapperBase;
import ru.softshaper.web.admin.view.utils.FieldCollection;
import ru.softshaper.web.admin.view.utils.ViewObjectParamsBuilder;
import ru.softshaper.web.admin.view.utils.ViewObjectsParams;

public class BackLinkDataViewAttrMapper extends DataViewAttrMapperBase {


  public BackLinkDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, ViewMapperBase viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    if (metaField.getBackReferenceField().getType().equals(FieldType.MULTILINK)) {
      DataSourceFromView dataSourceFrom = dataSourceFromViewStore.get(metaField.getOwner().getCode());
      DataSourceFromView dataSourceTo = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      Collection<String> objectsIds = dataSourceTo.getObjectsIdsByMultifield(metaClass.getCode(),
          metaField.getBackReferenceField().getCode(), viewMapperBase.getId(obj, metaClass), true);
      if (objectsIds != null && !objectsIds.isEmpty()) {
        ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
            .addIds(objectsIds);
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
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).setCondition(
          new ConditionFieldImpl(metaField.getBackReferenceField()).equal(viewMapperBase.getId(obj, metaClass)));
      if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
        ListObjectsView listObjects = dataSourceFromView
            .getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
        value = listObjects;
      } else {
        value = dataSourceFromView.getTableObjects(paramsBuilder.setFieldCollection(FieldCollection.TABLE).build());
      }
    }
    return value;
  }

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    if (metaField.getBackReferenceField().getType() == FieldType.MULTILINK) {
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClass.getCode());
      Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(
          metaField.getLinkToMetaClass().getCode(), metaField.getBackReferenceField().getCode(),
          viewMapperBase.getId(obj, metaClass), true);
      if (linkIds != null) {
        DataSourceFromView linkedStore = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
        value = linkedStore
            .getTitleObject(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
      }
    } else {
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
          // todo: вот тут идентификатор надо приводить к реальному
          // типу
          .setCondition(
              new ConditionFieldImpl(metaField.getBackReferenceField()).equal(viewMapperBase.getId(obj, metaClass)));
      value = dataSourceFromView.getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
    }
    return value;
  }

  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    return null;
  }

  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    // TODO Auto-generated method stub
    return null;
  }
}
