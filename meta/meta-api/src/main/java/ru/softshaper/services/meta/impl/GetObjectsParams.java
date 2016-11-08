package ru.softshaper.services.meta.impl;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.conditions.Condition;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by Sunchise on 13.09.2016.
 */
public class GetObjectsParams {

  private final MetaClass metaClass;

  private final LinkedHashMap<MetaField, SortOrder> orderFields;

  private final Condition condition;

  private final int limit;

  private final int offset;

  private final Collection<String> ids;

  GetObjectsParams(MetaClass metaClass, LinkedHashMap<MetaField, SortOrder> orderFields, Condition condition, int limit, int offset, Collection<String> ids) {
    this.metaClass = metaClass;
    this.orderFields = orderFields;
    this.condition = condition;
    this.limit = limit;
    this.offset = offset;
    this.ids = ids;
  }

  public static GetObjectsParamsBuilder newBuilder(MetaClass metaClass) {
    return new GetObjectsParamsBuilder(metaClass);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

  public LinkedHashMap<MetaField, SortOrder> getOrderFields() {
    return orderFields;
  }

  public Condition getCondition() {
    return condition;
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }

  public Collection<String> getIds() {
    return ids;
  }
}
