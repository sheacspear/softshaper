package ru.softshaper.datasource.meta;

import com.google.common.base.Preconditions;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.softshaper.bean.meta.MetaFieldBean;
import ru.softshaper.services.meta.*;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.jooq.JooqFieldFactory;
import ru.softshaper.staticcontent.meta.conditions.MetaFieldConditionChecker;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;
import ru.softshaper.storage.jooq.tables.FieldView;
import ru.softshaper.storage.jooq.tables.daos.DynamicFieldDao;
import ru.softshaper.storage.jooq.tables.records.DynamicFieldRecord;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sunchise on 16.08.2016.
 */
@Component
@ThreadSafe
@Qualifier("metaField")
public class MetaFieldDataSourceImpl implements ContentDataSource<MetaField> {

  /**
   * DSLContext
   */
  private final DSLContext dslContext;

  /**
   * DynamicFieldDao
   */
  private final DynamicFieldDao dynamicFieldDao;

  /**
   * MetaInitializer
   */
  private MetaInitializer metaInitializer;

  private JooqFieldFactory jooqFieldFactory = JooqFieldFactory.getInstance();

  /**
   * MetaStorage
   */
  private final MetaStorage metaStorage;

  /**
   * @param dslContext
   * @param dynamicFieldDao
   * @param metaStorage
   */
  @Autowired
  public MetaFieldDataSourceImpl(DSLContext dslContext, DynamicFieldDao dynamicFieldDao, MetaStorage metaStorage) {
    this.dslContext = dslContext;
    this.dynamicFieldDao = dynamicFieldDao;
    this.metaStorage = metaStorage;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#setMetaInitializer(ru.softshaper.services.meta.MetaInitializer)
   */
  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {
    this.metaInitializer = metaInitializer;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#createObject(java.lang.String, java.util.Map)
   */
  @Override
  @CacheEvict(cacheNames = {"metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  @Transactional
  public String createObject(String contentCode, Map<String, Object> values) {
    MetaField field = constructMetaField(null, values);
    return createField(field.getOwner().getId(), field);
  }

  /**
   * Конструирование объекта
   *
   * @param id     ид
   * @param values параметры
   * @return MetaField
   */
  private MetaField constructMetaField(String id, Map<String, Object> values) {
    String ownerStr = (String) values.get("owner");
    String objectId = String.valueOf(ownerStr);
    MetaClass owner = metaStorage.getMetaClassById(objectId);
    String code = (String) values.get("code");
    String name = (String) values.get("name");
    String column = (String) values.get("column");
    Long type = Long.valueOf(values.get("type").toString());
    String linkToMetaClassStr = (String) values.get("linkToMetaClass");
    String backReferenceFieldStr = (String) values.get("backReferenceField");
    MetaClass linkToMetaClass = null;
    MetaField backReferenceField = null;
    if (linkToMetaClassStr != null) {
      linkToMetaClass = metaStorage.getMetaClassById(String.valueOf(linkToMetaClassStr));
      if (backReferenceFieldStr != null) {
        backReferenceField = linkToMetaClass.getFields().stream().filter(field -> field.getId().equals(backReferenceFieldStr)).findFirst().orElse(null);
      }
    }
    return new MetaFieldBean(id, owner, name, code, column, FieldType.getFieldType(type), linkToMetaClass, backReferenceField, null);
  }

  /**
   * Запись строки о нового поля в таблицу
   *
   * @param metaClassId динамический контент, с которым связано это поле
   * @param field       описание поля
   */
  private String createField(String metaClassId, MetaField field) {
    DynamicFieldRecord df = new DynamicFieldRecord();
    df.setCode(field.getCode());
    df.setColumnField(field.getColumn());
    df.setDynamicContentId(Long.valueOf(metaClassId));
    df.setName(field.getName());
    df.setType1(field.getType().getId());
    df.setTypeField(field.getType().getId());
    if (field.getType().equals(FieldType.MULTILINK)) {
      createBindTable(field);
      df.setNxMTableName(constructBindTableName(field));
    }
    if (field.getLinkToMetaClass() != null) {
      df.setLinkToDynamicContent(Long.valueOf(field.getLinkToMetaClass().getId()));
    }
    if (field.getBackReferenceField() != null) {
      df.setBackReferenceField(Long.valueOf(field.getBackReferenceField().getId()));
    }
    InsertSetStep<DynamicFieldRecord> recordInsertSetStep = dslContext.insertInto(dynamicFieldDao.getTable());
    InsertSetMoreStep<DynamicFieldRecord> insertSetMoreStep = recordInsertSetStep.set(df);
    final Field<Object> idFields = DSL.field("id");
    InsertResultStep<DynamicFieldRecord> insertResultStep = insertSetMoreStep.returning(idFields);
    Result<DynamicFieldRecord> result = insertResultStep.fetch();
    String objectIdF = String.valueOf(result.getValue(0, "id"));
    if (field.getColumn() != null) {
      dslContext.alterTable(field.getOwner().getTable()).add(field.getColumn(), jooqFieldFactory.getDataType(field.getType())).execute();
    }
    metaInitializer.init();
    return objectIdF;
  }

  /**
   * @param m2nField
   * @return
   */
  private String constructBindTableName(MetaField m2nField) {
    Preconditions.checkNotNull(m2nField);
    MetaClass fromClass = m2nField.getOwner();
    Preconditions.checkNotNull(fromClass);
    MetaClass toClass = m2nField.getLinkToMetaClass();
    Preconditions.checkNotNull(toClass);
    return ("dc_bind" + fromClass.getTable() + "_for_" + m2nField.getCode()).toLowerCase();
  }

  /**
   * @param m2nField
   */
  private void createBindTable(MetaField m2nField) {
    String table = constructBindTableName(m2nField);
    dslContext.createSequence("seq_" + table + "_id").execute();
    dslContext.createTable(table).column("id", SQLDataType.BIGINT.nullable(false)).column("from_id", SQLDataType.BIGINT.nullable(false))
        .column("to_id", SQLDataType.BIGINT.nullable(false)).constraint(DSL.constraint("pkKey_" + table).primaryKey("id")).execute();
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#updateObject(java.lang.String, java.lang.String, java.util.Map)
   */
  @Override
  @Transactional
  @CacheEvict(cacheNames = {"metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  public void updateObject(String contentCode, String id, Map<String, Object> values) {
    MetaField field = constructMetaField(id, values);
    ru.softshaper.storage.jooq.tables.DynamicField fieldTable = ru.softshaper.storage.jooq.tables.DynamicField.DYNAMIC_FIELD;
    dslContext.update(fieldTable).set(fieldTable.NAME, field.getName()).where(fieldTable.ID.eq(Long.valueOf(field.getId()))).execute();


    dslContext.update(fieldTable)
        .set(fieldTable.LINK_TO_DYNAMIC_CONTENT, field.getLinkToMetaClass() != null ? Long.valueOf(field.getLinkToMetaClass().getId()) : null)
        .where(fieldTable.ID.eq(Long.valueOf(field.getId())))
        .execute();
    dslContext.update(fieldTable)
        .set(fieldTable.BACK_REFERENCE_FIELD, field.getBackReferenceField() != null ? Long.valueOf(field.getBackReferenceField().getId()) : null)
        .where(fieldTable.ID.eq(Long.valueOf(field.getId())))
        .execute();
    dslContext.update(fieldTable)
        .set(fieldTable.TYPE_FIELD, field.getType().getId())
        .where(fieldTable.ID.eq(Long.valueOf(field.getId())))
        .execute();

    if (field.getColumn() != null) {
      dslContext.alterTable(field.getOwner().getTable()).alter(field.getColumn()).set(jooqFieldFactory.getDataType(field.getType())).execute();
    }
    // TODO alter table
    metaInitializer.init();
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#deleteObject(java.lang.String, java.lang.String)
   */
  @Override
  @Transactional
  @CacheEvict(cacheNames = {"metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt"}, allEntries = true)
  public void deleteObject(String contentCode, String id) {
    ru.softshaper.storage.jooq.tables.pojos.DynamicField fieldsPojo = dynamicFieldDao.fetchOneById(Long.valueOf(id));
    MetaClass dc = metaStorage.getMetaClassById(fieldsPojo.getDynamicContentId().toString());
    MetaField field = dc.getField(fieldsPojo.getCode());
    dynamicFieldDao.deleteById(Long.valueOf(id));
    // todo: тут проблемка в том, что представление не совсем является частью
    // меты,
    // todo: т.ч. и удаление должно вызываться событием, а никак не напрямую
    dslContext.delete(FieldView.FIELD_VIEW.asTable()).where(FieldView.FIELD_VIEW.COLUMN_CONTENT.eq(field.getCode())).and(FieldView.FIELD_VIEW.TABLE_CONTENT.eq(dc.getCode()))
        .execute();
  }


  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public MetaField getObj(GetObjectsParams params) {
    Collection<MetaField> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    return metaStorage.getMetaFields().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return Long.class;
  }

  @Override
  public Collection<MetaField> getObjects(GetObjectsParams params) {
    Collection<MetaField> metaFields = metaStorage.getMetaFields();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      metaFields = metaFields.stream()
          .filter(metaField -> params.getIds().contains(metaField.getId()))
          .collect(Collectors.toSet());
    }
    LinkedHashMap<MetaField, ru.softshaper.services.meta.impl.SortOrder> orderFields = params.getOrderFields();
    Stream<MetaField> stream = metaFields.stream();
    ru.softshaper.services.meta.conditions.Condition condition = params.getCondition();
    if (condition != null) {
      stream = stream.filter(metaField -> condition.check(new MetaFieldConditionChecker(metaField)));
    }
    if (orderFields != null) {
      stream = stream.sorted((o1, o2) -> {
        int compareResult;
        if (o1 == null) {
          compareResult = -1;
        } else if (o2 == null) {
          compareResult = 1;
        } else {
          compareResult = 0;
          //todo: something
          for (Map.Entry<MetaField, ru.softshaper.services.meta.impl.SortOrder> order : orderFields.entrySet()) {
            switch (order.getKey().getCode()) {
              case MetaFieldStaticContent.Field.code:
                compareResult = o1.getCode().compareTo(o2.getCode());
                break;
              case MetaFieldStaticContent.Field.name:
                compareResult = o1.getName().compareTo(o2.getName());
                break;
              case MetaFieldStaticContent.Field.column:
                compareResult = compareStrings(o1.getColumn(), o2.getColumn());
                break;
              case MetaFieldStaticContent.Field.type:
                compareResult = o1.getType().getName().compareTo(o2.getType().getName());
                break;
              case MetaFieldStaticContent.Field.owner:
                compareResult = o1.getOwner().getName().compareTo(o2.getOwner().getName());
                break;
              case MetaFieldStaticContent.Field.linkToMetaClass:
                compareResult = o1.getLinkToMetaClass() == null ? o2.getLinkToMetaClass() == null ? 0 : -1 : o2.getLinkToMetaClass() == null ? 1 : o1.getLinkToMetaClass().getName().compareTo(o2.getLinkToMetaClass().getName());
                break;
              case MetaFieldStaticContent.Field.backReferenceField:
                compareResult = o1.getBackReferenceField() == null ?  o2.getBackReferenceField() == null ? 0 : -1 : o2.getBackReferenceField() == null ? 1 : o1.getBackReferenceField().getName().compareTo(o2.getBackReferenceField().getName());
                break;
            }

            if (compareResult != 0) {
              compareResult = ru.softshaper.services.meta.impl.SortOrder.DESC.equals(order.getValue()) ? compareResult * -1 : compareResult;
              break;
            }
          }
        }
        return compareResult;
      });
    }
    return stream.skip(params.getOffset())
      .limit(params.getLimit())
      .collect(Collectors.toList());
  }

  private int compareStrings(String s1, String s2) {
    return s1 == null ? s2 == null ? 0 : -1 : s2 == null ? 1 : s1.compareTo(s2);
  }
}
