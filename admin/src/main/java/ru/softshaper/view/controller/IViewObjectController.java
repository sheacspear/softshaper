package ru.softshaper.view.controller;

import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.view.bean.obj.impl.FullObjectView;
import ru.softshaper.view.bean.obj.impl.TitleObjectView;
import ru.softshaper.view.bean.objlist.IColumnView;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.bean.objlist.ITableObjectsView;

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
  <T> ITableObjectsView convertTableObjects(Collection<T> objList, String contentCode, Integer cnt,ObjectExtractor<T> objectExtractor);

  /**
   * @param objList
   * @param contentCode
   * @param total
   * @param backLinkAttr
   * @return
   */
  <T> IListObjectsView convertListObjects(Collection<T> objList, String contentCode, Integer total,ObjectExtractor<T> objectExtractor);

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
  Map<MetaField, IColumnView> constructColumnsView(MetaClass metaClass);

  DataSourceFromView getDataSourceFromView(String contentCode);

  Map<String, Object> parseAttrsFromView(MetaClass metaClass, Map<String, Object> viewAttrs);
}
