package ru.softshaper.datasource.events;

import java.util.Collection;
import ru.softshaper.services.meta.MetaClass;

public class ObjectExecuted {

  private final String metaClass;

  private final String id;

  private final String userLogin;

  private final Collection<String> msgs;

  public ObjectExecuted(String metaClass, String id, String userLogin, Collection<String> msgs) {
    super();
    this.metaClass = metaClass;
    this.id = id;
    this.userLogin = userLogin;
    this.msgs = msgs;
  }

  public String getMetaClass() {
    return metaClass;
  }

  public String getId() {
    return id;
  }

  public String getUserLogin() {
    return userLogin;
  }

  public Collection<String> getMsgs() {
    return msgs;
  }

  @Override
  public String toString() {
    return "ObjectExecuted [metaClass=" + metaClass + ", id=" + id + ", userLogin=" + userLogin + ", msgs=" + msgs + "]";
  }
}
