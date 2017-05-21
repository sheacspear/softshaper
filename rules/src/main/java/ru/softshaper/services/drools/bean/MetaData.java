package ru.softshaper.services.drools.bean;

import java.util.Collection;

import org.jooq.tools.StringUtils;

public class MetaData {

  private final Collection<String> tags;

  public MetaData(Collection<String> tags) {
    super();
    this.tags = tags;
  }

  public Collection<String> getTags() {
    return tags;
  }

  @Override
  public String toString() {
    return StringUtils.join(tags, ",");
  }
}
