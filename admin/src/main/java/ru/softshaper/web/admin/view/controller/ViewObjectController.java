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
import ru.softshaper.web.admin.view.IViewAttrController;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.store.ViewSettingStore;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  private  ViewSettingStore viewSetting;

  /**
   * MetaStorage
   */
  @Autowired
  private  MetaStorage metaStorage;

  /**
   * 
   */
  //private final ObjectExtractor<T> objectExtractor;

  /**
   * 
   */
  private IViewAttrController defaultViewAttrController;

  /**
   * 
   */
  private final Map<FieldType, IViewAttrController> attrmapper = Maps.newHashMap();

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewObjectController#registerAttrController(ru.softshaper.services.meta.FieldType, ru.softshaper.web.admin.view.IViewAttrController)
   */
  @Override
  public void registerAttrController(FieldType fieldType, IViewAttrController viewAttrController) {
    attrmapper.put(fieldType, viewAttrController);
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.view.IViewObjectController#setDefaultAttrController(ru.softshaper.web.admin.view.IViewAttrController)
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
  public <T> FullObjectView convertFullObject(T obj, String metaClassCode,ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    FullObjectViewBuilder view = FullObjectView.newBuilder(metaClass.getCode(), objectExtractor.getId(obj, metaClass));
    for (MetaField metaField : metaClass.getFields()) {
      ListObjectsView variants = null;
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
  public <T> TitleObjectView convertTitleObject(T obj, String metaClassCode,ObjectExtractor<T> objectExtractor) {
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
  public <T> TableObjectsView convertTableObjects(Collection<T> objList, String metaClassCode, Integer total,ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<ColumnView> columnsView = constructColumnsView(metaClass);
    List<ObjectRowView> objectsView = Lists.newArrayList();
    objList.forEach(obj -> {
      List<Object> data = Lists.newArrayList();
      data.add(objectExtractor.getId(obj, metaClass));
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
          data.add(value);
        }
      });
      //
      ObjectRowView objectRowView = new ObjectRowView(objectExtractor.getId(obj, metaClass), data, null);
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
  public <T> ListObjectsView convertListObjects(Collection<T> objects, String metaClassCode, Integer total,ObjectExtractor<T> objectExtractor) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<TitleObjectView> titleObjects = objects.stream().map(record -> convertTitleObject(record, metaClass.getCode(),objectExtractor)).collect(Collectors.toList());
    return new ListObjectsView(metaClassCode, total, titleObjects);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataViewMapper#getEmptyObj(java.lang.String,
   * java.util.Map)
   */
  @Override
  public <T> FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue,ObjectExtractor<T> objectExtractor) {
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
      ListObjectsView variants = attrMapper.getVariants(metaField, objectExtractor);
      ViewSetting fieldView = viewSetting.getView(metaField);
      view.addField(metaField, fieldView, defValue.get(metaField.getCode()), variants);
    }
    return view.build();
  }

  @FunctionalInterface
  protected interface Extractor<O, V> {
    V value(O from);
  }
}
