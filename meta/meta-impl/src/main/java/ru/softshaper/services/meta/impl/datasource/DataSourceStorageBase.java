package ru.softshaper.services.meta.impl.datasource;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;

import java.util.Collection;
import java.util.Map;

/**
 * Хранилище DataSource
 *
 * @author ashek
 *
 */
@Component
public class DataSourceStorageBase implements DataSourceStorage {

  /**
   * Источники DataSource,для MetaClass
   */
  private Map<MetaClass, ContentDataSource<?>> dataSources = Maps.newHashMap();

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSourceStorage#register(java.util.Collection, ru.softshaper.services.meta.DataSource)
   */
  @Override
  public synchronized void register(Collection<? extends MetaClass> clazzs, ContentDataSource<?> dataSource) {
    Map<MetaClass, ContentDataSource<?>> dataSource2 = Maps.newHashMap(dataSources);
    for (MetaClass clazz : clazzs) {
      dataSource2.put(clazz, dataSource);
    }
    dataSources = dataSource2;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSourceStorage#register(ru.softshaper.services.meta.MetaClass, ru.softshaper.services.meta.DataSource)
   */
  @Override
  public synchronized void register(MetaClass clazz, ContentDataSource<?> dataSource) {
    Map<MetaClass, ContentDataSource<?>> dataSource2 = Maps.newHashMap(dataSources);
    dataSource2.put(clazz, dataSource);
    dataSources = dataSource2;
  }


  @Override
  public synchronized void registerAllWithClean(Map<? extends MetaClass, ContentDataSource<?>> dataSourceMap) {
    dataSources = Maps.newHashMap(dataSourceMap);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.DataSourceStorage#get(ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public ContentDataSource<?> get(MetaClass clazz) {
    return dataSources.get(clazz);
  }


}
