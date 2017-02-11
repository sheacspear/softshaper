package ru.softshaper.web.admin.view.mapper.attr;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.bean.ViewSetting;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.utils.FieldCollection;
import ru.softshaper.web.admin.view.utils.ViewObjectsParams;

public class UniversalLinkDataViewAttrMapper extends DataViewAttrMapperBase {


  public UniversalLinkDataViewAttrMapper(MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ViewSettingFactory viewSetting, DefaultViewMapper viewMapperBase) {
    super(metaStorage, dataSourceFromViewStore, viewSetting, viewMapperBase);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getValueByObject(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value;
    value = viewMapperBase.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());
          if (FieldTypeView.LINK_INNER_OBJECT.equals(fieldView.getTypeView())) {
            ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier)
                .setFieldCollection(FieldCollection.ALL).build();
            value = dataSourceFromView.getFullObject(params);
          } else if (FieldTypeView.LINK_BROWSE.equals(fieldView.getTypeView())) {
            ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier)
                .setFieldCollection(FieldCollection.TITLE).build();
            value = dataSourceFromView.getTitleObject(params);
          }
        }
      }

    }
    return value;
  }

  @Override
  public Object getValueByTable(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = viewMapperBase.getValue(obj, metaField);
    if (value != null) {
      String stringValue = value.toString();
      int delimiterPosition = stringValue.lastIndexOf("@");
      if (delimiterPosition > 0) {
        String identifier = stringValue.substring(0, delimiterPosition);
        String linkedClassCode = stringValue.substring(delimiterPosition + 1);
        MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
        if (linkedClass != null) {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());

          ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass).ids().add(identifier)
              .setFieldCollection(FieldCollection.TITLE).build();
          TitleObjectView titleObject = dataSourceFromView.getTitleObject(params);
          value = titleObject == null ? value : titleObject.getTitle();
        }
      }
    }
    return value;
  }

  @Override
  public String getTitle(Object obj, MetaField metaField, ViewSetting fieldView) {
    Object value = viewMapperBase.getValue(obj, metaField);
    return value != null ? viewMapperBase.toString() : null;
  }

  @Override
  public ListObjectsView getVariants(MetaField metaField) {
    // TODO Auto-generated method stub
    return null;
  }
}
