package ru.softshaper.web.admin.view.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.*;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.obj.builder.FullObjectViewBuilder;
import ru.softshaper.web.admin.bean.obj.impl.FullObjectView;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.objlist.IColumnView;
import ru.softshaper.web.admin.bean.objlist.IListObjectsView;
import ru.softshaper.web.admin.bean.objlist.IObjectRowView;
import ru.softshaper.web.admin.bean.objlist.ITableObjectsView;
import ru.softshaper.web.admin.bean.objlist.impl.ColumnView;
import ru.softshaper.web.admin.bean.objlist.impl.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.impl.ObjectRowView;
import ru.softshaper.web.admin.bean.objlist.impl.TableObjectsView;
import ru.softshaper.web.admin.view.controller.impl.DataSourceFromViewImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовый маппер
 */
@Service
public class ViewObjectController implements IViewObjectController {

  /**
   * Хранилище, которое возвращает представление поля по его параметрам (табица
   * и колонка)
   */
  @Autowired
  private ViewSettingStore viewSetting;

  /**
   * MetaStorage
   */
  @Autowired
  private MetaStorage metaStorage;

  @Autowired
  private DataSourceStorage dataSourceStorage;
  /**
   *
   */
  private IViewAttrController defaultViewAttrController;

  /**
   *
   */
  private final Map<FieldType, IViewAttrController> attrmapper = Maps.newHashMap();

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewObjectController#registerAttrController(
   * ru.softshaper.services.meta.FieldType,
   * ru.softshaper.view.IViewAttrController)
   */
  @Override
  public void registerAttrController(FieldType fieldType, IViewAttrController viewAttrController) {
    attrmapper.put(fieldType, viewAttrController);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.view.IViewObjectController#setDefaultAttrController
   * (ru.softshaper.view.IViewAttrController)
   */
  @Override
  public void setDefaultAttrController(IViewAttrController viewAttrController) {
    defaultViewAttrController = viewAttrController;
  }

  /**
   * @param metaClass
   * @return
   */
  @Override
  public Map<MetaField, IColumnView> constructColumnsView(MetaClass metaClass) {
    Map<MetaField, IColumnView> columnsView = Maps.newLinkedHashMap();
    columnsView.put(null, new ColumnView("Идентификатор", "id", FieldTypeView.STRING_SINGLE, false));
    metaClass.getFields().forEach(dynamicField -> {
      ViewSetting typeView = viewSetting.getView(dynamicField);
      if (typeView.isTableField()) {
        columnsView.put(dynamicField,
            new ColumnView(dynamicField.getName(), dynamicField.getCode(), typeView.getTypeView()));
      }
    });
    return columnsView;
  }

  /**
   * @param titleFields
   * @return
   */
  private String constructTitle(Map<ViewSetting, String> titleFields) {
    return titleFields.keySet().stream().sorted((o1, o2) -> Integer.compare(o1.getNumber(), o2.getNumber()))
        .map(titleFields::get).reduce((s, s2) -> s.isEmpty() ? s2 : s + (s2.isEmpty() ? "" : " " + s2)).orElse("");
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataViewMapper#convertFullObject(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public <T> FullObjectView convertFullObject(T obj, String metaClassCode, ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    FullObjectViewBuilder view = FullObjectView.newBuilder(metaClass.getCode(), objectExtractor.getId(obj, metaClass));
    for (MetaField metaField : metaClass.getFields()) {
      IListObjectsView variants = null;
      ViewSetting fieldView = viewSetting.getView(metaField);
      FieldType fieldType = metaField.getType();
      // get values
      IViewAttrController attrMapper = attrmapper.get(fieldType);
      if (attrMapper == null) {
        attrMapper = defaultViewAttrController;
      }
      Object value = attrMapper.getValueByObject(obj, metaField, fieldView, objectExtractor);
      // get Title values
      if (fieldView.isTitleField()) {
        titleFields.put(fieldView, value == null ? "" : value.toString());
      }
      // find variant values
      if (attrMapper != null) {
        variants = attrMapper.getVariants(metaField, objectExtractor);
      }
      // add field
      view.addField(metaField, fieldView, value, variants);
    }
    String title = constructTitle(titleFields);
    view.setTitle(title);
    return view.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataViewMapper#convertTitleObject(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public <T> TitleObjectView convertTitleObject(T obj, String metaClassCode, ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    for (MetaField metaField : metaClass.getFields()) {
      ViewSetting fieldView = viewSetting.getView(metaField);
      if (fieldView.isTitleField()) {
        FieldType fieldType = metaField.getType();
        IViewAttrController attrMapper = attrmapper.get(fieldType);
        if (attrMapper == null) {
          attrMapper = defaultViewAttrController;
        }
        Object value = attrMapper.getTitle(obj, metaField, fieldView, objectExtractor);
        if (value != null) {
          titleFields.put(fieldView, value.toString());
        }
      }
    }
    String title = constructTitle(titleFields);
    if (title == null || title.isEmpty()) {
      title = objectExtractor.getId(obj, metaClass);
    }
    return new TitleObjectView(metaClass.getCode(), objectExtractor.getId(obj, metaClass), title);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#convertTableObjects(java.util.
   * Collection, java.lang.String, java.lang.Integer)
   */
  @Override
  public <T> ITableObjectsView convertTableObjects(Collection<T> objList, String metaClassCode, Integer total,
      ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    // column names
    Map<MetaField, IColumnView> columns = constructColumnsView(metaClass);
    // data by column
    ListMultimap<MetaField, Object> columnsViewData = ArrayListMultimap.create();
    // fill data by column
    for (T obj : objList) {
      columnsViewData.put(null, objectExtractor.getId(obj, metaClass));
      metaClass.getFields().forEach(metaField -> {
        ViewSetting fieldView = viewSetting.getView(metaField);
        if (fieldView.isTableField()) {
          FieldType fieldType = metaField.getType();
          // get values
          IViewAttrController attrMapper = attrmapper.get(fieldType);
          if (attrMapper == null) {
            attrMapper = defaultViewAttrController;
          }
          Object value = attrMapper.getValueByTable(obj, metaField, fieldView, objectExtractor);
          columnsViewData.put(metaField, value);
        }
      });
    }
    // replace link value & NxM
    List<MetaField> keys = new ArrayList<>(columnsViewData.keySet());
    for (MetaField fileld : keys) {
      if (fileld != null) {
        if (FieldType.LINK.equals(fileld.getType())) {
          fillLinkColumn(columnsViewData, fileld);
        } else if (FieldType.MULTILINK.equals(fileld.getType())) {
          // todo
        } else if (FieldType.BACK_REFERENCE.equals(fileld.getType())) {
          // todo
        }
      }
    }
    // transform data by column -> data by rows
    List<IObjectRowView> objectsView = Lists.newArrayList();
    int rowSize = columns.keySet().size();
    int objCnt = objList.size();
    for (int i = 0; i < objCnt; i++) {
      List<Object> listKeys = columnsViewData.get(null);
      List<Object> data = new ArrayList<>(rowSize);
      for (MetaField filed : columns.keySet()) {
        List<Object> fieldVales = columnsViewData.get(filed);
        data.add(fieldVales.get(i));
      }
      ObjectRowView objectRowView = new ObjectRowView(listKeys.get(i).toString(), data, null);
      objectsView.add(objectRowView);
    }
    return new TableObjectsView(metaClassCode, total != null ? total : objectsView.size(), columns.values(),
        objectsView);
  }

  private void fillLinkColumn(ListMultimap<MetaField, Object> columnsViewData, MetaField fileld) {
    MetaClass linkToMetaClass = fileld.getLinkToMetaClass();
    List<Object> ids = columnsViewData.get(fileld);
    Set<String> idsStr = Sets.newHashSet();
    if (ids != null) {
      for (Object id : ids) {
        if (id != null) {
          idsStr.add(id.toString());
        }
      }
    }

    if (idsStr != null && !idsStr.isEmpty()) {
      DataSourceFromView dataSourceFromView = getDataSourceFromView(linkToMetaClass.getCode());
      // get link values
      IListObjectsView listObjectsView = dataSourceFromView.getListObjects(ViewObjectsParams
          .newBuilder(linkToMetaClass).setFieldCollection(FieldCollection.TITLE).addIds(idsStr).build());
      // Map link value by key
      Map<Object, IObjectView> dataKeys = Maps.newHashMap();
      if (listObjectsView != null) {
        Collection<IObjectView> objViews = listObjectsView.getObjects();
        if (objViews != null) {
          for (IObjectView objTitle : objViews) {
            dataKeys.put(objTitle.getId(), objTitle);
          }
        }
      }
      // fill data ids
      Collection<Object> objs = Lists.newArrayList();
      for (Object id : ids) {
        objs.add(id!=null?dataKeys.get(id.toString()):null);
      }
      columnsViewData.replaceValues(fileld, objs);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#convertListObjects(java.util.
   * Collection, java.lang.String, java.lang.Integer)
   */
  @Override
  public <T> IListObjectsView convertListObjects(Collection<T> objects, String metaClassCode, Integer total,
      ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<IObjectView> titleObjects = objects.stream()
        .map(record -> convertTitleObject(record, metaClass.getCode(), objectExtractor)).collect(Collectors.toList());
    return new ListObjectsView(metaClassCode, total, titleObjects);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#getEmptyObj(java.lang.String,
   * java.util.Map)
   */
  @Override
  public <T> FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue,
      ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(contentCode);
    MetaClass content = metaStorage.getMetaClass(contentCode);
    Preconditions.checkNotNull(content);
    FullObjectViewBuilder view = FullObjectView.newBuilder(contentCode, null);
    Preconditions.checkNotNull(content.getFields());
    for (MetaField metaField : content.getFields()) {
      IViewAttrController attrMapper = attrmapper.get(metaField.getType());
      if (attrMapper == null) {
        attrMapper = defaultViewAttrController;
      }
      IListObjectsView variants = attrMapper.getVariants(metaField, objectExtractor);
      ViewSetting fieldView = viewSetting.getView(metaField);
      view.addField(metaField, fieldView, defValue.get(metaField.getCode()), variants);
    }
    return view.build();
  }

  @Override
  public DataSourceFromView getDataSourceFromView(String contentCode) {
    ContentDataSource<?> dataSource = dataSourceStorage.get(metaStorage.getMetaClass(contentCode));
    return new DataSourceFromViewImpl<>(this, dataSource);
  }

  @Override
  public Map<String, Object> parseAttrsFromView(MetaClass metaClass, Map<String, Object> viewAttrs) {
    final Map<String, Object> resultMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : viewAttrs.entrySet()) {
      MetaField metaField = metaClass.getField(entry.getKey());
      Object value = entry.getValue();
      IViewAttrController iViewAttrController = attrmapper.get(metaField.getType());
      if (iViewAttrController == null) {
        iViewAttrController = defaultViewAttrController;
      }
      Object objectValue = iViewAttrController.convertViewValueToObjectValue(value, metaField, viewSetting.getView(metaField));
      resultMap.put(metaField.getCode(), objectValue);
    }
    return resultMap;
  }
}
