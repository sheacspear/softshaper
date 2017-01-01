package ru.softshaper.web.view;

import ru.softshaper.web.bean.obj.impl.FullObjectView;
import ru.softshaper.web.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.bean.objlist.ListObjectsView;
import ru.softshaper.web.bean.objlist.TableObjectsView;

import java.util.Collection;
import java.util.Map;

/**
 * Mapper Data to FormBean
 *
 * @author ashek
 *
 * @param <T>
 */
public interface DataViewMapper<T> {

  /**
   * Оборачивает объект в бин представления
   *
   * @param obj Data
   * @param contentCode
   * @return FormBean
   */
  FullObjectView convertFullObject(T obj, String contentCode);

  /**
   * @param obj
   * @param contentCode
   * @return
   */
  TitleObjectView convertTitleObject(T obj, String contentCode);

  /**
   * Оборачивает список объектов в бин представления
   *
   * @param objList dataBeans
   * @param contentCode
   * @return FormListBean
   */
  TableObjectsView convertTableObjects(Collection<T> objList, String contentCode, Integer cnt, String backLinkAttr);

  /**
   * @param objList
   * @param contentCode
   * @param total
   * @param backLinkAttr
   * @return
   */
  ListObjectsView convertListObjects(Collection<T> objList, String contentCode, Integer total, String backLinkAttr);

  /**
   * Создаёт болванку с описанием полей класса
   *
   * @param contentCode
   * @return FormBean
   */
  FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue);

}
