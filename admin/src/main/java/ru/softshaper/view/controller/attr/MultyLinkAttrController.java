package ru.softshaper.view.controller.attr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.bean.objlist.ITableObjectsView;
import ru.softshaper.view.bean.objlist.impl.TableObjectsView;
import ru.softshaper.view.controller.DataSourceFromView;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectParamsBuilder;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.ViewSetting;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;

/**
 *
 */
@Component
public class MultyLinkAttrController extends AttrControllerBase {

  @Autowired
  @Qualifier("multilinkField")
  private FieldConverter fieldConverter;

  @PostConstruct
  void init() {
    viewObjectController.registerAttrController(FieldType.MULTILINK, this);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewAttrController#getValueByObject(java.lang
   * .Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
   */
  @Override
  public <T> Object getValueByObject(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    MetaClass metaClass = metaField.getOwner();
    Object value;
    DataSourceFromView dataSourceFrom = viewObjectController.getDataSourceFromView(metaField.getOwner().getCode());
    DataSourceFromView dataSourceTo = viewObjectController.getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
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
   * ru.softshaper.view.IViewAttrController#getValueByTable(java.lang.
   * Object, ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
   */
  @Override
  public <T> Object getValueByTable(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    Object value = null;
    MetaClass metaClass = metaField.getOwner();
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(metaClass.getCode());
    Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getCode(), objectExtractor.getId(obj, metaClass),
        false);
    if (linkIds != null && !linkIds.isEmpty()) {
      DataSourceFromView linkedStore = viewObjectController.getDataSourceFromView(metaField.getLinkToMetaClass().getCode());
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
  private <T> ITableObjectsView emptyTableObjectsView(MetaClass metaClass, ObjectExtractor<T> objectExtractor) {
    return new TableObjectsView(metaClass.getCode(), 0, viewObjectController.constructColumnsView(metaClass).values(), Collections.emptyList());
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewAttrController#getTitle(java.lang.Object,
   * ru.softshaper.services.meta.MetaField,
   * ru.softshaper.view.viewsettings.impl.ViewSettingImpl)
   */
  @Override
  public <T> String getTitle(T obj, MetaField metaField, ViewSetting fieldView, ObjectExtractor<T> objectExtractor) {
    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewAttrController#getVariants(ru.softshaper.
   * services.meta.MetaField)
   */
  @Override
  public <T> IListObjectsView getVariants(MetaField metaField, ObjectExtractor<T> objectExtractor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected FieldConverter getFieldConverter() {
    return fieldConverter;
  }
}
