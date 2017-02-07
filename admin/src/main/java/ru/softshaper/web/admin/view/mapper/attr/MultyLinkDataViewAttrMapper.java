package ru.softshaper.web.admin.view.mapper.attr;

import java.util.Collection;
import java.util.Collections;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
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

public class MultyLinkDataViewAttrMapper extends DataViewAttrMapperBase {


  public MultyLinkDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, ViewMapperBase viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    MetaClass metaClass = metaField.getOwner();
    Object value;
    DataSourceFromView dataSourceFrom = dataSourceFromViewStore.get(metaField.getOwner().getCode());
    DataSourceFromView dataSourceTo = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
    Collection<String> objectsIds = dataSourceFrom.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getCode(),
        viewMapperBase.getId(obj, metaClass), false);
    ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
        .addIds(objectsIds);
    if (FieldTypeView.MULTILINK_CHECKBOX.equals(fieldView.getTypeView())) {
      ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TITLE).build();
      value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getListObjects(params) : null;
    } else {
      ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TABLE).build();
      value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getTableObjects(params)
          : emptyTableObjectsView(metaField.getLinkToMetaClass());
    }
    return value;
  }

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClass.getCode());
    Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getCode(),
        viewMapperBase.getId(obj, metaClass), false);
    if (linkIds != null && !linkIds.isEmpty()) {
      DataSourceFromView linkedStore = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
      value = linkedStore
          .getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
    }
    return value;
  }

  /**
   * @param metaClass
   * @return
   */
  private TableObjectsView emptyTableObjectsView(MetaClass metaClass) {
    return new TableObjectsView(metaClass.getCode(), 0, viewMapperBase.constructColumnsView(metaClass),
        Collections.emptyList());
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
