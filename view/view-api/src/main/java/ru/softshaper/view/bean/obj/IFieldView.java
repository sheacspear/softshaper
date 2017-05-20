package ru.softshaper.view.bean.obj;

import ru.softshaper.bean.meta.FieldTypeView;

/**
 * Field by Class
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