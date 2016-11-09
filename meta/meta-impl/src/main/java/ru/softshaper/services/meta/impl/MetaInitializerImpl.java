package ru.softshaper.services.meta.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaClassMutable;
import ru.softshaper.services.meta.MetaFieldMutable;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.MetaLoader;
import ru.softshaper.services.meta.MetaStorage;

/**
 * MetaInitializer<br/>
 * Created by Sunchise on 10.08.2016.
 */
//@Component
public class MetaInitializerImpl implements MetaInitializer {

  /**
   * MetaLoader
   */
  private final Collection<MetaLoader> metaLoaders = Sets.newConcurrentHashSet();

  /**
   * MetaStorage
   */
  private final MetaStorage metaStorage;

  /**
   * DataSourceStorage
   */
  private final DataSourceStorage dataSourceStorage;

  /**
   * @param metaStorage
   * @param dataSourceStorage
   */
  @Autowired
  public MetaInitializerImpl(MetaStorage metaStorage, DataSourceStorage dataSourceStorage) {
    this.metaStorage = metaStorage;
    this.dataSourceStorage = dataSourceStorage;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaInitializer#registerLoader(ru.softshaper.services.meta.MetaLoader)
   */
  @Override
  public void registerLoader(MetaLoader loader) {
    metaLoaders.add(loader);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaInitializer#init()
   */
  @Override
  public synchronized void init() {
    // todo чистка dataSourceStorage,а лучше регистрация пост файктум
    List<MetaClassMutable> result = Lists.newArrayList();
    Map<String, MetaClassMutable> clazzCodeMap = Maps.newHashMap();
    Map<String, MetaClassMutable> clazzIdMap = Maps.newHashMap();
    Map<MetaClassMutable, ContentDataSource<?>> dataSourceMap = Maps.newHashMap();
    metaLoaders.forEach(loader -> {
      Map<MetaClassMutable, ContentDataSource<?>> metasData = loader.loadClasses();
      for (Entry<MetaClassMutable, ContentDataSource<?>> entry : metasData.entrySet()) {
        MetaClassMutable meta = entry.getKey();
        ContentDataSource<?> dataSource = entry.getValue();
        result.add(meta);
        clazzCodeMap.put(meta.getCode(), meta);
        clazzIdMap.put(meta.getId().toString(), meta);
        dataSourceMap.put(meta, dataSource);
      }
    });

    // грузим поля, кроме обратных ссылок, т.к. им нужны поля, которых может ещё не быть
    metaLoaders.forEach(loader -> {
      Collection<MetaFieldMutable> fields = loader.loadFields(clazzCodeMap, clazzIdMap);
      if (fields!=null&&!fields.isEmpty()) {
        fields.forEach(field -> {
          if (!FieldType.BACK_REFERENCE.equals(field.getType())) {
            if (field.getOwnerMutable() != null) {
              field.getOwnerMutable().addField(field);
            }
          }
        });
      }
    });
    // Грузим обратные ссылки
    metaLoaders.forEach(loader -> {
      Collection<MetaFieldMutable> fields = loader.loadFields(clazzCodeMap, clazzIdMap);
      if (fields!=null&&!fields.isEmpty()) {
        fields.forEach(field -> {
          if (FieldType.BACK_REFERENCE.equals(field.getType())) {
            if (field.getOwnerMutable() != null) {
              field.getOwnerMutable().addField(field);
            }
          }
        });
      }
    });
    dataSourceStorage.registerAllWithClean(dataSourceMap);
    metaStorage.register(result);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaInitializer#init(ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public void init(MetaClass meta) {
    throw new RuntimeException("Not implemented yet!");
  }
}
