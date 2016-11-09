package ru.softshaper.datasource.meta;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 11.10.2016.
 */
@Component
@Qualifier("fieldTypeView")
public class FieldTypeViewDataSource implements ContentDataSource<FieldTypeView> {
  @Override
  public Collection<FieldTypeView> getObjects(GetObjectsParams params) {
    Collection<FieldTypeView> fieldsTypeViews = FieldTypeView.getAll();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      fieldsTypeViews = fieldsTypeViews.stream()
          .filter(fieldType -> params.getIds().contains(fieldType.getCode()))
          .collect(Collectors.toSet());
    }
    if (params.getCondition() != null) {
      throw new RuntimeException("Not implemented yet!");
    }
    if (params.getOrderFields() != null && !params.getOrderFields().isEmpty()) {
      List<FieldTypeView> fieldTypeList = new ArrayList<>(fieldsTypeViews);
      LinkedHashMap<MetaField, SortOrder> orderFields = params.getOrderFields();
      orderFields.forEach((metaField, sortOrder)
          -> Collections.sort(fieldTypeList, (fieldType1, fieldType2) -> {
        if (FieldTypeViewStaticContent.Field.code.equals(metaField.getCode())) {
          return fieldType1.getCode().compareTo(fieldType2.getCode());
        } else if (FieldTypeViewStaticContent.Field.name.equals(metaField.getCode())) {
          return fieldType1.getName().compareTo(fieldType2.getName());
        }
        throw new RuntimeException("Unknown sort field " + metaField);
      }));
      fieldsTypeViews = fieldTypeList;
    }
    if (params.getLimit() < Integer.MAX_VALUE || params.getOffset() > 0) {
      fieldsTypeViews = fieldsTypeViews.stream()
          .skip(params.getOffset())
          .limit(params.getLimit())
          .collect(Collectors.toList());
    }
    return fieldsTypeViews;
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }

  @Override
  public FieldTypeView getObj(GetObjectsParams params) {
    Collection<FieldTypeView> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(String contentCode, String id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    return FieldTypeView.getAll().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }
}
