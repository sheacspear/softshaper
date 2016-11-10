package ru.softshaper.staticcontent.meta.comparators;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.comparators.ObjectComparator;

/**
 * Created by Sunchise on 11.11.2016.
 */
public abstract class AbstractObjectComparator<T> implements ObjectComparator<T> {

  private final ObjectExtractor<T> objectExtractor;

  public AbstractObjectComparator(ObjectExtractor<T> objectExtractor) {
    this.objectExtractor = objectExtractor;
  }

  @Override
  public int compareField(MetaField metaField, T o1, T o2) {
    Object v1 = objectExtractor.getValue(o1, metaField);
    Object v2 = objectExtractor.getValue(o2, metaField);
    if (v1 == null) {
      return v2 == null ? 0 : -1;
    }
    if (v2 == null) {
      return 1;
    }
    if (v1 instanceof Comparable) {
      return ((Comparable)v1).compareTo(v2);
    }
    throw new RuntimeException("Uncomparable values of class " + v1.getClass().getCanonicalName());
  }
}
