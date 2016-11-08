package ru.softshaper.services.meta;

import java.util.Collection;

/**
 * MetaStorage Created by Sunchise on 10.08.2016.
 */
public interface MetaStorage {


  /**
   * register
   *
   * @param meta
   */
  void register(Collection<? extends MetaClass> meta);

  /**
   * getMetaClass
   *
   * @param code
   * @return
   */
  MetaClass getMetaClass(String code);

  /**
   * getMetaClassById
   *
   * @param id
   * @return
   */
  MetaClass getMetaClassById(String id);

  /**
   * @return getAllMetaClasses
   */
  Collection<MetaClass> getAllMetaClasses();

  /**
   * getMetaField
   *
   * @param id
   * @return
   */
  MetaField getMetaField(String id);

  /**
   * @return getMetaFields
   */
  Collection<MetaField> getMetaFields();

  MetaClass getMetaClassByTable(String name);

}
