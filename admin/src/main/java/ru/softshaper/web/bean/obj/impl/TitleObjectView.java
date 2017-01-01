package ru.softshaper.web.bean.obj.impl;

import ru.softshaper.web.bean.obj.ObjectView;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class TitleObjectView implements ObjectView {
  /**
   * key bissness object
   */
  private final String id;

  /**
   * contentCode bissness object
   */
  private final String contentCode;

  /**
   * title bissness object
   */
  private final String title;

  public TitleObjectView(String contentCode, String id, String title) {
    this.contentCode = contentCode;
    this.id = id;
    this.title = title;
  }

  public String getContentCode() {
    return contentCode;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }
}
