package ru.softshaper.datasource.meta;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.ObjectComparator;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.meta.comparators.DefaultObjectComparator;
import ru.softshaper.staticcontent.meta.conditions.DefaultConditionChecker;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Болванка для кастомного датасурса
 */
public abstract class AbstractCustomDataSource<T> implements ContentDataSource<T> {

  /**
   * Сравниватель объектов
   */
  private final ObjectComparator<T> objectComparator;

  /**
   * Вычленитель значений полей для объекта
   */
  private final ObjectExtractor<T> objectExtractor;

  /**
   * Конструктор
   *
   * @param objectComparator Сравниватель объектов
   * @param objectExtractor Вычленитель значений полей для объекта
   */
  public AbstractCustomDataSource(ObjectComparator<T> objectComparator, ObjectExtractor<T> objectExtractor) {
    this.objectComparator = objectComparator;
    this.objectExtractor = objectExtractor;
  }

  /**
   *
   * @return Вычленитель значений полей для объекта
   */
  protected ObjectExtractor<T> getObjectExtractor() {
    return objectExtractor;
  }

  /**
   * Конструктор с компаратором по умолчанию
   *
   * @param objectExtractor Вычленитель значений полей для объекта
   */
  public AbstractCustomDataSource(ObjectExtractor<T> objectExtractor) {
    this(new DefaultObjectComparator<>(objectExtractor), objectExtractor);
  }

  @Override
  public T getObj(GetObjectsParams params) {
    Collection<T> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  public Collection<T> getObjects(GetObjectsParams params) {
    Collection<T> objects = getAllObjects(params);
    if (objects == null) {
      return null;
    }
    Stream<T> stream = objects.stream();
    stream = filterByIds(params, stream);
    stream = filterByConditions(params, stream);
    stream = order(params, stream);
    stream = limitOffset(params, stream);
    return stream.collect(Collectors.toList());
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }


  /**
   * Накладывает ограничение кол-ва возвращаемых объектов и отступ
   *
   * @param params парамсы
   * @param stream фильтры
   * @return изменённый фильтр
   */
  protected Stream<T> limitOffset(GetObjectsParams params, Stream<T> stream) {
    if (params.getLimit() < Integer.MAX_VALUE || params.getOffset() > 0) {
          stream = stream.skip(params.getOffset())
          .limit(params.getLimit());
    }
    return stream;
  }

  /**
   * Сортировка объектов
   *
   * @param params парамсы
   * @param stream фильры
   * @return изменённый фильтр
   */
  protected Stream<T> order(GetObjectsParams params, Stream<T> stream) {
    LinkedHashMap<MetaField, ru.softshaper.services.meta.impl.SortOrder> orderFields = params.getOrderFields();
    if (orderFields != null) {
      stream = stream.sorted((o1, o2) -> {
        int compareResult;
        if (o1 == null) {
          compareResult = o2 == null ? 0 : -1;
        } else if (o2 == null) {
          compareResult = 1;
        } else {
          compareResult = 0;
          for (Map.Entry<MetaField, ru.softshaper.services.meta.impl.SortOrder> order : orderFields.entrySet()) {
            compareResult = objectComparator.compareField(order.getKey(), o1, o2);
            if (compareResult != 0) {
              compareResult = ru.softshaper.services.meta.impl.SortOrder.DESC.equals(order.getValue())
                  ? compareResult * -1
                  : compareResult;
              break;
            }
          }
        }
        return compareResult;
      });
    }
    return stream;
  }

  /**
   * Накладывает условия выборки
   *
   * @param params парамсы
   * @param stream фильтры
   * @return изменённые фильтры
   */
  protected Stream<T> filterByConditions(GetObjectsParams params, Stream<T> stream) {
    ru.softshaper.services.meta.conditions.Condition condition = params.getCondition();
    if (condition != null) {
      stream = stream.filter(object -> condition.check(newCheckCondition(object)));
    }
    return stream;
  }

  /**
   * Фильтрует по идентификаторам
   *
   * @param params парамсы
   * @param stream фильтры
   * @return изменённые фильтры
   */
  protected Stream<T> filterByIds(GetObjectsParams params, Stream<T> stream) {
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      stream = stream.filter(object -> params.getIds().contains(objectExtractor.getId(object, params.getMetaClass())));
    }
    return stream;
  }

  /**
   * Чекер для условий
   *
   * @param object объект
   * @return чекер
   */
  protected CheckConditionVisitor newCheckCondition(T object) {
    return new DefaultConditionChecker<T>(object, objectExtractor);
  }

  /**
   * Возвращает список объектов которые будут фильтроваться по условиям вдальнейшем
   *
   * @param params парамсы
   * @return коллекция объектов
   */
  protected abstract Collection<T> getAllObjects(GetObjectsParams params);
}
