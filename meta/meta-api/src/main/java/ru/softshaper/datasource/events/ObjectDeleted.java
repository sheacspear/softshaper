package ru.softshaper.datasource.events;

import ru.softshaper.services.meta.MetaClass;

/**
 * Created by Sunchise on 28.03.2017.
 */
public class ObjectDeleted {

  private final MetaClass metaClass;

  private final String id;

  public ObjectDeleted(MetaClass metaClass, String id) {
    this.metaClass = metaClass;
    this.id = id;
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "ObjectDeleted{" +
        "metaClass=" + metaClass +
        ", id='" + id + '\'' +
        '}';
  }
}


