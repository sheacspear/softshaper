package ru.softshaper.services.meta.jooq;

import com.google.common.collect.Maps;

import ru.softshaper.services.meta.FieldType;

import java.util.Map;

import org.jooq.DataType;
import org.jooq.impl.SQLDataType;

/**
 * Мапер, который преобразует тип поля зорба в SQL тип
 */
public class JooqFieldFactory {

  private final Map<FieldType, DataType<?>> map = Maps.newHashMap();

  private final static JooqFieldFactory instance = new JooqFieldFactory();

  public static JooqFieldFactory getInstance() {
    return instance;
  }

  private JooqFieldFactory() {
    map.put(FieldType.DATE, SQLDataType.DATE);
    map.put(FieldType.LOGICAL, SQLDataType.BOOLEAN);
    map.put(FieldType.NUMERIC_INTEGER, SQLDataType.INTEGER);
    map.put(FieldType.LINK, SQLDataType.BIGINT);
    map.put(FieldType.ID, SQLDataType.BIGINT);
    map.put(FieldType.STRING, SQLDataType.VARCHAR);
    map.put(FieldType.TEXT, SQLDataType.LONGVARCHAR);
    map.put(FieldType.FILE, SQLDataType.VARCHAR);
    map.put(FieldType.UNIVERSAL_LINK, SQLDataType.VARCHAR);
  }

  public DataType<?> getDataType(FieldType fieldType) {
    return map.get(fieldType);
  }
}
