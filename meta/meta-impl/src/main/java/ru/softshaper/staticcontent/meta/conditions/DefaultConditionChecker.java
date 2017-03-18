package ru.softshaper.staticcontent.meta.conditions;

import com.google.common.base.Preconditions;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.impl.*;

/**
 * Универсальный проверяльщик условий
 */
public class DefaultConditionChecker<T> implements CheckConditionVisitor {

  /**
   * Объект
   */
  private final T object;

  /**
   * Экстрактор
   */
  private final ObjectExtractor<T> extractor;

  /**
   * Конструктор
   *
   * @param object объект
   * @param extractor экстрактор
   */
  public DefaultConditionChecker(T object, ObjectExtractor<T> extractor) {
    this.object = object;
    this.extractor = extractor;
  }

  @Override
  public boolean check(AndCondition condition) {
    return condition.getFirstCondition().check(this) && condition.getSecondCondition().check(this);
  }

  @Override
  public boolean check(BetweenValueCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public boolean check(CompareValueCondition condition) {
    Object value = extractor.getValue(object, condition.getField());
    switch (condition.getCompareOperation()) {
      case EQUAL:
        if (value == null) {
          return condition.getValue() == null;
        } else {
          return value.equals(condition.getValue());
        }
      case END_WITH:
        Preconditions.checkNotNull(condition.getValue());
        return value != null && value.toString().endsWith(condition.getValue().toString());
      case START_WITH:
        Preconditions.checkNotNull(condition.getValue());
        return value != null && value.toString().startsWith(condition.getValue().toString());
      case LIKE:
        Preconditions.checkNotNull(condition.getValue());
        return value != null && value.toString().contains(condition.getValue().toString());
      case GREAT:
        if (value == null) {
          return false;
        }
        Preconditions.checkNotNull(condition.getValue());
        if (value instanceof Number && condition.getValue() instanceof Number) {
          return compareNumbers((Number) value, (Number) condition.getValue()) == 1;
        }
        throw new RuntimeException("Not supported for field " + condition.getField().getCode());
      case LESS:
        if (value == null) {
          return false;
        }
        Preconditions.checkNotNull(condition.getValue());
        if (value instanceof Number && condition.getValue() instanceof Number) {
          return compareNumbers((Number) value, (Number) condition.getValue()) == 1;
        }
        throw new RuntimeException("Not supported for field " + condition.getField().getCode());
      case IN:
        throw new RuntimeException("Not implemented yet!");
    }
    throw new RuntimeException("Not supported for field " + condition.getField().getCode());
}

  /**
   * Сравниваем два числа
   *
   * @param n1 первое число
   * @param n2 второе число
   * @return -1 - меньше, 0 - равноб 1 - больше
   */
  private int compareNumbers(Number n1, Number n2) {
    if (n1 instanceof Comparable && n2 instanceof Comparable) {
      //noinspection unchecked
      return ((Comparable) n1).compareTo(n2);
    }
    int result = Long.compare(n1.longValue(), n2.longValue());
    return result == 0 ? Double.compare(n1.doubleValue(), n2.doubleValue()) : result;
  }

  @Override
  public boolean check(InCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public boolean check(NotCondition condition) {
    return !condition.check(this);
  }

  @Override
  public boolean check(OrCondition condition) {
    return condition.getFirstCondition().check(this) || condition.getSecondCondition().check(this);
  }
}
