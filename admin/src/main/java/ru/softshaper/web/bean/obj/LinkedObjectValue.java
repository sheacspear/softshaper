package ru.softshaper.web.bean.obj;

/**
 * Бин для ссылочного значения
 */
public class LinkedObjectValue {

  private final String id;

  private final String dynamicContentCode;

  private final String title;

  public LinkedObjectValue(String id, String dynamicContentCode, String title) {
    this.id = id;
    this.dynamicContentCode = dynamicContentCode;
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public String getDynamicContentCode() {
    return dynamicContentCode;
  }

  public String getTitle() {
    return title;
  }
}
