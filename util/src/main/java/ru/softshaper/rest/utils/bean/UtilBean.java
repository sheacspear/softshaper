package ru.softshaper.rest.utils.bean;

public class UtilBean {

  private final String name;

  private final String code;

  public UtilBean(String name, String code) {
    super();
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }
}
