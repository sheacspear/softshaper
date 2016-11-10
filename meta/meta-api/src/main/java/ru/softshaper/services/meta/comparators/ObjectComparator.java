package ru.softshaper.services.meta.comparators;

import ru.softshaper.services.meta.MetaField;

/**
 * Created by Sunchise on 09.11.2016.
 */
public interface ObjectComparator<T> {
  int compareField(MetaField metaField, T o1, T o2);
}
