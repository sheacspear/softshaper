package ru.zorb.services.meta;

import java.util.Collection;
import java.util.Map;

/**
 * Хранилище DataSource
 *
 * @author ashek
 *
 */
public interface DataSourceStorage {

  /**
   * Регистрация
   *
   * @param clazz
   * @param dataSource
   */
  void register(Collection<? extends MetaClass> clazzs, ContentDataSource<?> dataSource);

  /**
   * Регистрация
   *
   * @param clazz
   * @param dataSource
   */
  void register(MetaClass clazz, ContentDataSource<?> dataSource);

  /**
   * Получение
   *
   * @param clazz
   * @return dataSource
   */
  ContentDataSource<?> get(MetaClass clazz);

  /**
   * @param dataSourceMap
   */
  void registerAllWithClean(Map<? extends MetaClass, ContentDataSource<?>> dataSourceMap);

}
