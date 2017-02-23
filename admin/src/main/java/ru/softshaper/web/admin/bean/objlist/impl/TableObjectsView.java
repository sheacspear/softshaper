package ru.softshaper.web.admin.bean.objlist.impl;

import java.util.Collection;

import ru.softshaper.web.admin.bean.objlist.IColumnView;
import ru.softshaper.web.admin.bean.objlist.IObjectRowView;
import ru.softshaper.web.admin.bean.objlist.ITableObjectsView;

/**
 *
 */
public class TableObjectsView implements ITableObjectsView  {

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
  private final Collection<IColumnView> columnsView;

  /**
   *
   */
  private final Collection<IObjectRowView> objectsView;

  public TableObjectsView(String clazz, int cnt, Collection<IColumnView> columnsView, Collection<IObjectRowView> objectsView) {
    super();
    this.clazz = clazz;
    this.cnt = cnt;
    this.columnsView = columnsView;
    this.objectsView = objectsView;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.ITableObjectsView#getClazz()
   */
  @Override
  public String getClazz() {
    return clazz;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.ITableObjectsView#getCnt()
   */
  @Override
  public int getCnt() {
    return cnt;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.ITableObjectsView#getColumnsView()
   */
  @Override
  public Collection<IColumnView> getColumnsView() {
    return columnsView;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.ITableObjectsView#getObjectsView()
   */
  @Override
  public Collection<IObjectRowView> getObjectsView() {
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
