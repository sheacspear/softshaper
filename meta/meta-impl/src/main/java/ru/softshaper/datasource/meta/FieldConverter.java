package ru.softshaper.datasource.meta;

import ru.softshaper.services.meta.MetaField;

/**
 * Created by Sunchise on 07.02.2017.
 */
public interface FieldConverter {

  <T> T convert(MetaField field, String stringValue);

  @FunctionalInterface
  interface FieldValueConverterFunction<O> {
    O convert(String value);
  }

}
