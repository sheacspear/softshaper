package ru.softshaper.datasource.meta;

import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;

import java.util.Collection;
import java.util.Map;

/**
 * DataSource<br/>
 * Created by Sunchise on 16.08.2016.
 */
public interface ContentDataSource<T> {

  Collection<T> getObjects(final GetObjectsParams params);

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
   * Инит
   *
   * @param metaInitializer
   */
  //todo: убрать этот метод от сюда
  void setMetaInitializer(MetaInitializer metaInitializer);

  /**
   * @param params
   * @return
   */
  T getObj(final GetObjectsParams params);

  /**
   * Создание объекта
   *
   * @param contentCode код
   * @param values
   * @return
   */
  String createObject(String contentCode, Map<String, Object> values);

  /**
   * Обновление объекта
   *
   * @param String
   * @param values
   */
  void updateObject(String contentCode, String String, Map<String, Object> values);

  /**
   * Удаление объекта
   *
   * @param id
   */
  void deleteObject(String contentCode, String id);

  /**
   * Кол-во объектов
   *
   * @param contentCode код
   * @return
   */
  Integer getCntObjList(String contentCode);

  /**
   * @param metaClassCode
   * @return
   */
  Class<?> getIdType(String metaClassCode);
  
  /**
   * @return
   */
  ObjectExtractor<T> getObjectExtractor();
 }
