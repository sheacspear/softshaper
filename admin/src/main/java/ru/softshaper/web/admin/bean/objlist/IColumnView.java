package ru.softshaper.web.admin.bean.objlist;

import ru.softshaper.bean.meta.FieldTypeView;

public interface IColumnView {

  /**
   * @return the name
   */
  String getName();

  /**
   * @return the key
   */
  String getKey();

  /**
   * @return the typeView
   */
  FieldTypeView getTypeView();

  /**
   * @return the sortable
   */
  boolean isSortable();

  /**
   * @return the filterable
   */
  boolean isFilterable();

}