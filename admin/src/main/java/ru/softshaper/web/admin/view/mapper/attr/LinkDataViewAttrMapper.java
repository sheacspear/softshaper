package ru.softshaper.web.admin.view.mapper.attr;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.bean.ViewSetting;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.ViewMapperBase;
import ru.softshaper.web.admin.view.utils.FieldCollection;
import ru.softshaper.web.admin.view.utils.ViewObjectsParams;

public class LinkDataViewAttrMapper extends DataViewAttrMapperBase {

  public LinkDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, ViewMapperBase viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

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

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    return viewMapperBase.getValue(obj, metaField);
  }

  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    IObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE);
    if (valueLink != null) {
      return valueLink.getTitle();
    }
    return null;
  }

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
