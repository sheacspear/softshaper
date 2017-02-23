package ru.softshaper.web.admin.bean.objlist;

import java.util.List;

public interface IObjectRowView {

  /**
   * @return the id
   */
  String getId();

  /**
   * @return the data
   */
  List<Object> getData();

  String getTitle();

}