package ru.softshaper.view.viewsettings;

import ru.softshaper.bean.meta.FieldTypeView;

/**
 *
 */
public interface ViewSetting {

  /**
   * @return the columnContent
   */
  String getColumnContent();

  /**
   * @return
   */
  Boolean isTableField();

  /**
   * @return the number
   */
  Integer getNumber();

  /**
   * @return the readonly
   */
  Boolean getReadonly();

  /**
   * @return the required
   */
  Boolean getRequired();

  /**
   * @return the title
   */
  String getTitle();

  /**
   * @return the titlefield
   */
  Boolean isTitleField();

  /**
   * @return the typeView
   */
  FieldTypeView getTypeView();

}