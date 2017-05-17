package ru.softshaper.datasource.events;

import ru.softshaper.services.meta.MetaClass;

/**
 * Created by Sunchise on 28.03.2017.
 */
public class ObjectDeleted {

  private final MetaClass metaClass;

  private final String id;

  private final String userLogin;

  public ObjectDeleted(MetaClass metaClass, String id, String userLogin) {
    this.metaClass = metaClass;
    this.id = id;
    this.userLogin = userLogin;
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

  public String getId() {
    return id;
  }

  public String getUserLogin() {
    return userLogin;
  }

  @Override
  public String toString() {
    return "ObjectDeleted{" +
        "metaClass=" + metaClass +
        ", id='" + id + '\'' +
        '}';
  }
}


