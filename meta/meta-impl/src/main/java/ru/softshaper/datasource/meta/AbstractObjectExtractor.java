package ru.softshaper.datasource.meta;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;

/**
 * Экстарктор для получения значения объекта по метаклассу
 */
public abstract class AbstractObjectExtractor<T> implements ObjectExtractor<T> {

  /**
   * Мапа определяющая какой метод объекта вызвать, что бы получить значение
   * поля
   */
  private final Map<String, FieldExtractor<T, ?>> valueExtractorByField = new HashMap<>();

  /**
   * MetaStorage
   */
  @Autowired
  protected MetaStorage metaStorage;

  /**
   * Зарегистрировать экстрактор поля
   *
   * @param metaFieldCode код мета-поля
   * @param fieldExtractor экстрактор поля
   */
  protected void registerFieldExtractor(String metaFieldCode, FieldExtractor<T, ?> fieldExtractor) {
    valueExtractorByField.put(metaFieldCode, fieldExtractor);
  }

  @Override
  public Object getValue(T obj, MetaField field) {
    FieldExtractor<T, ?> metaFieldValueFieldExtractor = valueExtractorByField.get(field.getCode());
    return metaFieldValueFieldExtractor == null ? null : metaFieldValueFieldExtractor.value(obj);
  }
}
