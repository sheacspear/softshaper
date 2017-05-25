package ru.softshaper.bean.meta;

import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaClassMutable;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaFieldMutable;
import ru.softshaper.storage.jooq.tables.records.DynamicFieldRecord;

public class FieldBuilder<T extends Record, K> implements Cloneable {

  private MetaClassMutable metaClass;
  private Map<String, MetaClassMutable> clazzCode;
  private String code;
  private String name;
  private final TableField<T, K> column;
  private String linkToMetaClass;
  private String backReferenceField;
  private String nxMTableName;

  private final FieldType fieldType;

  private DSLContext dslContext;

  public FieldBuilder(TableField<T, K> column,FieldType fieldType) {
    super();
    this.column = column;
    this.fieldType = fieldType;
    this.code = column != null ? column.getName() : null;
    this.name = column != null ? column.getName() : null;
  }

  /**
   * @param metaClass the metaClass to set
   */
  public FieldBuilder<T, K> setMetaClass(MetaClassMutable metaClass) {
    this.metaClass = metaClass;
    return this;
  }

  /**
   * @param clazzCode the clazzCode to set
   */
  public FieldBuilder<T, K> setClazzCode(Map<String, MetaClassMutable> clazzCode) {
    this.clazzCode = clazzCode;
    return this;
  }

  /**
   * @param code the code to set
   */
  public FieldBuilder<T, K> setCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * @param name the name to set
   */
  public FieldBuilder<T, K> setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @param linkToMetaClass the linkToMetaClass to set
   */
  public FieldBuilder<T, K> setLinkToMetaClass(String linkToMetaClass) {
    this.linkToMetaClass = linkToMetaClass;
    return this;
  }

  /**
   * @param backReferenceField the backReferenceField to set
   */
  public FieldBuilder<T, K> setBackReferenceField(String backReferenceField) {
    this.backReferenceField = backReferenceField;
    return this;
  }

  /**
   * @param nxMTableName the nxMTableName to set
   */
  public FieldBuilder<T, K> setNxMTableName(String nxMTableName) {
    this.nxMTableName = nxMTableName;
    return this;
  }

  /**
   * @param dslContext the dslContext to set
   */
  public FieldBuilder<T, K> setDslContext(DSLContext dslContext) {
    this.dslContext = dslContext;
    return this;
  }

  /**
   * @return
   */
  public MetaFieldMutable build() {
    String columnName = column == null ? null : column.getName();
    ru.softshaper.storage.jooq.tables.DynamicField dfTable = ru.softshaper.storage.jooq.tables.DynamicField.DYNAMIC_FIELD;
    Long longMetaClassId = Long.valueOf(metaClass.getId());
    Long id = dslContext.select(dfTable.ID).from(dfTable).where(dfTable.DYNAMIC_CONTENT_ID.equal(longMetaClassId).and(dfTable.CODE.equal(code))).fetchOne(dfTable.ID);
    if (id == null) {
      DynamicFieldRecord dynamicFieldRecord = dslContext.insertInto(dfTable).columns(dfTable.CODE, dfTable.DYNAMIC_CONTENT_ID).values(code, longMetaClassId).returning(dfTable.ID)
          .fetchOne();
      id = dynamicFieldRecord.getValue(dfTable.ID);
    }
    MetaClass linkedClass = linkToMetaClass == null ? null : clazzCode.get(linkToMetaClass);
    MetaField backlinkedField = backReferenceField == null || linkedClass == null ? null : linkedClass.getField(backReferenceField);
    return new MetaFieldMutableBean(id.toString(), metaClass, name, code, columnName, FieldType.getFieldType(fieldType.getId()), linkedClass, backlinkedField, nxMTableName);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FieldBuilder [metaClass=" + metaClass + ", clazzCode=" + clazzCode + ", code=" + code + ", name=" + name + ", column=" + column + ", linkToMetaClass=" + linkToMetaClass
        + ", backReferenceField=" + backReferenceField + ", nxMTableName=" + nxMTableName + "]";
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#clone()
   */
  @Override
  public Object clone() {
    return new FieldBuilder<T, K>(column,fieldType).setCode(code).setBackReferenceField(backReferenceField).setLinkToMetaClass(linkToMetaClass).setName(name)
        .setNxMTableName(nxMTableName).setDslContext(dslContext);
  }
}