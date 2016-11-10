package ru.softshaper.services.meta;

/**
 * Created by Sunchise on 10.11.2016.
 */
public interface ObjectExtractor<T> {

  String getId(T obj, MetaClass metaClass);

  Object getValue(T obj, MetaField field);


  @FunctionalInterface
  interface Extractor<T, V> {
    V value(T from);
  }
}
