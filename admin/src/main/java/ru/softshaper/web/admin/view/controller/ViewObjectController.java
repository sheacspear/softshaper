package ru.softshaper.web.admin.view.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.builder.FullObjectViewBuilder;
import ru.softshaper.web.admin.bean.obj.impl.FullObjectView;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.obj.impl.ViewSetting;
import ru.softshaper.web.admin.bean.objlist.ColumnView;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.ObjectRowView;
import ru.softshaper.web.admin.bean.objlist.TableObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.IViewAttrController;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.controller.attr.BackLinkAttrController;
import ru.softshaper.web.admin.view.controller.attr.DefaultAttrController;
import ru.softshaper.web.admin.view.controller.attr.FileAttrController;
import ru.softshaper.web.admin.view.controller.attr.LinkAttrController;
import ru.softshaper.web.admin.view.controller.attr.MultyLinkAttrController;
import ru.softshaper.web.admin.view.controller.attr.UniversalLinkAttrController;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовый маппер
 */
public class ViewObjectController<T> implements IViewObjectController<T> {

  /**
   * Хранилище, которое возвращает представление поля по его параметрам (табица
   * и колонка)
   */
  private final ViewSettingStore viewSetting;

  /**
   * MetaStorage
   */
  private final MetaStorage metaStorage;

  /**
   * 
   */
  private final ObjectExtractor<T> objectExtractor;

  /**
   * 
   */
  private final IViewAttrController defaultDataViewAttrMapper;

  /**
   * 
   */
  Map<FieldType, IViewAttrController> attrmapper = Maps.newHashMap();

  /**
   * 
   */
  public ViewObjectController(ViewSettingStore viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore,
      ObjectExtractor<T> objectExtractor) {
    this.viewSetting = viewSetting;
    this.metaStorage = metaStorage;
    attrmapper.put(FieldType.UNIVERSAL_LINK, new UniversalLinkAttrController(metaStorage, dataSourceFromViewStore, viewSetting,(ViewObjectController<Object>) this));
    attrmapper.put(FieldType.LINK, new LinkAttrController(metaStorage, dataSourceFromViewStore, viewSetting, (ViewObjectController<Object>) this));
    attrmapper.put(FieldType.BACK_REFERENCE, new BackLinkAttrController(metaStorage, dataSourceFromViewStore, viewSetting, (ViewObjectController<Object>) this));
    attrmapper.put(FieldType.MULTILINK, new MultyLinkAttrController(metaStorage, dataSourceFromViewStore, viewSetting, (ViewObjectController<Object>) this));
    attrmapper.put(FieldType.FILE, new FileAttrController(metaStorage, dataSourceFromViewStore, viewSetting, (ViewObjectController<Object>) this));
    defaultDataViewAttrMapper = new DefaultAttrController(metaStorage, dataSourceFromViewStore, viewSetting, (ViewObjectController<Object>) this);
    this.objectExtractor = objectExtractor;
  }

  /**
   * @param metaClass
   * @return
   */
  public List<ColumnView> constructColumnsView(MetaClass metaClass) {
    List<ColumnView> columnsView = Lists.newArrayList();
    columnsView.add(new ColumnView("Идентификатор", "id", FieldTypeView.STRING_SINGLE, false));
    metaClass.getFields().forEach(dynamicField -> {
      ViewSetting typeView = viewSetting.getView(dynamicField);
      if (typeView.isTableField()) {
        columnsView.add(new ColumnView(dynamicField.getName(), dynamicField.getCode(), typeView.getTypeView()));
      }
    });
    return columnsView;
  }

