package ru.zorb.web.bean.obj;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class TitleObjectView implements ObjectView {

  /**
   * contentCode bissness object
   */
  private final String contentCode;

  /**
   * key bissness object
   */
  private final String id;

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
