package ru.softshaper.web.admin.bean.objlist;

import java.util.Collection;

public interface ITableObjectsView {

  /**
   * @return the clazz
   */
  String getClazz();

  /**
   * @return the cnt
   */
  int getCnt();

  /**
   * @return the columnsView
   */
  Collection<IColumnView> getColumnsView();

  /**
   * @return the objectsView
   */
  Collection<IObjectRowView> getObjectsView();

}