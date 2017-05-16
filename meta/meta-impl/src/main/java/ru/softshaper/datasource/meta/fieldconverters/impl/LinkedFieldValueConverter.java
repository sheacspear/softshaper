package ru.softshaper.datasource.meta.fieldconverters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverterFunctionFactory;
import ru.softshaper.datasource.meta.fieldconverters.FieldValueConverterFunction;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaField;

import java.util.Map;

/**
 * Created by Sunchise on 16.05.2017.
 */
@Component
@Qualifier("linkedField")
public class LinkedFieldValueConverter implements FieldConverter {

  private final DataSourceStorage dataSourceStorage;

  private final FieldConverterFunctionFactory fieldConverterFunctionFactory;


  @Autowired
  public LinkedFieldValueConverter(DataSourceStorage dataSourceStorage,
                                   FieldConverterFunctionFactory fieldConverterFunctionFactory) {
    this.dataSourceStorage = dataSourceStorage;
    this.fieldConverterFunctionFactory = fieldConverterFunctionFactory;
  }

  @Override
  public <T> T convert(MetaField field, Object viewValue) {
    String value;
    if (viewValue instanceof String) {
      value = (String) viewValue;
    } else if (viewValue instanceof Map) {
      value = ((Map) viewValue).get("id").toString();
    } else {
      value = viewValue.toString();
    }
    Class<?> idType = dataSourceStorage.get(field.getLinkToMetaClass()).getIdType(field.getLinkToMetaClass().getCode());
    FieldValueConverterFunction function = fieldConverterFunctionFactory.getFunction(idType);
    //noinspection unchecked
    return (T) function.convert(value);
  }
}
