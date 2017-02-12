package ru.softshaper.datasource.meta;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.jooq.JooqFieldFactory;

/**
 * Created by Sunchise on 07.02.2017.
 */
@Service
public class FieldValueConverterImpl implements FieldConverter {

  private static final Logger log = LoggerFactory.getLogger(FieldValueConverterImpl.class);

  private final DataSourceStorage dataSourceStorage;

  private final JooqFieldFactory jooqFieldFactory = JooqFieldFactory.getInstance();

  private final Map<Class, FieldValueConverterFunction> converterMap = new HashMap<>();

  @Autowired
  public FieldValueConverterImpl(DataSourceStorage dataSourceStorage) {
    this.dataSourceStorage = dataSourceStorage;
    converterMap.put(Date.class, value -> {
      try {
        return new Date(new SimpleDateFormat("dd.MM.yyy HH:mm:ss").parse(value).getTime());
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    });
    converterMap.put(Boolean.class, "true"::equals);
    converterMap.put(Integer.class, Integer::valueOf);
    converterMap.put(Long.class, Long::valueOf);
    converterMap.put(String.class, value -> value);
  }

  @Override
  public <T> T convert(MetaField field, String stringValue) {
    if ("null".equals(stringValue)) {
      return null;
    }
    FieldValueConverterFunction fieldValueConverterFunction;
    if (FieldType.LINK.equals(field.getType())) {
      Class<?> idType = dataSourceStorage.get(field.getLinkToMetaClass()).getIdType(field.getLinkToMetaClass().getCode());
      fieldValueConverterFunction = converterMap.get(idType);
    } else {
      fieldValueConverterFunction = converterMap.get(jooqFieldFactory.getDataType(field.getType()).getType());
    }
    // noinspection unchecked
    return (T) fieldValueConverterFunction.convert(stringValue);
  }

}
