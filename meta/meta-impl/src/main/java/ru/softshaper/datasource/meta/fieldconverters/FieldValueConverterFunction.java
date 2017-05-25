package ru.softshaper.datasource.meta.fieldconverters;

/**
 * Created by Sunchise on 16.05.2017.
 */
@FunctionalInterface
public interface FieldValueConverterFunction<O> {
  O convert(String value);
}
