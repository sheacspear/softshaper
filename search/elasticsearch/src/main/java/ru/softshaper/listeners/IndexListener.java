package ru.softshaper.listeners;

import com.google.common.eventbus.Subscribe;
import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.events.ObjectDeleted;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.search.elasticsearch.Indexator;

import java.util.Map;
import java.util.stream.Collectors;

public class IndexListener {

  private final Indexator indexator;

  public IndexListener(Indexator indexator) {
    this.indexator = indexator;
  }

  @Subscribe
  public void onObjectCreate(ObjectCreated event) {
    Map<String, Object> values = event.getValues().entrySet().stream().collect(Collectors.toMap(key -> key.getKey().getCode(), Map.Entry::getValue));
    indexator.indexObject(event.getMetaClass().getCode(), event.getId(), values);
  }

  @Subscribe
  public void onObjectUpdate(ObjectUpdated event) {
    Map<String, Object> values = event.getValues().entrySet().stream().collect(Collectors.toMap(key -> key.getKey().getCode(), Map.Entry::getValue));
    indexator.indexObject(event.getMetaClass().getCode(), event.getId(), values);
  }

  @Subscribe
  public void onObjectDelete(ObjectDeleted event) {
    indexator.deleteIndex(event.getMetaClass().getCode(), event.getId());
  }
}
