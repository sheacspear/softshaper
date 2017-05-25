package ru.softshaper.services.meta;

/**
 * Определяет как сравнивать объекты мета-класса
 */
public interface ObjectComparator<T> {

  /**
   * Сравнивает 2 объекта по указаному полю
   *
   * @param metaField поле, значение которого будут сравнивать
   * @param o1 объект 1
   * @param o2 объект 2
   * @return -1 если первый объект меньше, 0 - если равны, 1 если первый объект больше
   */
  int compareField(MetaField metaField, T o1, T o2);
}
