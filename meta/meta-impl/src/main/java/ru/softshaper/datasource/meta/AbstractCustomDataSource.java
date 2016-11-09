package ru.softshaper.datasource.meta;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * �������� ��� ��������� ���������
 */
public abstract class AbstractCustomDataSource<T> implements ContentDataSource<T> {

  @Override
  public T getObj(GetObjectsParams params) {
    Collection<T> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  public Collection<T> getObjects(GetObjectsParams params) {
    Collection<T> objects = getAllObjects();
    objects = filterByIds(params, objects);
    objects = filterByConditions(params, objects);
    objects = order(params, objects);
    objects = limitOffset(params, objects);
    return objects;
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }



  protected Collection<T> limitOffset(GetObjectsParams params, Collection<T> objects) {
    if (params.getLimit() < Integer.MAX_VALUE || params.getOffset() > 0) {
      objects = objects.stream()
          .skip(params.getOffset())
          .limit(params.getLimit())
          .collect(Collectors.toList());
    }
    return objects;
  }

  protected abstract Collection<T> order(GetObjectsParams params, Collection<T> objects);

  protected abstract Collection<T> filterByConditions(GetObjectsParams params, Collection<T> objects);

  private Collection<T> filterByIds(GetObjectsParams params, Collection<T> objects) {
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      objects = objects.stream()
          .filter(object -> params.getIds().contains(getId(object)))
          .collect(Collectors.toSet());
    }
    return objects;
  }

  protected abstract Collection<T> getAllObjects();

  protected abstract String getId(T object);
}
