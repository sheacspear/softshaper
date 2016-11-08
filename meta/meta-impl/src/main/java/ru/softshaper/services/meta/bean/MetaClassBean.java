package ru.softshaper.services.meta.bean;

import com.google.common.collect.Maps;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * мета класс
 *
 * @author asheknew
 *
 */
@ThreadSafe
public class MetaClassBean implements MetaClass {

  /**
   * Идентификатор
   */
  private final String id;
  /**
   * Код
   */
  private final String code;
  /**
   * Имя
   */
  private final String name;
  /**
   * Таблица
   */
  private final String table;
  /**
   * Поля
   */
  protected Collection<? extends MetaField> fields;

  /**
   * Зарегистрированные поля
   */
  protected Map<String, MetaField> fieldByCode = Maps.newHashMap();;

  /**
   * колнка идентификатор
   */
  private final String idColumn;
  /**
   * последовательность для идентификатора
   */
  private final String idSequence;
  /**
   * имя PrimaryKey
   */
  private final String primaryKey;
  /**
   * Проверять безопасность
   */
  private final boolean checkSecurity;
  /**
   * Проверять безопасность на объекты
   */
  private final boolean checkObjectSecurity;

  /**
   * фиксированный
   */
  private final boolean fixed;

  /**
   * @param id Идентификатор
   * @param code Код
   * @param name Имя
   * @param table Таблица
   * @param fields Поля
   * @param checkSecurity
   * @param checkObjectSecurity
   */
  public MetaClassBean(String id, String code, String name, String table, Collection<? extends MetaField> fields, boolean checkSecurity, boolean checkObjectSecurity) {
    this(id, code, name, table, fields, checkSecurity, checkObjectSecurity, false);
  }

  /**
   * @param id Идентификатор
   * @param code Код
   * @param name Имя
   * @param table Таблица
   * @param fields Поля
   * @param checkSecurity
   * @param checkObjectSecurity
   */
  public MetaClassBean(String id, String code, String name, String table, Collection<? extends MetaField> fields, boolean checkSecurity, boolean checkObjectSecurity, boolean fixed) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
    this.table = table;
    this.fields = fields == null ? Collections.emptyList() : Collections.unmodifiableCollection(new ArrayList<>(fields));
    this.fields.forEach(field -> fieldByCode.put(field.getCode(), field));
    this.idColumn = "id";
    this.idSequence = "seq_" + table + "_id";
    this.primaryKey = "pkKey_" + table;
    this.checkSecurity = checkSecurity;
    this.checkObjectSecurity = checkObjectSecurity;
    this.fixed = fixed;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getCode()
   */
  @Override
  public String getCode() {
    return code;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getTable()
   */
  @Override
  public String getTable() {
    return table;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getFields()
   */
  @Override
  public Collection<? extends MetaField> getFields() {
    return fields;
  }

  @Override
  public MetaField getField(String fieldCode) {
    return fieldByCode.get(fieldCode);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getIdColumn()
   */
  @Override
  public String getIdColumn() {
    return idColumn;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getIdSequence()
   */
  @Override
  public String getIdSequence() {
    return idSequence;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#getPrimaryKey()
   */
  @Override
  public String getPrimaryKey() {
    return primaryKey;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#isCheckSecurity()
   */
  @Override
  public boolean isCheckSecurity() {
    return checkSecurity;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#isCheckObjectSecurity()
   */
  @Override
  public boolean isCheckObjectSecurity() {
    return checkObjectSecurity;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClass#isFixed()
   */
  @Override
  public boolean isFixed() {
    return fixed;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + id + ") " + code + " - " + name;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MetaClassBean other = (MetaClassBean) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
