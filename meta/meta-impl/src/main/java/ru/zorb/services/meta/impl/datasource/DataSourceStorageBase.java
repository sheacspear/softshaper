package ru.zorb.services.meta.impl.datasource;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.DataSourceStorage;
import ru.zorb.services.meta.MetaClass;

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
   * @see ru.zorb.services.meta.DataSourceStorage#register(java.util.Collection, ru.zorb.services.meta.DataSource)
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
   * @see ru.zorb.services.meta.DataSourceStorage#register(ru.zorb.services.meta.MetaClass, ru.zorb.services.meta.DataSource)
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
   * @see ru.zorb.services.meta.DataSourceStorage#get(ru.zorb.services.meta.MetaClass)
   */
  @Override
  public ContentDataSource<?> get(MetaClass clazz) {
    return dataSources.get(clazz);
  }


}
