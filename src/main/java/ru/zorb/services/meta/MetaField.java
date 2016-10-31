package ru.zorb.services.meta;

/**
 * Поле мета класса
 */
public interface MetaField {

  /**
   * @return идентиикатор
   */
  String getId();

  /**
   * @return класс владелец
   */
  MetaClass getOwner();

  /**
   * @return имя
   */
  String getName();

  /**
   * @return код
   */
  String getCode();

  /**
   * @return колонка
   */
  String getColumn();

  /**
   * @return тип
   */
  FieldType getType();

  /**
   * @return ссылка на класс
   */
  MetaClass getLinkToMetaClass();

  /**
   * @return поле обратной ссылки
   */
  MetaField getBackReferenceField();

  String getNxMTableName();
}