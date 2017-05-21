package ru.softshaper.listeners;

import com.google.common.eventbus.Subscribe;
import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.events.ObjectDeleted;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.search.elasticsearch.Indexator;
import ru.softshaper.services.meta.MetaField;

import java.util.HashMap;
import java.util.Map;

public class IndexListener {

  private final Indexator indexator;

  public IndexListener(Indexator indexator) {
    this.indexator = indexator;
  }

  @Subscribe
  public void onObjectCreate(ObjectCreated event) {
    Map<String, Object> values = convertMap(event.getValues());
    indexator.indexObject(event.getMetaClass().getCode(), event.getId(), values);
  }

  @Subscribe
  public void onObjectUpdate(ObjectUpdated event) {
    Map<String, Object> values = convertMap(event.getValues());
    indexator.indexObject(event.getMetaClass().getCode(), event.getId(), values);
  }

  @Subscribe
  public void onObjectDelete(ObjectDeleted event) {
    indexator.deleteIndex(event.getMetaClass().getCode(), event.getId());
  }

  private Map<String, Object> convertMap(Map<MetaField, Object> inMap) {
    HashMap<String, Object> outMap = new HashMap<>();
    for (Map.Entry<MetaField, Object> entry : inMap.entrySet()) {
      outMap.put(entry.getKey().getCode(), entry.getValue());
    }
    return outMap;
  }
}
