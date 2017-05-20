package ru.softshaper.view.bean.obj.impl;

import ru.softshaper.view.bean.obj.IUtilView;

public class UtilView implements IUtilView {

  private final String name;

  private final String code;

  public UtilView(String name, String code) {
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
