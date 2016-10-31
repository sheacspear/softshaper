package ru.zorb.web.view.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.meta.conditions.impl.ConditionFieldImpl;
import ru.zorb.services.meta.staticcontent.meta.FileObjectStaticContent;
import ru.zorb.web.bean.FieldTypeView;
import ru.zorb.web.bean.obj.FullObjectView;
import ru.zorb.web.bean.obj.FullObjectViewBuilder;
import ru.zorb.web.bean.obj.ObjectView;
import ru.zorb.web.bean.obj.TitleObjectView;
import ru.zorb.web.bean.objlist.ColumnView;
import ru.zorb.web.bean.objlist.ListObjectsView;
import ru.zorb.web.bean.objlist.ObjectRowView;
import ru.zorb.web.bean.objlist.TableObjectsView;
import ru.zorb.web.view.DataSourceFromView;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.DataViewMapper;
import ru.zorb.web.view.bean.ViewSetting;
import ru.zorb.web.view.impl.ViewSettingFactory;
import ru.zorb.web.view.utils.FieldCollection;
import ru.zorb.web.view.utils.ViewObjectParamsBuilder;
import ru.zorb.web.view.utils.ViewObjectsParams;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовый маппер
 */
public abstract class ViewMapperBase<T> implements DataViewMapper<T> {

  /**
   * Хранилище, которое возвращает представление поля по его параметрам (табица и колонка)
   */
  private final ViewSettingFactory viewSetting;

  /**
   * MetaStorage
   */
  private final MetaStorage metaStorage;

  /**
   * Хранилище Источник данных для формы
   */
  private final DataSourceFromViewStore dataSourceFromViewStore;

  public ViewMapperBase(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    this.viewSetting = viewSetting;
    this.metaStorage = metaStorage;
    this.dataSourceFromViewStore = dataSourceFromViewStore;
  }

  protected abstract String getId(T obj, MetaClass metaClass);

  protected abstract Object getValue(T obj, MetaField field);

