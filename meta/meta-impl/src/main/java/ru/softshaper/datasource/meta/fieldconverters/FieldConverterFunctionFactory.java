package ru.softshaper.datasource.meta.fieldconverters;

/**
 * Created by Sunchise on 16.05.2017.
 */
public interface FieldConverterFunctionFactory {

  FieldValueConverterFunction getFunction(Class cls);

}
