package ru.softshaper.services.meta.impl.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.services.meta.staticcontent.meta.FieldTypeStaticContent;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 21.08.2016.
 */
@Component
@ThreadSafe
@Qualifier("fieldType")
public class FieldTypeDataSourceImpl implements ContentDataSource<FieldType> {

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }

  @Override
  @Cacheable("fieldObj")
  public FieldType getObj(GetObjectsParams params) {
    Collection<FieldType> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  @CacheEvict(cacheNames = {"fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  @CacheEvict(cacheNames = {"fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  public void updateObject(String contentCode, String id, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  @CacheEvict(cacheNames = {"fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  public void deleteObject(String contentCode, String id) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Cacheable("fieldObjCnt")
  public Integer getCntObjList(String contentCode) {
    return FieldType.getFieldsTypes().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return Long.class;
  }

  @Override
  @Cacheable("fieldObjList")
  public Collection<FieldType> getObjects(GetObjectsParams params) {
    Collection<FieldType> fieldsTypes = FieldType.getFieldsTypes();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      fieldsTypes = fieldsTypes.stream()
          .filter(fieldType -> params.getIds().contains(fieldType.getId().toString()))
          .collect(Collectors.toSet());
    }
    if (params.getCondition() != null) {
      throw new RuntimeException("Not implemented yet!");
    }
    if (params.getOrderFields() != null && !params.getOrderFields().isEmpty()) {
      List<FieldType> fieldTypeList = new ArrayList<>(fieldsTypes);
      LinkedHashMap<MetaField, SortOrder> orderFields = params.getOrderFields();
      orderFields.forEach((metaField, sortOrder)
          -> Collections.sort(fieldTypeList, (fieldType1, fieldType2) -> {
                      if (FieldTypeStaticContent.Field.code.equals(metaField.getCode())) {
                        return fieldType1.getCode().compareTo(fieldType2.getCode());
                      } else if (FieldTypeStaticContent.Field.name.equals(metaField.getCode())) {
                        return fieldType1.getName().compareTo(fieldType2.getName());
                      }
                      throw new RuntimeException("Unknown sort field " + metaField);
                    }));
      fieldsTypes = fieldTypeList;
    }
    if (params.getLimit() < Integer.MAX_VALUE || params.getOffset() > 0) {
      fieldsTypes = fieldsTypes.stream()
          .skip(params.getOffset())
          .limit(params.getLimit())
          .collect(Collectors.toList());
    }
    return fieldsTypes;
  }
}
