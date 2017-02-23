package ru.softshaper.web.admin.bean.objlist.impl;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.web.admin.bean.objlist.IColumnView;

/**
 *
 */
public class ColumnView implements IColumnView {

  /**
   * title column
   */
  private final String name;

  /**
   * code field column
   */
  private final String key;

  private final boolean sortable;

  private final boolean filterable = true;

  /**
   * typeView field column
   */
  private final FieldTypeView typeView;

  /**
   * @param name
   * @param key
   * @param typeView
   */
  public ColumnView(String name, String key, FieldTypeView typeView) {
    super();
    this.name = name;
    this.key = key;
    this.typeView = typeView;
    this.sortable = true;
  }

  public ColumnView(String name, String key, FieldTypeView typeView,boolean sortable) {
    super();
    this.name = name;
    this.key = key;
    this.typeView = typeView;
    this.sortable = sortable;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IColumnView#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IColumnView#getKey()
   */
  @Override
  public String getKey() {
    return key;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IColumnView#getTypeView()
   */
  @Override
  public FieldTypeView getTypeView() {
    return typeView;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ColumnView [name=" + name + ", key=" + key + ", typeView=" + typeView + "]";
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IColumnView#isSortable()
   */
  @Override
  public boolean isSortable() {
    return sortable;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IColumnView#isFilterable()
   */
  @Override
  public boolean isFilterable() {
    return filterable;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ColumnView other = (ColumnView) obj;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    return true;
  }


}
