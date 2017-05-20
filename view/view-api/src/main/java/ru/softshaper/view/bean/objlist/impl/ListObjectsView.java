package ru.softshaper.view.bean.objlist.impl;

import java.util.Collection;

import ru.softshaper.view.bean.obj.IObjectView;
import ru.softshaper.view.bean.objlist.IListObjectsView;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class ListObjectsView implements IListObjectsView  {

  private final String classCode;
  private final int total;

  private final Collection<IObjectView> objects;

  public ListObjectsView(String classCode, int total, Collection<IObjectView> objects) {
    super();
    this.classCode = classCode;
    this.total = total;
    this.objects = objects;
  }

  public String getClassCode() {
    return classCode;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IListObjectsView#getTotal()
   */
  @Override
  public int getTotal() {
    return total;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.admin.bean.objlist.impl.IListObjectsView#getObjects()
   */
  @Override
  public Collection<IObjectView> getObjects() {
    return objects;
  }
}
