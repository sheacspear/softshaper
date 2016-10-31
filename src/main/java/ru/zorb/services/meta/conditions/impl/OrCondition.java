package ru.zorb.services.meta.conditions.impl;

import com.google.common.base.Preconditions;
import ru.zorb.services.meta.conditions.CheckConditionVisitor;
import ru.zorb.services.meta.conditions.Condition;
import ru.zorb.services.meta.conditions.ConvertConditionVisitor;

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
    return null;
  }

  @Override
  public boolean check(CheckConditionVisitor checker) {
    return checker.check(this);
  }
}
