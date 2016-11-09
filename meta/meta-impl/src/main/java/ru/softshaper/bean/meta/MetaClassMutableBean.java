package ru.softshaper.bean.meta;

import com.google.common.collect.Maps;
import ru.softshaper.services.meta.MetaClassMutable;
import ru.softshaper.services.meta.MetaField;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * мета класс
 *
 * @author asheknew
 *
 */
@ThreadSafe
public class MetaClassMutableBean extends MetaClassBean implements MetaClassMutable {

  /**
   * @param id Идентификатор
   * @param dataSource Источник данных
   * @param code Код
   * @param name Имя
   * @param table Таблица
   * @param fields Поля
   * @param checkSecurity
   * @param checkObjectSecurity
   */
  public MetaClassMutableBean(String id, String code, String name, String table, Collection<? extends MetaField> fields, boolean checkSecurity, boolean checkObjectSecurity) {
    this(id, code, name, table, fields, checkSecurity, checkObjectSecurity, false);
  }

  /**
   * @param id Идентификатор
   * @param code Код
   * @param name Имя
   * @param table Таблица
   * @param fields Поля
   * @param checkSecurity
   * @param checkObjectSecurity
   */
  public MetaClassMutableBean(String id, String code, String name, String table, Collection<? extends MetaField> fields, boolean checkSecurity, boolean checkObjectSecurity,
                              boolean fixed) {
    super(id, code, name, table, fields, checkSecurity, checkObjectSecurity, fixed);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.meta.MetaClassMutable#addField(ru.softshaper.services.dynamiccontent.MetaField)
   */
  @Override
  public synchronized void addField(MetaField field) {
    ArrayList<MetaField> fields = new ArrayList<>(this.fields);
    fields.add(field);
    this.fields = Collections.unmodifiableCollection(fields);
    Map<String, MetaField> fieldByCodeNew = Maps.newHashMap(fieldByCode);
    fieldByCodeNew.put(field.getCode(), field);
    fieldByCode = fieldByCodeNew;
  }
}
