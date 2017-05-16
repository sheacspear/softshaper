package ru.softshaper.datasource.meta.fieldconverters.impl;

import org.jooq.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverterFunctionFactory;
import ru.softshaper.datasource.meta.fieldconverters.FieldValueConverterFunction;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.jooq.JooqFieldFactory;

/**
 * Created by Sunchise on 07.02.2017.
 */
@Component
@Qualifier("simpleField")
public class SimpleFieldValueConverter implements FieldConverter {

  private static final Logger log = LoggerFactory.getLogger(SimpleFieldValueConverter.class);


  private final JooqFieldFactory jooqFieldFactory = JooqFieldFactory.getInstance();

  private final FieldConverterFunctionFactory fieldConverterFunctionFactory;

  @Autowired
  public SimpleFieldValueConverter(FieldConverterFunctionFactory fieldConverterFunctionFactory) {
    this.fieldConverterFunctionFactory = fieldConverterFunctionFactory;
  }

  @Override
  public <T> T convert(MetaField field, Object viewValue) {
    if ("null".equals(viewValue) || viewValue == null) {
      return null;
    }
    FieldValueConverterFunction fieldValueConverterFunction = null;
    DataType<?> dataType = jooqFieldFactory.getDataType(field.getType());
    if (dataType != null) {
      fieldValueConverterFunction = fieldConverterFunctionFactory.getFunction(dataType.getType());
    }
    // noinspection unchecked
    return fieldValueConverterFunction != null ? (T) fieldValueConverterFunction.convert(viewValue.toString()) : null;
  }

}
