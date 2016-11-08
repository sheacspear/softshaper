package ru.softshaper.services.meta.conditions.impl;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.conditions.Condition;
import ru.softshaper.services.meta.conditions.ConditionField;

import java.util.Collection;

/**
 * Created by arostov on 30.08.2016.
 */
public class ConditionFieldImpl<T> implements ConditionField<T> {

  private final MetaField field;

  public ConditionFieldImpl(MetaField field) {
    this.field = field;
  }

  @Override
  public Condition equal(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.EQUAL);
  }

  @Override
  public Condition less(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.LESS);
  }

  @Override
  public Condition great(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.GREAT);
  }

  @Override
  public Condition between(T first, T last) {
    return new BetweenValueCondition<>(field, first, last);
  }

  @Override
  public Condition in(Collection<T> values) {
    return new InCondition<>(field, values);
  }

  @Override
  public Condition like(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.LIKE);
  }

  @Override
  public Condition startWith(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.START_WITH);
  }

  @Override
  public Condition endWith(T value) {
    return new CompareValueCondition<>(field, value, CompareOperation.END_WITH);
  }
}
