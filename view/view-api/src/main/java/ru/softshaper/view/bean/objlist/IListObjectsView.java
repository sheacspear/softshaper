package ru.softshaper.view.bean.objlist;

import java.util.Collection;

import ru.softshaper.view.bean.obj.IObjectView;

public interface IListObjectsView {

  int getTotal();

  Collection<IObjectView> getObjects();

}