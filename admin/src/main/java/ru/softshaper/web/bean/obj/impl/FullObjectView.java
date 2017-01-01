package ru.softshaper.web.bean.obj.impl;

import java.util.List;

import ru.softshaper.web.bean.obj.FieldObjectView;
import ru.softshaper.web.bean.obj.ObjectView;
import ru.softshaper.web.bean.obj.builder.FullObjectViewBuilder;

/**
 * View for bissness object</br>
 * Created by Sunchise on 23.05.2016.
 */
public class FullObjectView implements ObjectView {
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
   * fields by bissness object
   */
  private final List<FieldObjectView<?>> fields;

  /**
   * @param key bissness object
   * @param title bissness object
   * @param fields bissness object
   */
  public FullObjectView(String contentCode, String id, String title, List<FieldObjectView<?>> fields) {
    this.contentCode = contentCode;
    this.id = id;
    this.title = title;
    this.fields = fields;
  }

  public String getContentCode() {
    return contentCode;
  }

  /**
   * @return key bissness object
   */
  public String getId() {
    return id;
  }

  /**
   * @return title bissness object
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return fields by bissness object
   */
  public List<FieldObjectView<?>> getFields() {
    return fields;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "FullObjectView [id=" + id + ", title=" + title + ", fields=" + fields + "]";
  }

  /**
   * Builder for create FullObjectView
   *
   * @param key key object
   * @return FullObjectViewBuilder
   */
  public static FullObjectViewBuilder newBuilder(String contentCode, String key) {
    return new FullObjectViewBuilder(contentCode, key);
  }
}
