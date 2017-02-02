package ru.softshaper.web.admin.bean.objlist;

import ru.softshaper.bean.meta.FieldTypeView;

/**
 *
 */
public class ColumnView {

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

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @return the typeView
   */
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

  /**
   * @return the sortable
   */
  public boolean isSortable() {
    return sortable;
  }

  /**
   * @return the filterable
   */
  public boolean isFilterable() {
    return filterable;
  }


}
