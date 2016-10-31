package ru.zorb.services.meta.conditions.impl;

import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.conditions.CheckConditionVisitor;
import ru.zorb.services.meta.conditions.ConvertConditionVisitor;

/**
 * Created by arostov on 30.08.2016.
 */
public class BetweenValueCondition<T> extends AbstractCondition {

  private final MetaField field;

  private final T firstValue;

  private final T secondValue;

  public BetweenValueCondition(MetaField field, T firstValue, T secondValue) {
    this.field = field;
    this.firstValue = firstValue;
    this.secondValue = secondValue;
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
