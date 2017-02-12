package ru.softshaper.datasource.meta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.concurrent.ThreadSafe;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertResultStep;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.TableImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.conditions.ConvertConditionVisitor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.services.security.ContentSecurityManager;

//import org.jooq.util.Database;

/**
 * Dao для работы с динамическими объектами
 */
@Component
@ThreadSafe
@Qualifier("data")
public class ObjectDataSourceImpl implements ContentDataSource<Record>, POJOContentDataSource {

  private static final Logger log = LoggerFactory.getLogger(ObjectDataSourceImpl.class);

  /**
   * MetaStorage
   */
  @Autowired
  private MetaStorage metaStorage;

  /**
   * DSLContext
   */
  @Autowired
  private DSLContext dsl;

  /**
   * Менеджер безопасности
   */
  @Autowired
  private ContentSecurityManager securityManager;

  @Autowired
  private DefaultConfiguration defaultConfiguration;

  @Autowired
  @Qualifier("fileObjectDataSource")
  private FileObjectDataSource fileDataSource;

  @Autowired
  @Qualifier("jooq")
  private ConvertConditionVisitor<Condition> conditionConverter;

  @Autowired
  private DataSourceStorage dataSourceStorage;

  @Autowired
  private FieldConverter fieldConverter;

  // @Override
  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.services.meta.impl.datasource.Test#getObjects(org.jooq.impl.
   * TableImpl, java.lang.Class, java.util.Collection)
   */
  @Override
  @SuppressWarnings("unchecked")
  public <R extends Record, E> Collection<E> getObjects(TableImpl<R> table, Class<? extends E> type, Collection<String> ids) {
    RecordMapperProvider recordMapperProvider = defaultConfiguration.recordMapperProvider();
    RecordMapper<R, E> recordMapper = recordMapperProvider.provide(table.recordType(), type);
    MetaClass metaClass = metaStorage.getMetaClassByTable(table.getName());
    GetObjectsParams params = GetObjectsParams.newBuilder(metaClass).addIds(ids).build();
    Collection<Record> ress = getObjects(params);
    Collection<E> result = new ArrayList<E>();
    for (Record res : ress) {
      result.add(recordMapper.map((R) res));
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.services.meta.DataSource#setMetaInitializer(ru.softshaper.
   * services.meta.MetaInitializer)
   */
  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#getCntObjList(java.lang.String)
   */
  @Override
  public Integer getCntObjList(String contentCode) {
    MetaClass content = getMetaClass(contentCode);
    if (content.isCheckSecurity() && !securityManager.canRead(content.getId())) {
      log.error("Access denied for object of content " + content.getCode());
      throw new RuntimeException("Access denied for object of content " + content.getCode());
    }
    Table<Record> table = DSL.table(content.getTable());
    SelectJoinStep<Record1<Integer>> queryBuilder = dsl.select(DSL.count()).from(table);
    if (content.isCheckObjectSecurity()) {
      queryBuilder = addChekSecFilter(content, queryBuilder);
    }
    return queryBuilder.fetchOne(0, int.class);
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return Long.class;
  }

  /**
   * Добавление фильтра безопасности
   *
   * @param content мета класс
   * @param queryBuilder билдер запроса
   * @return билдер запроса
   */
  private <T extends Record> SelectJoinStep<T> addChekSecFilter(MetaClass content, SelectJoinStep<T> queryBuilder) {
    Collection<String> currentUserRoles = securityManager.getCurrentUserRoles();
    if (currentUserRoles == null || currentUserRoles.isEmpty()) {
      throw new RuntimeException("User have not roles");
    }
    Table<Record> aclTable = DSL.table(content.getTable() + "_acl");
    Field<Object> contentIdField = DSL.field(content.getTable() + "." + content.getId());
    Field<Object> aclIdField = DSL.field(aclTable.getName() + ".id");
    Field<Object> aclRoleField = DSL.field(aclTable.getName() + ".role");
    Field<Object> aclReadField = DSL.field(aclTable.getName() + ".can_read");
    queryBuilder = queryBuilder.innerJoin(content.getTable() + "_acl").on(contentIdField.eq(aclIdField));
    Condition where = null;
    for (String role : currentUserRoles) {
      if (where == null) {
        where = aclRoleField.eq(role);
      } else {
        where = where.or(aclRoleField.eq(role));
      }
    }
    where = where.and(aclReadField.eq(Boolean.TRUE));
    queryBuilder.where(where);
    return queryBuilder;
  }

  @Override
  public Collection<Record> getObjects(final GetObjectsParams params) {
    Preconditions.checkNotNull(params);
    MetaClass metaClass = params.getMetaClass();
    if (metaClass.isCheckSecurity() && !securityManager.canRead(metaClass.getId())) {
      log.error("Access denied for object of content " + metaClass.getCode());
      throw new RuntimeException("Access denied for object of content " + metaClass.getCode());
    }
    Table<Record> table = DSL.table(metaClass.getTable());
    SelectJoinStep<Record> queryBuilder = dsl.select().from(table);
    Condition where = null;
    if (metaClass.isCheckObjectSecurity()) {
      Collection<String> currentUserRoles = securityManager.getCurrentUserRoles();
      if (currentUserRoles == null || currentUserRoles.isEmpty()) {
        return Collections.emptyList();
      }

      Table<Record> aclTable = DSL.table(metaClass.getTable() + "_acl");
      Field<Object> contentIdField = DSL.field(metaClass.getTable() + "." + metaClass.getId());
      Field<Object> aclIdField = DSL.field(aclTable.getName() + ".id");
      Field<Object> aclRoleField = DSL.field(aclTable.getName() + ".role");
      Field<Object> aclReadField = DSL.field(aclTable.getName() + ".can_read");
      queryBuilder = queryBuilder.innerJoin(metaClass.getTable() + "_acl").on(contentIdField.eq(aclIdField));
      for (String role : currentUserRoles) {
        if (where == null) {
          where = aclRoleField.eq(role);
        } else {
          where = where.or(aclRoleField.eq(role));
        }
      }
      where = where.and(aclReadField.eq(Boolean.TRUE));
    }

    if (params.getCondition() != null) {
      Condition condition = params.getCondition().convert(conditionConverter);
      where = where == null ? condition : where.and(condition);
    }
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      List<Long> ids = params.getIds().stream().map(Long::valueOf).collect(Collectors.toList());
      Condition condition = DSL.field(params.getMetaClass().getIdColumn()).in(ids);
      where = where == null ? condition : condition.and(where);
    }
    if (where != null) {
      queryBuilder.where(where);
    }
    if (params.getOrderFields() != null) {
      List<SortField<Object>> sortFields = params.getOrderFields().entrySet().stream().map(orderField -> {
        Field<Object> field = DSL.field(orderField.getKey().getColumn());
        return orderField.getValue().equals(SortOrder.ASC) ? field.asc() : field.desc();
      }).collect(Collectors.toList());
      queryBuilder.orderBy(sortFields);
    }
    queryBuilder.offset(params.getOffset());
    queryBuilder.limit(params.getLimit());
    return queryBuilder.fetch();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    MetaClass metaClass = getMetaClass(contentCode);
    MetaField mxNField = metaClass.getField(multyfieldCode);
    Field<Long> fromIdField;
    Field<Long> toIdField;
    if (reverse) {
      toIdField = DSL.field("from_id", Long.class);
      fromIdField = DSL.field("to_id", Long.class);
    } else {
      fromIdField = DSL.field("from_id", Long.class);
      toIdField = DSL.field("to_id", Long.class);

    }
    List<Long> fetch = dsl.select(toIdField).from(mxNField.getNxMTableName()).where(fromIdField.equal(Long.valueOf(id))).fetch(toIdField);
    return fetch == null ? null : fetch.stream().map(Object::toString).collect(Collectors.toList());
  }

  @Override
  public Record getObj(GetObjectsParams params) {
    Collection<Record> records = getObjects(params);
    return records == null || records.isEmpty() ? null : records.iterator().next();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.services.dynamiccontent.ObjectDynamicContentDAO#createObject(
   * java .util.Map)
   */
  @Transactional
  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    Preconditions.checkNotNull(values);
    Preconditions.checkArgument(!values.isEmpty());
    MetaClass metaClass = getMetaClass(contentCode);
    if (metaClass.isCheckSecurity() && !securityManager.canCreate(metaClass.getId())) {
      log.error("Access denied for object of metaClass " + metaClass.getCode());
      throw new RuntimeException("Access denied for object of metaClass " + metaClass.getCode());
    }
    Map<Field<?>, Object> fieldValues = Maps.newHashMap();
    Map<MetaField, Object> valuesMap = constructValuesMap(values, metaClass);
    valuesMap.forEach((MetaField field, Object value) -> {
      if (value != null) {
        fieldValues.put(DSL.field(field.getColumn(), value.getClass()), value);
      }
    });
    Table<Record> table = DSL.table(metaClass.getTable());
    InsertSetStep<Record> recordInsertSetStep = dsl.insertInto(table);
    InsertSetMoreStep<Record> insertSetMoreStep = recordInsertSetStep.set(fieldValues);
    Field<BigInteger> filed = DSL.sequence(metaClass.getIdSequence()).nextval();
    final Field<Object> idFields = DSL.field(metaClass.getIdColumn());
    insertSetMoreStep.set(idFields, filed);
    InsertResultStep<Record> insertResultStep = insertSetMoreStep.returning(idFields);
    Result<Record> result = insertResultStep.fetch();
    return result.getValue(0, idFields).toString();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.services.dynamiccontent.ObjectDynamicContentDAO#updateObject(
   * java .lang.String, java.util.Map)
   */
  @Transactional
  @Override
  public void updateObject(String contentCode, String id, Map<String, Object> values) {
    Preconditions.checkNotNull(contentCode);
    Preconditions.checkNotNull(values);
    Preconditions.checkArgument(!values.isEmpty());
    MetaClass metaClass = getMetaClass(contentCode);
    if (metaClass.isCheckSecurity() && !securityManager.canUpdate(metaClass.getId())) {
      throw new RuntimeException("Access denied for object of metaClass " + metaClass.getCode());
    }
    Map<Field<?>, Object> fieldValues = Maps.newHashMap();
    final Map<MetaField, Object> valuesMap = constructValuesMap(values, metaClass);
    for (Entry<MetaField, Object> entry : valuesMap.entrySet()) {
      MetaField field = entry.getKey();
      Object value = entry.getValue();
      if (value != null) {
        fieldValues.put(DSL.field(field.getColumn()), value);
      } else {
        // todo: вообщем то работает, но какой то лайвхак
        fieldValues.put(DSL.field(field.getColumn()), DSL.castNull(Long.class));
      }
    }
    Table<Record> table = DSL.table(metaClass.getTable());
    dsl.update(table).set(fieldValues).where(DSL.field(metaClass.getIdColumn()).equal(Long.valueOf(id))).execute();
  }

  private Map<MetaField, Object> constructValuesMap(Map<String, Object> values, MetaClass metaClass) {
    final Map<MetaField, Object> valuesMap = new HashMap<>();
    values.forEach((fieldCode, value) -> {
      MetaField metaField = metaClass.getField(fieldCode);
      valuesMap.put(metaField, fieldConverter.convert(metaField, value == null ? null : value.toString()));
    });
    return valuesMap;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.services.dynamiccontent.ObjectDynamicContentDAO#deleteObject(
   * java .lang.String)
   */
  @Transactional
  @Override
  public void deleteObject(String contentCode, String id) {
    MetaClass content = getMetaClass(contentCode);
    Table<Record> table = DSL.table(content.getTable());
    dsl.delete(table).where(DSL.field(content.getIdColumn()).equal(Long.valueOf(id))).execute();
  }

  private MetaClass getMetaClass(String contentCode) {
    Preconditions.checkNotNull(contentCode);
    MetaClass content = metaStorage.getMetaClass(contentCode);
    Preconditions.checkNotNull(content);
    return content;
  }

  @Override
  public ObjectExtractor<Record> getObjectExtractor() {
    return new ObjectMetaExtractor();
  }

  public static class ObjectMetaExtractor implements ObjectExtractor<Record> {

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(Record obj, MetaClass metaClass) {
      return obj.get(metaClass.getIdColumn(), Long.class).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.softshaper.services.meta.ObjectExtractor#getValue(java.lang.Object,
     * ru.softshaper.services.meta.MetaField)
     */
    @Override
    public Object getValue(Record obj, MetaField field) {
      return obj.get(field.getColumn());
    }
  }

}
