package ru.softshaper.web.admin.bean.objlist;

import java.util.Collection;

import ru.softshaper.web.admin.bean.obj.IObjectView;

public interface IListObjectsView {

  int getTotal();

  Collection<IObjectView> getObjects();

}