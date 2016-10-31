package ru.zorb.services.meta;

/**
 * Поле мета класса
 *
 */
public interface MetaFieldMutable extends MetaField {

  /**
   * @return
   */
  MetaClassMutable getOwnerMutable();

}
