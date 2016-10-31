package ru.zorb.web.view;

import ru.zorb.web.bean.obj.FullObjectView;
import ru.zorb.web.bean.obj.TitleObjectView;
import ru.zorb.web.bean.objlist.ListObjectsView;
import ru.zorb.web.bean.objlist.TableObjectsView;
import ru.zorb.web.view.utils.ViewObjectsParams;

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

  FullObjectView getNewObject(String contentCode, String backLinkAttr, String objId);

  TableObjectsView getTableObjects(final ViewObjectsParams params);

  ListObjectsView getListObjects(final ViewObjectsParams params);

  FullObjectView getFullObject(final ViewObjectsParams params);

  TitleObjectView getTitleObject(final ViewObjectsParams params);
}
