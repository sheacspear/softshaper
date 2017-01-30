package ru.softshaper.web.bean.objlist;

import java.util.Collection;

import ru.softshaper.web.bean.obj.impl.TitleObjectView;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class ListObjectsView implements ObjectCollectionView {

  private final String classCode;

  private final int total;

  private final Collection<TitleObjectView> objects;

  public ListObjectsView(String classCode, int total, Collection<TitleObjectView> objects) {
    super();
    this.classCode = classCode;
    this.total = total;
    this.objects = objects;
  }

  public String getClassCode() {
    return classCode;
  }

  public int getTotal() {
    return total;
  }

  public Collection<TitleObjectView> getObjects() {
    return objects;
  }
}