  /**
   * @param titleFields
   * @return
   */
  private String constructTitle(Map<ViewSetting, String> titleFields) {
    return titleFields.keySet().stream().sorted((o1, o2) -> Integer.compare(o1.getNumber(), o2.getNumber())).map(titleFields::get)
        .reduce((s, s2) -> s.isEmpty() ? s2 : s + (s2.isEmpty() ? "" : " " + s2)).orElse("");
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataViewMapper#convertFullObject(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public FullObjectView convertFullObject(T obj, String metaClassCode) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    FullObjectViewBuilder view = FullObjectView.newBuilder(metaClass.getCode(), getId(obj, metaClass));
    for (MetaField metaField : metaClass.getFields()) {
      ListObjectsView variants = null;
      ViewSetting fieldView = viewSetting.getView(metaField);
      FieldType fieldType = metaField.getType();
      // get values
      IViewAttrController attrMapper = attrmapper.get(fieldType);
      if (attrMapper == null) {
        attrMapper = defaultDataViewAttrMapper;
      }
      Object value = attrMapper.getValueByObject(obj, metaField, fieldView);
      // get Title values
      if (fieldView.isTitleField()) {
        titleFields.put(fieldView, value == null ? "" : value.toString());
      }
      // find variant values
      if (attrMapper != null) {
        variants = attrMapper.getVariants(metaField);
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
  public TitleObjectView convertTitleObject(T obj, String metaClassCode) {
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
          attrMapper = defaultDataViewAttrMapper;
        }
        Object value = attrMapper.getTitle(obj, metaField, fieldView);
        if (value != null) {
          titleFields.put(fieldView, value.toString());
        }
      }
    }
    String title = constructTitle(titleFields);
    if (title == null || title.isEmpty()) {
      title = getId(obj, metaClass);
    }
    return new TitleObjectView(metaClass.getCode(), getId(obj, metaClass), title);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#convertTableObjects(java.util.
   * Collection, java.lang.String, java.lang.Integer)
   */
  @Override
  public TableObjectsView convertTableObjects(Collection<T> objList, String metaClassCode, Integer total) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<ColumnView> columnsView = constructColumnsView(metaClass);
    List<ObjectRowView> objectsView = Lists.newArrayList();
    objList.forEach(obj -> {
      List<Object> data = Lists.newArrayList();
      data.add(getId(obj, metaClass));
      metaClass.getFields().forEach(metaField -> {
        ViewSetting fieldView = viewSetting.getView(metaField);
        if (fieldView.isTableField()) {
          FieldType fieldType = metaField.getType();
          // get values
          IViewAttrController attrMapper = attrmapper.get(fieldType);
          if (attrMapper == null) {
            attrMapper = defaultDataViewAttrMapper;
          }
          Object value = attrMapper.getValueByTable(obj, metaField, fieldView);
          data.add(value);
        }
      });
      //
      ObjectRowView objectRowView = new ObjectRowView(getId(obj, metaClass), data, null);
      objectsView.add(objectRowView);

      // хз что это такое
      /*
       * for (Map.Entry<MetaField, String> entry :
       * linkedValuesOfObject.entrySet()) { Map<ObjectRowView, String>
       * objectRowViewStringMap = linkedValues.get(entry.getKey()); if
       * (objectRowViewStringMap == null) { objectRowViewStringMap = new
       * HashMap<>(); linkedValues.put(entry.getKey(), objectRowViewStringMap);
       * } objectRowViewStringMap.put(objectRowView, entry.getValue()); }
       */

    });

    // хз что это такое
    /*
     * for (MetaField field : linkedValues.keySet()) { Map<ObjectRowView,
     * String> objectRowViewStringMap = linkedValues.get(field);
     * DataSourceFromView dataSourceFromView =
     * dataSourceFromViewStore.get(field.getLinkToMetaClass().getCode());
     * ViewObjectsParams params =
     * ViewObjectsParams.newBuilder(field.getLinkToMetaClass())
     * .addIds(objectRowViewStringMap.values()).setFieldCollection(
     * FieldCollection.TITLE).build(); int columnIndex = 0; for (ColumnView
     * columnView : columnsView) { if
     * (columnView.getKey().equals(field.getCode())) { break; } columnIndex++; }
     * ListObjectsView ListObjectView =
     * dataSourceFromView.getListObjects(params); for (Map.Entry<ObjectRowView,
     * String> rowLink : objectRowViewStringMap.entrySet()) { ObjectRowView row
     * = rowLink.getKey(); for (TitleObjectView objectView :
     * ListObjectView.getObjects()) { if (row.getData().get(columnIndex) != null
     * && objectView.getId().equals(row.getData().get(columnIndex).toString()))
     * { row.getData().set(columnIndex, objectView.getTitle()); } } } }
     */

    return new TableObjectsView(metaClassCode, total != null ? total : objectsView.size(), columnsView, objectsView);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#convertListObjects(java.util.
   * Collection, java.lang.String, java.lang.Integer)
   */
  @Override
  public ListObjectsView convertListObjects(Collection<T> objects, String metaClassCode, Integer total) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<TitleObjectView> titleObjects = objects.stream().map(record -> convertTitleObject(record, metaClass.getCode())).collect(Collectors.toList());
    return new ListObjectsView(metaClassCode, total, titleObjects);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#getEmptyObj(java.lang.String,
   * java.util.Map)
   */
  @Override
  public FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue) {
    Preconditions.checkNotNull(contentCode);
    MetaClass content = metaStorage.getMetaClass(contentCode);
    Preconditions.checkNotNull(content);
    FullObjectViewBuilder view = FullObjectView.newBuilder(contentCode, null);
    Preconditions.checkNotNull(content.getFields());
    for (MetaField metaField : content.getFields()) {
      IViewAttrController attrMapper = attrmapper.get(metaField.getType());
      if (attrMapper == null) {
        attrMapper = defaultDataViewAttrMapper;
      }
      ListObjectsView variants = attrMapper.getVariants(metaField);
      ViewSetting fieldView = viewSetting.getView(metaField);
      view.addField(metaField, fieldView, defValue.get(metaField.getCode()), variants);
    }
    return view.build();
  }

  public String getId(T obj, MetaClass metaClass) {
    return objectExtractor.getId(obj, metaClass);
  }

  public Object getValue(T obj, MetaField field) {
    return objectExtractor.getValue(obj, field);
  }

  @FunctionalInterface
  protected interface Extractor<O, V> {
    V value(O from);
  }
}
