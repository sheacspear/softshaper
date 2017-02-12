package ru.softshaper.web.admin.bean.objlist;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class TableObjectsView implements ObjectCollectionView {

  /**
   *
   */
  private final String clazz;

  /**
  *
  */
  private final int cnt;

  /**
   *
   */
  private final Collection<ColumnView> columnsView;

  /**
   *
   */
  private final Collection<ObjectRowView> objectsView;

  public TableObjectsView(String clazz, int cnt, Collection<ColumnView> columnsView, Collection<ObjectRowView> objectsView) {
    super();
    this.clazz = clazz;
    this.cnt = cnt;
    this.columnsView = columnsView;
    this.objectsView = objectsView;
  }

  /**
   * @return the clazz
   */
  public String getClazz() {
    return clazz;
  }

  /**
   * @return the cnt
   */
  public int getCnt() {
    return cnt;
  }

  /**
   * @return the columnsView
   */
  public Collection<ColumnView> getColumnsView() {
    return columnsView;
  }

  /**
   * @return the objectsView
   */
  public Collection<ObjectRowView> getObjectsView() {
    return objectsView;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ListObjectBean \n[clazz=" + clazz + ", \ncnt=" + cnt + ", \ncolumnsView=" + columnsView + ", \nobjectsView="
        + objectsView + "]";
  }
}
