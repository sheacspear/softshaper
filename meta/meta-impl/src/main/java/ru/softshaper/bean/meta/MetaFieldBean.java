package ru.softshaper.bean.meta;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;

import javax.annotation.concurrent.Immutable;

/**
 * Поле мета класса
 *
 * @author asheknew
 *
 */
@Immutable
public class MetaFieldBean implements MetaField {

  /**
   * идентиикатор
   */
  private final String id;

  /**
   * класс владелец
   */
  private final MetaClass owner;

  /**
   * имя
   */
  private final String name;
  /**
   * код
   */
  private final String code;
  /**
   * колонка
   */
  private final String column;
  /**
   * тип
   */
  private final FieldType type;

  /**
   * ссылка на класс
   */
  private final MetaClass linkToMetaClass;

  /**
   * поле обратной ссылки
   */
  private final MetaField backReferenceField;

  /**
   * поле обратной ссылки
   */
  private final String nxMTableName;

  /**
   * @param id идентиикатор
   * @param owner класс владелец
   * @param name имя
   * @param code код
   * @param column колонка
   * @param type тип
   * @param linkToMetaClass ссылка на класс
   * @param backReferenceField поле обратной ссылки
   */
  public MetaFieldBean(String id, MetaClass owner, String name, String code, String column, FieldType type, MetaClass linkToMetaClass, MetaField backReferenceField, String nxMTableName) {
    super();
    this.id = id;
    this.owner = owner;
    this.name = name;
    this.code = code;
    this.column = column;
    this.type = type;
    this.linkToMetaClass = linkToMetaClass;
    this.backReferenceField = backReferenceField;
    this.nxMTableName = nxMTableName;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getOwner()
   */
  @Override
  public MetaClass getOwner() {
    return owner;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getCode()
   */
  @Override
  public String getCode() {
    return code;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getColumn()
   */
  @Override
  public String getColumn() {
    return column;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getType()
   */
  @Override
  public FieldType getType() {
    return type;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getLinkToMetaClass()
   */
  @Override
  public MetaClass getLinkToMetaClass() {
    return linkToMetaClass;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.dynamiccontent.MetaField#getBackReferenceField()
   */
  @Override
  public MetaField getBackReferenceField() {
    return backReferenceField;
  }

  @Override
  public String getNxMTableName() {
    return nxMTableName;
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
}
