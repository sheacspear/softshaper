package ru.softshaper.web.bean.obj.builder;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.web.bean.obj.IFieldView;
import ru.softshaper.web.bean.obj.impl.FieldView;

/**
 * @author ashek
 *
 * @param <T>
 */
public class FieldObjectViewBuilder<T> {

  /**
   * Field title
   */
  private String title;
  /**
   * Field code
   */
  private String code;
  /**
   * Field type
   */
  private FieldTypeView type;
  /**
   * Field link Class
   */
  private String linkMetaClass;
  /**
   * Field backink Class
   */
  private String backLinkAttr;

  /**
   * @param Field code
   * @param Field title
   * @param Field value
   */
  public FieldObjectViewBuilder(String code, String title) {
    this.title = title;
    this.code = code;
  }

  /**
   * @param Field type
   * @return
   */
  public FieldObjectViewBuilder<T> setType(FieldTypeView type) {
    this.type = type;
    return this;
  }

  /**
   * @param linkMetaClass
   * @return
   */
  public FieldObjectViewBuilder<T> setLinkMetaClass(String linkMetaClass) {
    this.linkMetaClass = linkMetaClass;
    return this;
  }

  /**
   * @param backLinkAttr
   * @return
   */
  public FieldObjectViewBuilder<T> setBackLinkAttr(String backLinkAttr) {
    this.backLinkAttr = backLinkAttr;
    return this;
  }

  /**
   * @return
   */
  public IFieldView build() {
    return new FieldView(title, code, type, linkMetaClass, backLinkAttr);
  }
}