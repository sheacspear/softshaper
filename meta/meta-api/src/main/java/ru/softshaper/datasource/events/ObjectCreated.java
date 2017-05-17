package ru.softshaper.datasource.events;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;

import java.util.Collections;
import java.util.Map;

public class ObjectCreated {


  private final MetaClass metaClass;

  private final String id;

  private final String userLogin;

  private final Map<MetaField, Object> values;

  public ObjectCreated(MetaClass metaClass, String id, Map<MetaField, Object> values, String userLogin) {
    this.metaClass = metaClass;
    this.id = id;
    this.values = Collections.unmodifiableMap(values);
    this.userLogin = userLogin;
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

  public String getId() {
    return id;
  }

  public Map<MetaField, Object> getValues() {
    return values;
  }

  public String getUserLogin() {
    return userLogin;
  }

  @Override
  public String toString() {
    return "ObjectCreated{" +
        "metaClass=" + metaClass +
        ", id='" + id + '\'' +
        '}';
  }
}
