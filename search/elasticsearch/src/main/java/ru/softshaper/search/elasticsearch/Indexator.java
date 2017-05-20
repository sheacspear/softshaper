package ru.softshaper.search.elasticsearch;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 26.09.2016.
 */
@Component
public class Indexator {


  private static final Logger log = LoggerFactory.getLogger(Indexator.class);
  public static final String INDEX_BASE = "softshaper";

  private final Client client;

  private final DataSourceStorage dataSourceStorage;

  private final MetaStorage metaStorage;

  private final ViewSettingStore viewSettingStore;

  @Autowired
  Searcher searcher;

  @Autowired
  public Indexator(Client client, DataSourceStorage dataSourceStorage, MetaStorage metaStorage, ViewSettingStore viewSettingStore) {
    this.client = client;
    this.dataSourceStorage = dataSourceStorage;
    this.metaStorage = metaStorage;
    this.viewSettingStore = viewSettingStore;
  }

  public void fullIndex() {
    long time = System.currentTimeMillis();
    for (MetaClass metaClass : metaStorage.getAllMetaClasses()) {
      ContentDataSource contentDataSource = dataSourceStorage.get(metaClass);
      Collection<?> objList;
      try {
        objList = contentDataSource.getObjects(GetObjectsParams.newBuilder(metaClass).build());
      } catch (Exception e) {
        continue;
      }
      if (objList != null && !objList.isEmpty()) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        objList.forEach(object -> {
          try {
            XContentBuilder sourceBuilder = XContentFactory.jsonBuilder().startObject();
            for (MetaField field : metaClass.getFields()) {
              if (field.getColumn() != null) {
                Object value = contentDataSource.getObjectExtractor().getValue(object, field);
                sourceBuilder.field(field.getCode(), value == null ? null : value.toString());
              }
            }
            sourceBuilder = sourceBuilder.endObject();
            IndexRequestBuilder index = client.prepareIndex("softshaper", metaClass.getCode(), contentDataSource.getObjectExtractor().getId(object, metaClass));
            index.setSource(sourceBuilder);
            bulkRequest.add(index);
          } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
          }
        });
        bulkRequest.get();
      }
    }
    log.info("Full search index at " + (System.currentTimeMillis() - time) + "ms");
  }

  public void indexObject(String metaClassCode, String id, Map<String, Object> values) {
    XContentBuilder sourceBuilder;
    try {
      sourceBuilder = XContentFactory.jsonBuilder().startObject();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      String fieldCode = entry.getKey();
      Object value = entry.getValue();
      if (!(value instanceof Collection)) {
        try {
          sourceBuilder.field(fieldCode, value == null ? null : value.toString());
        } catch (IOException e) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage(), e);
        }
      }
      try {
        sourceBuilder = sourceBuilder.endObject();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
      IndexRequestBuilder index = client.prepareIndex(INDEX_BASE, metaClassCode, id);
      index.setSource(sourceBuilder);
      BulkRequestBuilder bulkRequest = client.prepareBulk();
      bulkRequest.add(index);
      bulkRequest.get();
    }
  }

  public void deleteIndex(String metaClassCode, String id) {
    DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(INDEX_BASE, metaClassCode, id);
    client.prepareBulk().add(deleteRequestBuilder).get();
  }

}
