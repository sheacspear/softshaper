package ru.softshaper.services.meta;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import ru.softshaper.bean.meta.FieldTypeView;

import java.util.Collection;
import java.util.Map;

/**
 * Тип поля
 */
public class FieldType {
  /**
   *
   */
  private static Map<Long, FieldType> fieldsTypes = Maps.newHashMap();

  /**
   * Идентификатор
   */
  public static final FieldType ID = new FieldType(1, "ID", "Идентификатор", Sets.newHashSet(FieldTypeView.NUMERIC_INTEGER), FieldTypeView.NUMERIC_INTEGER);

  /**
   * Число
   */
  public static final FieldType NUMERIC_INTEGER = new FieldType(2, "NUMERIC", "Целое число", Sets.newHashSet(FieldTypeView.NUMERIC_INTEGER), FieldTypeView.NUMERIC_INTEGER);

  /**
   * Число 10
   */
  public static final FieldType NUMERIC_DECIMAL = new FieldType(3, "NUMERIC_DECIMAL", "Дробное число", Sets.newHashSet(FieldTypeView.NUMERIC_DECIMAL), FieldTypeView.NUMERIC_DECIMAL);

  /**
   * Да/нет
   */
  public static final FieldType LOGICAL = new FieldType(4, "LOGICAL", "Да/нет", Sets.newHashSet(FieldTypeView.BOOLEAN_CHECKBOX, FieldTypeView.BOOLEAN_STRING),
      FieldTypeView.BOOLEAN_CHECKBOX);

  /**
   * Ссылка
   */
  public static final FieldType LINK = new FieldType(5, "LINK", "Ссылка", Sets.newHashSet(FieldTypeView.LINK_BROWSE, FieldTypeView.LINK_RADIOBUTTON, FieldTypeView.LINK_SELECTBOX),
      FieldTypeView.LINK_SELECTBOX);

  /**
   * Множественная ссылка
   */
  public static final FieldType MULTILINK = new FieldType(6, "MULTILINK", "Множественная ссылка",
      Sets.newHashSet(FieldTypeView.MULTILINK_BROWSE, FieldTypeView.MULTILINK_CHECKBOX),
      FieldTypeView.MULTILINK_BROWSE);

  /**
   * Текст
   */
  public static final FieldType STRING = new FieldType(7, "STRING", "Строка", Sets.newHashSet(FieldTypeView.STRING_SINGLE, FieldTypeView.STRING_CODE, FieldTypeView.STRING_TAGS),
      FieldTypeView.STRING_SINGLE);




  /**
   * Календарь
   */
  public static final FieldType DATE = new FieldType(8, "DATE", "Календарь", Sets.newHashSet(FieldTypeView.DATE_BOTH, FieldTypeView.DATE_DATE_ONLY, FieldTypeView.DATE_TIME_ONLY),
      FieldTypeView.DATE_BOTH);

  /**
   * Ссылка на этот объект
   */
  public static final FieldType BACK_REFERENCE = new FieldType(9, "BACK_REFERENCE", "Ссылка на этот объект",
      Sets.newHashSet(FieldTypeView.BACK_REFERENCE_LIST, FieldTypeView.BACK_REFERENCE_TABLE), FieldTypeView.BACK_REFERENCE_TABLE);

  public static final FieldType FILE = new FieldType(10, "FILE", "Файл", Sets.newHashSet(FieldTypeView.FILE_SIMPLE), FieldTypeView.FILE_SIMPLE);

  /**
   * Текст
   */
  public static final FieldType TEXT = new FieldType(11, "TEXT", "Текст", Sets.newHashSet(FieldTypeView.STRING_MULTILINE),
      FieldTypeView.STRING_MULTILINE);

  public static final FieldType UNIVERSAL_LINK = new FieldType(12, "UNIVERSAL_LINK", "Универсальная ссылка",
      Sets.newHashSet(FieldTypeView.LINK_BROWSE, FieldTypeView.LINK_INNER_OBJECT), FieldTypeView.LINK_INNER_OBJECT);

  private final long id;

  /**
   * Кодовое Имя
   */
  private final String code;
  /**
   * Варианты отображения
   */
  private final Collection<FieldTypeView> viewVariants;
  /**
   * Наименование
   */
  private final String name;
  /**
   * Значение по умолчанию
   */
  private final FieldTypeView defaultView;

  /**
   * @param code Кодовое Имя
   * @param name Наименование
   * @param viewVariants Варианты отображения
   * @param defaultView Значение по умолчанию
   */
  private FieldType(long id, String code, String name, Collection<FieldTypeView> viewVariants, FieldTypeView defaultView) {
    this.id = id;
    this.code = code;
    this.viewVariants = viewVariants;
    this.name = name;
    this.defaultView = defaultView;
    if (fieldsTypes.containsKey(id)) {
      throw new RuntimeException("Тип поля с идентификатором " + id + " уже зарегистрирован");
    }
    fieldsTypes.put(id, this);
  }

  /**
   * FieldType by code
   *
   * @param id Идентификатор поля
   * @return Тип поля
   */
  public static FieldType getFieldType(long id) {
    return fieldsTypes.get(id);
  }

  public static Collection<FieldType> getFieldsTypes() {
    return fieldsTypes.values();
  }

  public Long getId() {
    return id;
  }

  /**
   * @return Кодовое Имя
   */
  public String getCode() {
    return code;
  }

  /**
   * @return Наименование
   */
  public String getName() {
    return name;
  }

  /**
   * @return Варианты отображения
   */
  public Collection<FieldTypeView> getViewVariants() {
    return viewVariants;
  }

  /**
   * @return Значение по умолчанию
   */
  public FieldTypeView getDefaultView() {
    return defaultView;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FieldType [code=" + code + ", viewVariants=" + viewVariants + ", name=" + name + "]";
  }
}
