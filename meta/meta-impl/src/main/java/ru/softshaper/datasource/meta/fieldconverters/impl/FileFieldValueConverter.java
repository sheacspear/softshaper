package ru.softshaper.datasource.meta.fieldconverters.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.MetaField;

import java.util.Map;

/**
 * Created by Sunchise on 17.05.2017.
 */
@Component
@Qualifier("file")
public class FileFieldValueConverter implements FieldConverter {
  @Override
  public <T> T convert(MetaField field, Object viewValue) {
    if (viewValue instanceof Map) {
      viewValue = ((Map) viewValue).get("id");
    }
    return (T) viewValue;
  }
}
