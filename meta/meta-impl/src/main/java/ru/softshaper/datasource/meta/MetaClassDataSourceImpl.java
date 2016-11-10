package ru.softshaper.datasource.meta;

import com.google.common.base.Preconditions;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.softshaper.bean.meta.MetaClassMutableBean;
import ru.softshaper.services.meta.*;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.services.security.ContentSecurityManager;
import ru.softshaper.staticcontent.meta.conditions.MetaClassConditionChecker;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.storage.jooq.tables.FieldView;
import ru.softshaper.storage.jooq.tables.daos.DynamicContentDao;
import ru.softshaper.storage.jooq.tables.daos.DynamicFieldDao;
import ru.softshaper.storage.jooq.tables.records.DynamicContentRecord;

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
@Qualifier("metaClass")
public class MetaClassDataSourceImpl implements ContentDataSource<MetaClass> {

  /**
   * DSLContext
   */
  private final DSLContext dslContext;

  /**
   * DynamicContentDao
   */
  private final DynamicContentDao dynamicContentRepository;
  /**
   * DynamicFieldDao
   */
  private final DynamicFieldDao dynamicFieldDao;
  /**
   * MetaInitializer
   */
  private MetaInitializer metaInitializer;

  /**
   * MetaStorage
   */
  private final MetaStorage metaStorage;

  /**
   * ContentSecurityManager
   */
  private final ContentSecurityManager dynamicContentSecurityManager;

