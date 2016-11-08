package ru.softshaper.services.meta.conditions.impl;

import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.Condition;
import ru.softshaper.services.meta.conditions.ConvertConditionVisitor;

/**
 * Created by arostov on 30.08.2016.
 */
public class NotCondition extends AbstractCondition {

  private final Condition condition;

  public NotCondition(Condition condition) {
    this.condition = condition;
  }

  @Override
  public <T> T convert(ConvertConditionVisitor<T> converter) {
    return converter.convert(this);
  }

  @Override
  public boolean check(CheckConditionVisitor checker) {
    return checker.check(this);
  }
}
