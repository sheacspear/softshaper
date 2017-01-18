package ru.softshaper.web.bean.obj;

import ru.softshaper.bean.meta.FieldTypeView;

/**
 * Field by Class
 * 
 * @param <T>
 */
public interface IFieldView {
  /**
   * @return the title
   */
  String getTitle();

  /**
   * @return the code
   */
  String getCode();

  /**
   * @return the type
   */
  FieldTypeView getType();

  /**
   * @return
   */
  String getLinkMetaClass();

  /**
   * @return
   */
  String getBackLinkAttr();

}