  /**
   * @param dslContext
   * @param dynamicContentRepository
   * @param metaStorage
   * @param dynamicFieldDao
   * @param dynamicContentSecurityManager
   */
  @Autowired
  public MetaClassDataSourceImpl(DSLContext dslContext, DynamicContentDao dynamicContentRepository, MetaStorage metaStorage, DynamicFieldDao dynamicFieldDao,
      ContentSecurityManager dynamicContentSecurityManager) {
    this.dslContext = dslContext;
    this.dynamicContentRepository = dynamicContentRepository;
    this.metaStorage = metaStorage;
    this.dynamicFieldDao = dynamicFieldDao;
    this.dynamicContentSecurityManager = dynamicContentSecurityManager;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#setMetaInitializer(ru.softshaper.services.meta. MetaInitializer)
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
  @CacheEvict(cacheNames = { "metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt" }, allEntries = true)
  public String createObject(String contentCode, Map<String, Object> values) {
    MetaClassMutableBean metaClass = constructMetaClass(null, values);
    return create(metaClass);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#updateObject(java.lang.String, java.lang.String, java.util.Map)
   */
  @Override
  @CacheEvict(cacheNames = { "metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt" }, allEntries = true)
  public void updateObject(String contentCode, String id, Map<String, Object> values) {
    update(constructMetaClass(id, values));
  }

  /**
   * @param id
   * @param values
   * @return
   */
  private MetaClassMutableBean constructMetaClass(String id, Map<String, Object> values) {
    String code = (String) values.get(MetaClassStaticContent.Field.code);
    String name = (String) values.get(MetaClassStaticContent.Field.name);
    String table = (String) values.get(MetaClassStaticContent.Field.table);
    table = "dc_" + table;
    Boolean checkSecurity = (Boolean) values.get(MetaClassStaticContent.Field.checkSecurity);
    Boolean checkObjectSecurity = (Boolean) values.get(MetaClassStaticContent.Field.checkObjectSecurity);
    return new MetaClassMutableBean(id, code, name, table, null, checkSecurity != null ? checkSecurity : false, checkObjectSecurity != null ? checkObjectSecurity : false);
  }

  /**
   * @param metaClass
   * @return
   */
  @Transactional
  private String create(MetaClass metaClass) {
    DynamicContentRecord dc = new DynamicContentRecord();
    dc.setName(metaClass.getName());
    dc.setCode(metaClass.getCode());
    String table = metaClass.getTable();

    dc.setTableContent(table);
    dc.setChecksecurity(metaClass.isCheckSecurity());
    dc.setCheckobjectsecurity(metaClass.isCheckObjectSecurity());
    InsertSetStep<DynamicContentRecord> recordInsertSetStep = dslContext.insertInto(dynamicContentRepository.getTable());
    InsertSetMoreStep<DynamicContentRecord> insertSetMoreStep = recordInsertSetStep.set(dc);
    final String idColumn = "id";
    final Field<Object> idFields = DSL.field(idColumn);
    InsertResultStep<DynamicContentRecord> insertResultStep = insertSetMoreStep.returning(idFields);
    Result<DynamicContentRecord> result = insertResultStep.fetch();
    String objectId = result.getValue(0, idColumn).toString();

    dslContext.createSequence(metaClass.getIdSequence()).execute();
    CreateTableAsStep<Record> createTableAsStep = dslContext.createTable(table);
    DataType<?> dataType = SQLDataType.BIGINT;
    dataType = dataType.nullable(false);
    // todo: nextval as default value
    CreateTableColumnStep tableColumnStep = createTableAsStep.column(metaClass.getIdColumn(), dataType);
    tableColumnStep.constraint(DSL.constraint(metaClass.getPrimaryKey()).primaryKey(metaClass.getIdColumn()));

    tableColumnStep.execute();

    if (metaClass.isCheckObjectSecurity()) {
      createAclTable(table);
    }
    metaInitializer.init();
    return objectId;
  }

	/**
	 * Изменение динамического контента
	 *
	 * @param content
	 */
	@Transactional

	private void update(MetaClass content) {
		MetaClass oldContent = metaStorage.getMetaClassById(content.getId());
		if (oldContent == null) {
			throw new RuntimeException(
					"Нельзя обновить, т.к. контента с идентификатором " + content.getId() + " не существует");
		}
		UpdateSetFirstStep<DynamicContentRecord> recordUpdateSetFirstStep = dslContext
				.update(dynamicContentRepository.getTable());
		UpdateSetMoreStep<DynamicContentRecord> updateSetMoreStep = null;
		if (!isFieldEquals(content.getName(), oldContent.getName())) {
			updateSetMoreStep = recordUpdateSetFirstStep
					.set(DSL.field(ru.softshaper.storage.jooq.tables.DynamicContent.DYNAMIC_CONTENT.NAME), content.getName());
		}
		if (!isFieldEquals(content.isCheckObjectSecurity(), oldContent.isCheckObjectSecurity())) {
			Field<Boolean> field = DSL.field(ru.softshaper.storage.jooq.tables.DynamicContent.DYNAMIC_CONTENT.CHECKOBJECTSECURITY);
			if (updateSetMoreStep == null) {
				updateSetMoreStep = recordUpdateSetFirstStep.set(field, content.isCheckObjectSecurity());
			} else {
				updateSetMoreStep.set(field, content.isCheckObjectSecurity());
			}
			if (content.isCheckObjectSecurity()) {
				createAclTable(content.getTable());
			} else {
				deleteAclTable(content);
			}
		}
		if (!isFieldEquals(content.isCheckSecurity(), oldContent.isCheckSecurity())) {
			Field<Boolean> field = DSL.field(ru.softshaper.storage.jooq.tables.DynamicContent.DYNAMIC_CONTENT.CHECKSECURITY);
			if (updateSetMoreStep == null) {
				updateSetMoreStep = recordUpdateSetFirstStep.set(field, content.isCheckSecurity());
			} else {
				updateSetMoreStep.set(field, content.isCheckSecurity());
			}
		}
		if (!isFieldEquals(content.getName(), oldContent.getName())) {
			updateSetMoreStep = recordUpdateSetFirstStep
					.set(DSL.field(ru.softshaper.storage.jooq.tables.DynamicContent.DYNAMIC_CONTENT.NAME), content.getName());
		}
		if (updateSetMoreStep != null) {
			updateSetMoreStep
					.where(DSL.field(ru.softshaper.storage.jooq.tables.DynamicContent.DYNAMIC_CONTENT.ID).eq(Long.valueOf(content.getId())))
					.execute();
		}
		metaInitializer.init();
	}

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#deleteObject(java.lang.String, java.lang.String)
   */
  @Override
  @Transactional
  @CacheEvict(cacheNames = { "metaObjList", "metaObj", "metaObjListCond", "metaObjCnt", "fieldObjList", "fieldObj", "fieldObjListCond", "fieldObjCnt" }, allEntries = true)
  public void deleteObject(String contentCode, String id) {
    MetaClass content = metaStorage.getMetaClassById(id);
    Preconditions.checkNotNull(content);
    // удаляем поля
    if (content.getFields() != null) {
      content.getFields().forEach(field -> deleteField(content, field));
    }
    // удаляем запись о контенте
    dynamicContentRepository.deleteById(Long.valueOf(id));
    // удаляем саму табличку контента
    dslContext.dropTableIfExists(content.getTable()).execute();
    // удаляем сиквенс идентификатора
    dslContext.dropSequenceIfExists(content.getIdSequence()).execute();
    // удаляем привязку прав к этому контенту
    // todo: что то я сильно не уверен что безопасность должна напрямую тут
    // удаляться
    dynamicContentSecurityManager.deleteAllRight(content);
    // удаляем таблицу acl
    deleteAclTable(content);
    // todo: delete linked data
    metaInitializer.init();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not implemented yet!");
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSource#getCntObjList(java.lang.String)
   */
  @Override
  @Cacheable("metaObjCnt")
  public Integer getCntObjList(String contentCode) {
    return metaStorage.getAllMetaClasses().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return Long.class;
  }

  /**
   * @param content
   * @param field
   */
  private void deleteField(MetaClass content, MetaField field) {
    dynamicFieldDao.deleteById(Long.valueOf(field.getId()));
    // todo: тут проблемка в том, что представление не совсем является частью
    // меты,
    // todo: т.ч. и удаление должно вызываться событием, а никак не напрямую
    dslContext.delete(FieldView.FIELD_VIEW.asTable()).where(FieldView.FIELD_VIEW.COLUMN_CONTENT.eq(field.getCode())).and(FieldView.FIELD_VIEW.TABLE_CONTENT.eq(content.getCode()))
        .execute();
  }

  /**
   * Создание таблицы acl
   *
   * @param tableName таблица, для которой создаётся таблица acl
   */
  public void createAclTable(String tableName) {
    DataType<?> dataType;
    CreateTableColumnStep tableColumnStep;
    CreateTableAsStep<Record> aclTableCreateStep = dslContext.createTableIfNotExists(tableName + "_acl");
    dataType = SQLDataType.BIGINT;
    dataType = dataType.nullable(false);
    dslContext.createSequenceIfNotExists("s_" + tableName + "_acl_id").execute();
    tableColumnStep = aclTableCreateStep.column("id", dataType);
    tableColumnStep.constraint(DSL.constraint("pk_" + tableName + "_acl_id").primaryKey("id"));
    tableColumnStep.column("object_id", SQLDataType.BIGINT).column("role", SQLDataType.VARCHAR).column("can_create", SQLDataType.BOOLEAN).column("can_read", SQLDataType.BOOLEAN)
        .column("can_update", SQLDataType.BOOLEAN).column("can_delete", SQLDataType.BOOLEAN).execute();
  }

  /**
   * @param content
   */
  private void deleteAclTable(MetaClass content) {
    dslContext.dropTableIfExists(DSL.table(content.getTable() + "_acl")).execute();
    dslContext.dropSequenceIfExists("s_" + content.getTable() + "_acl_id");
  }

  /**
   * Compare two objects.
   *
   * @param obj1 obj1
   * @param obj2 obj2
   * @param <T> type of comparing objects
   * @return True if equals. null == null
   */
  private <T> boolean isFieldEquals(T obj1, T obj2) {
    return obj1 == null && obj2 == null || obj1 == obj2 || obj1 != null && obj1.equals(obj2);
  }

  @Override
  public MetaClass getObj(GetObjectsParams params) {
    Collection<MetaClass> objects = getObjects(params);
    return objects == null ? null : objects.iterator().next();
  }

  @Override
  @Cacheable("metaObjList")
  public Collection<MetaClass> getObjects(GetObjectsParams params) {
    Collection<MetaClass> metaClasses = metaStorage.getAllMetaClasses();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      metaClasses = metaClasses.stream()
          .filter(metaClass -> params.getIds().contains(metaClass.getId()))
          .collect(Collectors.toSet());
    }
    LinkedHashMap<MetaField, ru.softshaper.services.meta.impl.SortOrder> orderFields = params.getOrderFields();
    Stream<MetaClass> stream = metaClasses.stream();
    ru.softshaper.services.meta.conditions.Condition condition = params.getCondition();
    if (condition != null) {
      stream = stream.filter(metaClass -> condition.check(new MetaClassConditionChecker(metaClass)));
    }
    if (orderFields != null) {
      stream = stream.sorted((o1, o2) -> {
        int compareResult = 0;
        //todo: something
        for (Map.Entry<MetaField, SortOrder> order : orderFields.entrySet()) {
          switch (order.getKey().getCode()) {
            case MetaClassStaticContent.Field.code:
              compareResult = o1.getCode().compareTo(o2.getCode());
              break;
            case MetaClassStaticContent.Field.name:
              compareResult = o1.getName().compareTo(o2.getName());
              break;
            case MetaClassStaticContent.Field.checkObjectSecurity:
              compareResult = o1.isCheckObjectSecurity() == o2.isCheckObjectSecurity() ? 0 : o1.isCheckObjectSecurity() ? 1 : -1;
              break;
            case MetaClassStaticContent.Field.checkSecurity:
              compareResult = o1.isCheckSecurity() == o2.isCheckSecurity() ? 0 : o1.isCheckSecurity() ? 1 : -1;
              break;
            case MetaClassStaticContent.Field.fixed:
              compareResult = o1.isFixed() == o2.isFixed() ? 0 : o1.isFixed() ? 1 : -1;
              break;
            case MetaClassStaticContent.Field.table:
              compareResult = o1.getTable().compareTo(o2.getTable());
              break;
          }
          if (compareResult != 0) {
            compareResult = SortOrder.DESC.equals(order.getValue()) ? compareResult * -1 : compareResult;
            break;
          }
        }
        return compareResult;
      });
    }
    return stream.skip(params.getOffset())
      .limit(params.getLimit())
      .collect(Collectors.toList());
  }
}
