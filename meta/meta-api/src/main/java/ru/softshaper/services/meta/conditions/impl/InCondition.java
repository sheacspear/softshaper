package ru.softshaper.services.meta.conditions.impl;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.ConvertConditionVisitor;

/**
 * Created by arostov on 30.08.2016.
 */
public class InCondition<T> extends AbstractCondition {

  private final MetaField field;

  private final T values;

  public InCondition(MetaField field, T values) {
    this.field = field;
    this.values = values;
  }

  @Override
  public <C> C convert(ConvertConditionVisitor<C> converter) {
    return converter.convert(this);
  }

  @Override
  public boolean check(CheckConditionVisitor checker) {
    return checker.check(this);
  }
}
