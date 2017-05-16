package ru.softshaper.datasource.meta.fieldconverters;

import ru.softshaper.services.meta.MetaField;

/**
 * Created by Sunchise on 07.02.2017.
 */
public interface FieldConverter {

  <T> T convert(MetaField field, Object viewValue);

}
