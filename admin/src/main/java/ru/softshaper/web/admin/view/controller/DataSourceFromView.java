package ru.softshaper.web.admin.view.controller;

import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.web.admin.bean.obj.IFullObjectView;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.objlist.IListObjectsView;
import ru.softshaper.web.admin.bean.objlist.ITableObjectsView;
import java.util.Collection;

/**
 * Источник данных для формы
 *
 * @author asheknew
 *
 */
public interface DataSourceFromView {

  /**
   * получение идентификаторов объекта связаного через МкН
   *
   * @param contentCode код мета класса
   * @param multyfieldCode код МкН поля
   * @param id идентификатор текущего объекта
   * @return список ссылочных идентификаторов
   */
  Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse);

  /**
   * @param contentCode
   * @param backLinkAttr
   * @param objId
   * @return
   */
  IFullObjectView getNewObject(String contentCode, String backLinkAttr, String objId);

  /**
   * @param params
   * @return
   */
  ITableObjectsView getTableObjects(final ViewObjectsParams params);

  /**
   * @param params
   * @return
   */
  IListObjectsView getListObjects(final ViewObjectsParams params);

  /**
   * @param params
   * @return
   */
  IFullObjectView getFullObject(final ViewObjectsParams params);

  /**
   * @param params
   * @return
   */
  IObjectView getTitleObject(final ViewObjectsParams params);
}
