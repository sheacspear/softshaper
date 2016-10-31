package ru.zorb.services.meta;

import java.util.Collection;
import java.util.Map;

/**
 * @author ashek
 *
 */
public interface StaticContent {

  /**
   * @return
   */
  String getCode();

  /**
   * @return
   */
  String getName();

  /**
   * @return
   */
  MetaClassMutable getMetaClass();

  /**
   * @return
   */
  ContentDataSource<?> getContentDataSource();

  /**
   * @param clazzCode
   * @return
   */
  Collection<MetaFieldMutable> loadFields(Map<String, MetaClassMutable> clazzCode);
}
