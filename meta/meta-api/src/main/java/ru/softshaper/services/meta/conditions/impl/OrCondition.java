package ru.softshaper.services.meta.conditions.impl;

import com.google.common.base.Preconditions;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.Condition;
import ru.softshaper.services.meta.conditions.ConvertConditionVisitor;

/**
 * Created by arostov on 30.08.2016.
 */
public class OrCondition extends AbstractCondition {

  private final Condition firstCondition;
  private final Condition secondCondition;

  public OrCondition(Condition firstCondition, Condition secondCondition) {
    Preconditions.checkNotNull(firstCondition);
    Preconditions.checkNotNull(secondCondition);
    this.firstCondition = firstCondition;
    this.secondCondition = secondCondition;
  }

  public Condition getFirstCondition() {
    return firstCondition;
  }

  public Condition getSecondCondition() {
    return secondCondition;
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
