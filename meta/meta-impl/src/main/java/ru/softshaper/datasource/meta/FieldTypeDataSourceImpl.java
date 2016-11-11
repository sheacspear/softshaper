package ru.softshaper.datasource.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.comparators.ObjectComparator;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.staticcontent.meta.conditions.DefaultConditionChecker;
import ru.softshaper.staticcontent.meta.meta.FieldTypeStaticContent;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 21.08.2016.
 */
@Component
@ThreadSafe
@Qualifier("fieldType")
public class FieldTypeDataSourceImpl extends AbstractCustomDataSource<FieldType> {

  @Autowired
  public FieldTypeDataSourceImpl(@Qualifier(FieldTypeStaticContent.META_CLASS) ObjectExtractor<FieldType> objectExtractor) {
    super(objectExtractor);
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  protected CheckConditionVisitor newCheckCondition(FieldType object) {
    return new DefaultConditionChecker<>(object, getObjectExtractor());
  }

  @Override
  protected Collection<FieldType> getAllObjects(GetObjectsParams params) {
    return FieldType.getFieldsTypes();
  }

  @Override
  @Cacheable("fieldObj")
  public FieldType getObj(GetObjectsParams params) {
    return super.getObj(params);
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
    return super.getObjects(params);
  }
}
