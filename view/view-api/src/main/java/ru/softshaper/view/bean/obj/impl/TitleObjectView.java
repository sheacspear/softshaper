package ru.softshaper.view.bean.obj.impl;

import ru.softshaper.view.bean.obj.IObjectView;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class TitleObjectView implements IObjectView {
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

  /**
   * @param contentCode
   * @param id
   * @param title
   */
  public TitleObjectView(String contentCode, String id, String title) {
    this.contentCode = contentCode;
    this.id = id;
    this.title = title;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IObjectView#getContentCode()
   */
  @Override
  public String getContentCode() {
    return contentCode;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IObjectView#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IObjectView#getTitle()
   */
  @Override
  public String getTitle() {
    return title;
  }
}
