package ru.softshaper.staticcontent.meta.extractors;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 10.11.2016.
 */
public abstract class AbstractObjectExtractor<T> implements ObjectExtractor<T> {

  private final Map<String, Extractor<T, ?>> valueExtractorByField = new HashMap<>();

  protected void registerFieldExtractor(String metaFieldCode, Extractor<T, ?> extractor) {
    valueExtractorByField.put(metaFieldCode, extractor);
  }

  @Override
  public Object getValue(T obj, MetaField field) {
    Extractor<T, ?> metaFieldValueExtractor = valueExtractorByField.get(field.getCode());
    return metaFieldValueExtractor == null ? null : metaFieldValueExtractor.value(obj);
  }

}
