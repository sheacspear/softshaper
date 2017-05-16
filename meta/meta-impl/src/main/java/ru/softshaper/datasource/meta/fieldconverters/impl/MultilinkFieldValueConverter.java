package ru.softshaper.datasource.meta.fieldconverters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverterFunctionFactory;
import ru.softshaper.datasource.meta.fieldconverters.FieldValueConverterFunction;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaField;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 16.05.2017.
 */
@Component
@Qualifier("multilinkField")
public class MultilinkFieldValueConverter implements FieldConverter {

  private final DataSourceStorage dataSourceStorage;

  private final FieldConverterFunctionFactory fieldConverterFunctionFactory;

  @Autowired
  public MultilinkFieldValueConverter(DataSourceStorage dataSourceStorage, FieldConverterFunctionFactory fieldConverterFunctionFactory) {
    this.dataSourceStorage = dataSourceStorage;
    this.fieldConverterFunctionFactory = fieldConverterFunctionFactory;
  }

  @Override
  public <T> T convert(MetaField field, Object viewValue) {
    final List<Object> ids = new ArrayList<>();
    if (viewValue instanceof Map) {
      Map multilinkMap = (Map) viewValue;
      List<Map<String, String>> objectsViews = (List<Map<String, String>>) multilinkMap.get("objectsView");
      if (objectsViews != null) {
        Class<?> idType = dataSourceStorage.get(field.getLinkToMetaClass()).getIdType(field.getLinkToMetaClass().getCode());
        FieldValueConverterFunction function = fieldConverterFunctionFactory.getFunction(idType);
        ids.addAll(objectsViews.stream()
                               .map(obj -> function.convert(obj.get("id")))
                               .collect(Collectors.toList()));
      }
    } else {
      throw new NotImplementedException();
    }
    return (T) ids;
  }
}
