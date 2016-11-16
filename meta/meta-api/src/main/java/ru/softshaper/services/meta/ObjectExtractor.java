package ru.softshaper.services.meta;

/**
 * Служит для возвращения значений объекта по его метоописанию
 */
public interface ObjectExtractor<T> {

  /**
   * Возвращает идентификатор объекта
   *
   * @param obj объект
   * @param metaClass мета-класс объекта
   * @return идентификатор
   */
  String getId(T obj, MetaClass metaClass);

  /**
   * Возвращает значение поля объекта
   *
   * @param obj объект
   * @param field поле
   * @return значение
   */
  Object getValue(T obj, MetaField field);


  /**
   * Описывает метод возвращающий значение объекта
   *
   * @param <T> класс объекта
   * @param <V> класс возвращаемого значения
   */
  @FunctionalInterface
  interface FieldExtractor<T, V> {
    /**
     * Возвращает значение объекта
     *
     * @param from объект
     * @return значение
     */
    V value(T from);
  }
}
