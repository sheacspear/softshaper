package ru.zorb.web.bean;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * field type View for bissness object <br/>
 *
 * Created by Sunchise on 23.05.2016.
 */
public class FieldTypeView {
  private static Map<String, FieldTypeView> byCode = Maps.newHashMap();
  /**
   * Число - целоисленное (1,2,3...)
   */
  public static final FieldTypeView NUMERIC_INTEGER = new FieldTypeView("NUMERIC_INTEGER", "Целочисленное");
  /**
   * Число - дробное (1.1, 3.14 ...)
   */
  public static final FieldTypeView NUMERIC_DECIMAL = new FieldTypeView("NUMERIC_DECIMAL", "Дробное");

  /**
   * Булеан - отображается текст (принять/отказать, да/нет и т д)
   */
  public static final FieldTypeView BOOLEAN_STRING = new FieldTypeView("BOOLEAN_STRING", "Текст");
  /**
   * Ссылка - выпадающий список
   */
  public static final FieldTypeView LINK_SELECTBOX = new FieldTypeView("LINK_SELECTBOX", "Выпадающий список");
  /**
   * Ссылка - выбор в отдельном окне
   */
  public static final FieldTypeView LINK_BROWSE = new FieldTypeView("LINK_BROWSE", "Ссылка - выбор в отдельном окне");
  /**
   * Ссылка - набор точек
   */
  public static final FieldTypeView LINK_RADIOBUTTON = new FieldTypeView("LINK_RADIOBUTTON", "Ссылка - набор точек");
  /**
   * МкН - выбор в отдельном окне
   */
  public static final FieldTypeView MULTILINK_BROWSE = new FieldTypeView("MULTILINK_BROWSE", "МкН - выбор в отдельном окне");
  /**
   * МкН - набор галок
   */
  public static final FieldTypeView MULTILINK_CHECKBOX = new FieldTypeView("MULTILINK_RADIOBUTTON", "МкН - набор галок");
  /**
   * Строка - однострочная
   */
  public static final FieldTypeView STRING_SINGLE = new FieldTypeView("STRING_SINGLE", "Одна строка");
  /**
   * STRING_MULTILINE
   */
  public static final FieldTypeView STRING_MULTILINE = new FieldTypeView("STRING_MULTILINE", "Многострочный");
  /**
   * Дата - только дата (без времени)
   */
  public static final FieldTypeView DATE_DATE_ONLY = new FieldTypeView("DATE_DATE_ONLY", "Только дата");
  /**
   * Дата - только время (без даты)
   */
  public static final FieldTypeView DATE_TIME_ONLY = new FieldTypeView("DATE_TIME_ONLY", "Только время");
  /**
   * Дата - целиком (время и дата)
   */
  public static final FieldTypeView DATE_BOTH = new FieldTypeView("DATE_BOTH", "Полная дата");
  /**
   * БУлеан - галка
   */
  public static final FieldTypeView BOOLEAN_CHECKBOX = new FieldTypeView("BOOLEAN_CHECKBOX", "Галка");
  /**
   * Обратная ссылка - сприсок заголовков
   */
  public static final FieldTypeView BACK_REFERENCE_LIST = new FieldTypeView("BACK_REFERENCE_LIST", "Список");
  /**
   * Обратная ссылка - таблица
   */
  public static final FieldTypeView BACK_REFERENCE_TABLE = new FieldTypeView("BACK_REFERENCE_TABLE", "Обратная ссылка - таблица");
  /**
   * Файл
   */
  public static final FieldTypeView FILE_SIMPLE = new FieldTypeView("FILE_SIMPLE", "Файл");

  /**
   * Ссылочный атрибут - вложеный объект
   */
  public static final FieldTypeView LINK_INNER_OBJECT = new FieldTypeView("LINK_INNER_OBJECT", "Вложеный объект");



  /**
   * field code
   */
  private final String code;
  /**
   * field name
   */
  private final String name;

  /**
   * @param code
   * @param name
   */
  private FieldTypeView(String code, String name) {
    this.code = code;
    this.name = name;
    byCode.put(code, this);
  }

  public static FieldTypeView byCode(String code) {
    return byCode.get(code);
  }

  public static Collection<FieldTypeView> getAll() {
    return byCode.values();
  }

  /**
   * @return code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FieldTypeView [code=" + code + ", name=" + name + "]";
  }
}
