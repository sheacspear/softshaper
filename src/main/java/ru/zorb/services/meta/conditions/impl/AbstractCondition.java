package ru.zorb.services.meta.conditions.impl;

import ru.zorb.services.meta.conditions.Condition;

/**
 * Created by arostov on 30.08.2016.
 */
abstract class AbstractCondition implements Condition {

  public Condition and(Condition otherCondition) {
    return new AndCondition(this, otherCondition);
  }

  public Condition or(Condition otherCondition) {
    return new OrCondition(this, otherCondition);
  }

  public Condition not() {
    return new NotCondition(this);
  }
}
