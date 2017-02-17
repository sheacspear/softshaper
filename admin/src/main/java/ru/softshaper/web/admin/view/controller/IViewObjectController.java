package ru.softshaper.web.admin.view.controller;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.bean.obj.impl.FullObjectView;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.objlist.ColumnView;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.TableObjectsView;

import java.util.Collection;
import java.util.Map;

/**
 * Mapper Data to FormBean
 *
 * @author ashek
 */
public interface IViewObjectController {

  /**
   * @param fieldType
   * @param viewAttrController
   */
  <T> void registerAttrController(FieldType fieldType, IViewAttrController viewAttrController);

  /**
   * @param viewAttrController
   */
  <T> void setDefaultAttrController(IViewAttrController viewAttrController);

  /**
   * Оборачивает объект в бин представления
   *
   * @param obj Data
   * @param contentCode
   * @return FormBean
   */
  <T> FullObjectView convertFullObject(T obj, String contentCode,ObjectExtractor<T> objectExtractor);

  /**
   * @param obj
   * @param contentCode
   * @return
   */
  <T> TitleObjectView convertTitleObject(T obj, String contentCode,ObjectExtractor<T> objectExtractor);

  /**
   * Оборачивает список объектов в бин представления
   *
   * @param objList dataBeans
   * @param contentCode
   * @return FormListBean
   */
  <T> TableObjectsView convertTableObjects(Collection<T> objList, String contentCode, Integer cnt,ObjectExtractor<T> objectExtractor);

  /**
   * @param objList
   * @param contentCode
   * @param total
   * @param backLinkAttr
   * @return
   */
  <T> ListObjectsView convertListObjects(Collection<T> objList, String contentCode, Integer total,ObjectExtractor<T> objectExtractor);

  /**
   * Создаёт болванку с описанием полей класса
   *
   * @param contentCode
   * @return FormBean
   */
  <T> FullObjectView getEmptyObj(String contentCode, Map<String, Object> defValue,ObjectExtractor<T> objectExtractor);

  /**
   * @param metaClass
   * @return
   */
  Map<MetaField, ColumnView> constructColumnsView(MetaClass metaClass);

  DataSourceFromView getDataSourceFromView(String contentCode);

}
