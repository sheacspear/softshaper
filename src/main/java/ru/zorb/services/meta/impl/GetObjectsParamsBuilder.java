package ru.zorb.services.meta.impl;

import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.conditions.Condition;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class GetObjectsParamsBuilder {
  private final MetaClass metaClass;
  private final OrderFields orderFields = new OrderFields(this);
  private Condition condition;
  private Collection<String> ids;

  private int limit = Integer.MAX_VALUE;

  private int offset = 0;

  GetObjectsParamsBuilder(MetaClass metaClass) {
    this.metaClass = metaClass;
  }

  public OrderFields orderFields() {
    return orderFields;
  }

  public GetObjectsParamsBuilder setCondition(Condition condition) {
    this.condition = condition;
    return this;
  }

  public GetObjectsParamsBuilder setLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public GetObjectsParamsBuilder setOffset(int offset) {
    this.offset = offset;
    return this;
  }

  public GetObjectsParamsBuilder addIds(Collection<String> ids) {
    if (this.ids == null) {
      this.ids = new HashSet<>();
    }
    this.ids.addAll(ids);
    return this;
  }

  public IDs ids() {
    return id -> {
      if (ids == null) {
        ids = new HashSet<>();
      }
      ids.add(id);
      return GetObjectsParamsBuilder.this;
    };
  }

  public GetObjectsParams build() {
    return new GetObjectsParams(metaClass, orderFields.getOrderFields(), condition, limit, offset, ids);
  }

  public class OrderFields {
    LinkedHashMap<MetaField, SortOrder> orderFields;
    final GetObjectsParamsBuilder builder;

    OrderFields(GetObjectsParamsBuilder builder) {
      this.builder = builder;
    }

    public GetObjectsParamsBuilder add(MetaField field, SortOrder order) {
      if (orderFields == null) {
        orderFields = new LinkedHashMap<>();
      }
      orderFields.put(field, order);
      return builder;
    }

    LinkedHashMap<MetaField, SortOrder> getOrderFields() {
      return orderFields;
    }
  }

  public interface IDs {
    GetObjectsParamsBuilder add(String id);
  }
}