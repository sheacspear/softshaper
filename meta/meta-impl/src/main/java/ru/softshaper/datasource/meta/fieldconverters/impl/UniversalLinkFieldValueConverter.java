package ru.softshaper.datasource.meta.fieldconverters.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.fieldconverters.FieldConverter;
import ru.softshaper.services.meta.MetaField;

/**
 * Created by Sunchise on 17.05.2017.
 */
@Component
@Qualifier("universalLink")
public class UniversalLinkFieldValueConverter implements FieldConverter {
  @Override
  public <T> T convert(MetaField field, Object viewValue) {
    if (viewValue == null) {
      return null;
    }
    return (T) (viewValue + "@" + field.getOwner().getCode());
  }
}