  protected FullObjectView convertFullObject(T obj, MetaClass metaClass) {
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    FullObjectViewBuilder view = FullObjectView.newBuilder(metaClass.getCode(), getId(obj, metaClass));
    for (MetaField metaField : metaClass.getFields()) {
      ListObjectsView variants = null;
      ViewSetting fieldView = viewSetting.getView(metaClass.getCode(), metaField.getCode());
      FieldType fieldType = metaField.getType();
      Object value;
      if (FieldType.UNIVERSAL_LINK.equals(fieldType)) {
        value = getValue(obj, metaField);
        if (value != null) {
          String stringValue = value.toString();
          int delimiterPosition = stringValue.lastIndexOf("@");
          if(delimiterPosition>0){

          String identifier = stringValue.substring(0, delimiterPosition);
          String linkedClassCode = stringValue.substring(delimiterPosition + 1);
          MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
          if (linkedClass != null) {
            DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());
            if (FieldTypeView.LINK_INNER_OBJECT.equals(fieldView.getTypeView())) {
              ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass)
                  .ids().add(identifier)
                  .setFieldCollection(FieldCollection.ALL)
                  .build();
              value = dataSourceFromView.getFullObject(params);
            } else if (FieldTypeView.LINK_BROWSE.equals(fieldView.getTypeView())) {
              ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass)
                  .ids().add(identifier)
                  .setFieldCollection(FieldCollection.TITLE)
                  .build();
              value = dataSourceFromView.getTitleObject(params);
            }
          }
          }

        }
      } else if (FieldType.LINK.equals(fieldType)) {
        ObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE);
        if (FieldTypeView.LINK_SELECTBOX.equals(fieldView.getTypeView()) && metaField.getLinkToMetaClass() != null) {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
          variants = dataSourceFromView.getListObjects(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).setFieldCollection(FieldCollection.TITLE).build());
          value = valueLink == null ? null : valueLink.getId();
        } else {
          value = valueLink;
        }
      } else if (FieldType.BACK_REFERENCE.equals(fieldType)) {
        if (metaField.getBackReferenceField().getType().equals(FieldType.MULTILINK)) {
          DataSourceFromView dataSourceFrom = dataSourceFromViewStore.get(metaField.getOwner().getCode());
          DataSourceFromView dataSourceTo = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
          Collection<String> objectsIds = dataSourceTo.getObjectsIdsByMultifield(metaClass.getCode(), metaField.getBackReferenceField().getCode(),
              getId(obj, metaClass), true);
          if (objectsIds != null && !objectsIds.isEmpty()) {
            ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
                .addIds(objectsIds);
            if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
              ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TITLE).build();
              ListObjectsView listObjectsView = objectsIds != null ? dataSourceFrom.getListObjects(params) : null;
              if (listObjectsView != null) {
                listObjectsView.setBackLinkAttr(metaField.getBackReferenceField().getCode());
              }
              value = listObjectsView;
            } else {
              ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TABLE).build();
              TableObjectsView tableObjectsView = objectsIds != null ? dataSourceFrom.getTableObjects(params) : null;
              if (tableObjectsView != null) {
                tableObjectsView.setBackLinkAttr(metaField.getBackReferenceField().getCode());
              }
              value = tableObjectsView;
            }
          } else {
            if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
              value = new ListObjectsView(metaField.getLinkToMetaClass().getCode(), 0, Collections.emptyList(),
                  metaField.getBackReferenceField().getCode());
            }else {
              value = emptyTableObjectsView(metaField.getLinkToMetaClass(), metaField.getBackReferenceField());
            }
          }
        } else {
          DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
          ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
              .setCondition(new ConditionFieldImpl(metaField.getBackReferenceField()).equal(getId(obj, metaClass)));
          if (FieldTypeView.BACK_REFERENCE_LIST.equals(fieldView.getTypeView())) {
            ListObjectsView listObjects = dataSourceFromView.getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
            listObjects.setBackLinkAttr(metaField.getBackReferenceField().getCode());
            value = listObjects;
          } else {
            value = dataSourceFromView.getTableObjects(paramsBuilder.setFieldCollection(FieldCollection.TABLE).build());
          }
        }
      } else if (FieldType.MULTILINK.equals(fieldType)) {
        DataSourceFromView dataSourceFrom = dataSourceFromViewStore.get(metaField.getOwner().getCode());
        DataSourceFromView dataSourceTo = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
        Collection<String> objectsIds = dataSourceFrom.getObjectsIdsByMultifield(metaClass.getCode(),
            metaField.getCode(), getId(obj, metaClass), false);
        ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
            .addIds(objectsIds);
        if (FieldTypeView.MULTILINK_CHECKBOX.equals(fieldView.getTypeView())) {
          ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TITLE).build();
          value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getListObjects(params) : null;
        } else {
          ViewObjectsParams params = paramsBuilder.setFieldCollection(FieldCollection.TABLE).build();
          value = objectsIds != null && !objectsIds.isEmpty() ? dataSourceTo.getTableObjects(params) : emptyTableObjectsView(metaField.getLinkToMetaClass(), null);
        }
      } else if (FieldType.FILE.equals(fieldType)) {
        value = getLinkedValue(obj, metaField, FieldCollection.TITLE);
      } else {
        value = getValue(obj, metaField);
      }
      if (fieldView.isTitleField()) {
        titleFields.put(fieldView, value == null ? "" : value.toString());
      }
      view.addFieldVariant(fieldView, value, variants);
    }
    String title = constructTitle(titleFields);
    view.setTitle(title);
    return view.build();
  }

  private TableObjectsView emptyTableObjectsView(MetaClass metaClass, MetaField backReferenceField) {
    return new TableObjectsView(metaClass.getCode(), 0, constructColumnsView(metaClass),
        Collections.emptyList(), backReferenceField == null ? null : backReferenceField.getCode());
  }


  private TitleObjectView convertTitleObject(T obj, MetaClass metaClass) {
    Map<ViewSetting, String> titleFields = Maps.newHashMap();
    for (MetaField metaField : metaClass.getFields()) {
      if (metaField.getType().equals(FieldType.MULTILINK)
          || metaField.getType().equals(FieldType.BACK_REFERENCE)
          || metaField.getType().equals(FieldType.FILE)) {
        continue;
      }
      ViewSetting fieldView = viewSetting.getView(metaClass.getCode(), metaField.getCode());
      if (fieldView.isTitleField()) {
        FieldType fieldType = metaField.getType();
        if (FieldType.LINK.equals(fieldType)) {
          ObjectView valueLink = getLinkedValue(obj, metaField, FieldCollection.TITLE);
          if (valueLink != null) {
            titleFields.put(fieldView, valueLink.getTitle());
          }
        } else {
          Object value = getValue(obj, metaField);
          if (value != null) {
            titleFields.put(fieldView, value.toString());
          }
        }
      }
    }
    String title = constructTitle(titleFields);
    if (title == null || title.isEmpty()) {
      title = getId(obj, metaClass);
    }
    return new TitleObjectView(metaClass.getCode(), getId(obj, metaClass), title);
  }




  private TableObjectsView convertTableObjectsView(Collection<T> objects, String metaClassCode, Integer total, String backLinkAttr) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<ColumnView> columnsView = constructColumnsView(metaClass);
    List<ObjectRowView> objectsView = Lists.newArrayList();


    //todo: за такое порно, не грех и посадить

    //todo: кого сажать тебя или меня?
    Map<MetaField, Map<ObjectRowView, String>> linkedValues = new HashMap<>();
    objects.forEach(obj -> {
      List<Object> data = Lists.newArrayList();
      data.add(getId(obj, metaClass));
      Map<MetaField, String> linkedValuesOfObject = new HashMap<>();
      metaClass.getFields().forEach(metaField -> {
        ViewSetting fieldView = viewSetting.getView(metaClass.getCode(), metaField.getCode());
        if (fieldView.isTableField()) {
          FieldType fieldType = metaField.getType();
          if (!fieldType.equals(FieldType.MULTILINK)
              && !fieldType.equals(FieldType.BACK_REFERENCE)) {
            Object value = getValue(obj, metaField);
            if (fieldType == FieldType.LINK && value != null) {
              linkedValuesOfObject.put(metaField, value.toString());
            } else if (fieldType == FieldType.UNIVERSAL_LINK) {
              if (value != null) {
                String stringValue = value.toString();
                int delimiterPosition = stringValue.lastIndexOf("@");
                if(delimiterPosition>0) {
                String identifier = stringValue.substring(0, delimiterPosition);
                String linkedClassCode = stringValue.substring(delimiterPosition + 1);
                MetaClass linkedClass = metaStorage.getMetaClass(linkedClassCode);
                if (linkedClass != null) {
                  DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(linkedClass.getCode());

                    ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedClass)
                        .ids().add(identifier)
                        .setFieldCollection(FieldCollection.TITLE)
                        .build();
                  TitleObjectView titleObject = dataSourceFromView.getTitleObject(params);
                  value = titleObject == null ? value : titleObject.getTitle();
                }
              }
            }
            }
            data.add(value);
          } else {
            Object value = null;
            if (fieldType == FieldType.BACK_REFERENCE) {
              if (metaField.getBackReferenceField().getType() == FieldType.MULTILINK) {
                DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClassCode);
                Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaField.getLinkToMetaClass().getCode(),
                    metaField.getBackReferenceField().getCode(), getId(obj, metaClass), true);
                if (linkIds != null) {
                  DataSourceFromView linkedStore = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
                  value = linkedStore.getTitleObject(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
                }
              } else {
                DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
                ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass())
                                                                                            //todo: вот тут идентификатор надо приводить к реальному типу
                    .setCondition(new ConditionFieldImpl(metaField.getBackReferenceField()).equal(getId(obj, metaClass)));
                value = dataSourceFromView.getListObjects(paramsBuilder.setFieldCollection(FieldCollection.TITLE).build());
              }
            }
            if (fieldType == FieldType.MULTILINK) {
              DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClassCode);
              Collection<String> linkIds = dataSourceFromView.getObjectsIdsByMultifield(metaClassCode, metaField.getCode(),
                  getId(obj, metaClass), false);
              if (linkIds != null) {
                DataSourceFromView linkedStore = dataSourceFromViewStore.get(metaField.getLinkToMetaClass().getCode());
                value = linkedStore.getTitleObject(ViewObjectsParams.newBuilder(metaField.getLinkToMetaClass()).addIds(linkIds).build());
              }
            }
            data.add(value);
          }
        }
      });
      ObjectRowView objectRowView = new ObjectRowView(getId(obj, metaClass), data, null);
      for (Map.Entry<MetaField, String> entry : linkedValuesOfObject.entrySet()) {
        Map<ObjectRowView, String> objectRowViewStringMap = linkedValues.get(entry.getKey());
        if (objectRowViewStringMap == null) {
          objectRowViewStringMap = new HashMap<>();
          linkedValues.put(entry.getKey(), objectRowViewStringMap);
        }
        objectRowViewStringMap.put(objectRowView, entry.getValue());
      }
      objectsView.add(objectRowView);
    });
    for (MetaField field : linkedValues.keySet()) {
      Map<ObjectRowView, String> objectRowViewStringMap = linkedValues.get(field);
      DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(field.getLinkToMetaClass().getCode());
      ViewObjectsParams params = ViewObjectsParams.newBuilder(field.getLinkToMetaClass())
          .addIds(objectRowViewStringMap.values())
          .setFieldCollection(FieldCollection.TITLE)
          .build();
      int columnIndex = 0;
      for (ColumnView columnView : columnsView) {
        if (columnView.getKey().equals(field.getCode())) {
          break;
        }
        columnIndex++;
      }
      ListObjectsView ListObjectView = dataSourceFromView.getListObjects(params);
      for (Map.Entry<ObjectRowView, String> rowLink : objectRowViewStringMap.entrySet()) {
        ObjectRowView row = rowLink.getKey();
        for (TitleObjectView objectView : ListObjectView.getObjects()) {
          if (row.getData().get(columnIndex) != null && objectView.getId().equals(row.getData().get(columnIndex).toString())) {
            row.getData().set(columnIndex, objectView.getTitle());
          }
        }
      }
    }
    return new TableObjectsView(metaClassCode, total != null ? total : objectsView.size(), columnsView, objectsView, backLinkAttr);
  }

  private List<ColumnView> constructColumnsView(MetaClass metaClass) {
    List<ColumnView> columnsView = Lists.newArrayList();
    columnsView.add(new ColumnView("Идентификатор", "id", FieldTypeView.STRING_SINGLE, false));

    metaClass.getFields().forEach(dynamicField -> {
      ViewSetting typeView = viewSetting.getView(metaClass.getCode(), dynamicField.getCode());
      if (typeView.isTableField()) {
        columnsView.add(new ColumnView(dynamicField.getName(), dynamicField.getCode(), typeView.getTypeView()));
      }
    });
    return columnsView;
  }


  private ObjectView getLinkedValue(T obj, MetaField metaField, FieldCollection fieldCollection) {
    Object linkedObjId = getValue(obj, metaField);
    if (linkedObjId == null) {
      return null;
    }
    MetaClass linkedMetaClass = metaField.getLinkToMetaClass();
    if (metaField.getType().equals(FieldType.FILE)) {
      linkedMetaClass = metaStorage.getMetaClass(FileObjectStaticContent.META_CLASS);
    }
    ViewObjectsParams params = ViewObjectsParams.newBuilder(linkedMetaClass)
        .ids().add(linkedObjId.toString())
        .setFieldCollection(fieldCollection)
        .build();
    if (FieldCollection.TITLE.equals(fieldCollection)) {
      return dataSourceFromViewStore.get(linkedMetaClass.getCode()).getTitleObject(params);
    } else {
      return dataSourceFromViewStore.get(linkedMetaClass.getCode()).getFullObject(params);
    }
  }

  private String constructTitle(Map<ViewSetting, String> titleFields) {
    return titleFields.keySet().stream()
        .sorted((o1, o2) -> Integer.compare(o1.getNumber(), o2.getNumber()))
        .map(titleFields::get)
        .reduce((s, s2) -> s.isEmpty() ? s2 : s + (s2.isEmpty() ? "" : " " + s2))
        .orElse("");
  }


  protected ViewSettingFactory getViewSetting() {
    return viewSetting;
  }

  protected MetaStorage getMetaStorage() {
    return metaStorage;
  }

  protected DataSourceFromViewStore getDataSourceFromViewStore() {
    return dataSourceFromViewStore;
  }

  @Override
  public FullObjectView convertFullObject(T obj, String metaClassCode) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    return convertFullObject(obj, metaClass);
  }

  @Override
  public TitleObjectView convertTitleObject(T obj, String metaClassCode) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    return convertTitleObject(obj, metaClass);
  }

  @Override
  public TableObjectsView convertTableObjects(Collection<T> objList, String metaClassCode, Integer total, String backLinkAttr) {
    return convertTableObjectsView(objList, metaClassCode, total, backLinkAttr);
  }

  @Override
  public ListObjectsView convertListObjects(Collection<T> objects, String metaClassCode, Integer total, String backLinkAttr) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    List<TitleObjectView> titleObjects = objects.stream()
        .map(record -> convertTitleObject(record, metaClass))
        .collect(Collectors.toList());
    return new ListObjectsView(metaClassCode, total, titleObjects, backLinkAttr);
  }

  @Override
  public FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue) {
    Preconditions.checkNotNull(contentCode);
    MetaClass content = metaStorage.getMetaClass(contentCode);
    Preconditions.checkNotNull(content);
    FullObjectViewBuilder view = FullObjectView.newBuilder(contentCode, null);
    Preconditions.checkNotNull(content.getFields());
    for (MetaField metaField : content.getFields()) {
      ViewSetting fieldView = viewSetting.getView(content.getCode(), metaField.getCode());
      if (fieldView.getTypeView().equals(FieldTypeView.LINK_SELECTBOX)) {
        view.addFieldVariantLinkClass(fieldView, defValue.get(metaField.getCode()), metaField.getLinkToMetaClass().getCode(), getVariants(metaField.getLinkToMetaClass()));
      } else {
        view.addField(fieldView, defValue.get(metaField.getCode()));
      }
    }
    return view.build();
  }

  private ListObjectsView getVariants(MetaClass metaClass) {
    DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClass.getCode());
    return dataSourceFromView.getListObjects(ViewObjectsParams.newBuilder(metaClass).setFieldCollection(FieldCollection.TITLE).build());
  }

  @FunctionalInterface
  protected interface Extractor<O, V> {
    V value(O from);
  }
}
