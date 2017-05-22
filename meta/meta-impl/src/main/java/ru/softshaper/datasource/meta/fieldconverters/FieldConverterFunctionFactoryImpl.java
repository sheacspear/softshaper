package ru.softshaper.datasource.meta.fieldconverters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 16.05.2017.
 */
@Component
public class FieldConverterFunctionFactoryImpl implements FieldConverterFunctionFactory {

  private static final Logger log = LoggerFactory.getLogger(FieldConverterFunctionFactoryImpl.class);

  private final Map<Class, FieldValueConverterFunction> converterMap = new HashMap<>();

  public FieldConverterFunctionFactoryImpl() {
    converterMap.put(Date.class, value -> {
      try {
        if (value.length() == "dd.MM.yyyy HH:mm:ss".length()) {
          return new Date(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(value).getTime());
        }
        return new Date(new SimpleDateFormat("yyyy.MM.dd").parse(value).getTime());
      } catch (ParseException e) {        
        log.error(e.getMessage(), e);
        try {
          return new Date(new SimpleDateFormat("yyyy-MM-dd").parse(value).getTime());
        } catch (ParseException e1) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage(), e);
        }
      }
    });
    converterMap.put(Boolean.class, "true"::equals);
    converterMap.put(Integer.class, Integer::valueOf);
    converterMap.put(Long.class, Long::valueOf);
    converterMap.put(String.class, value -> value);
  }

  @Override
  public FieldValueConverterFunction getFunction(Class cls) {
    return converterMap.get(cls);
  }
}
