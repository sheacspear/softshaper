package ru.zorb.services.meta;

import java.util.Collection;

/**
 * мета класс
 */
public interface MetaClass {

  /**
   * @return Идентификатор
   */
  String getId();

  /**
   * @return Код
   */
  String getCode();

  /**
   * @return Имя
   */
  String getName();

  /**
   * @return Таблица
   */
  String getTable();

  /**
   * @return Поля
   */
  Collection<? extends MetaField> getFields();

  /**
   * Получение поля по коду
   *
   * @param fieldCode
   * @return
   */
  MetaField getField(String fieldCode);

  /**
   * @return колнка идентификатор
   */
  String getIdColumn();

  /**
   * @return последовательность для идентификатора
   */
  String getIdSequence();

  /**
   * @return имя PrimaryKey
   */
  String getPrimaryKey();

  /**
   * @return Проверять безопасность
   */
  boolean isCheckSecurity();

  /**
   * @return Проверять безопасность на объекты
   */
  boolean isCheckObjectSecurity();

  /**
   * @return фиксированный
   */
  boolean isFixed();

}