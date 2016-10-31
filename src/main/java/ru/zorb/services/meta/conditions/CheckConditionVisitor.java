package ru.zorb.services.meta.conditions;

import ru.zorb.services.meta.conditions.impl.*;

/**
 * Created by Sunchise on 14.09.2016.
 */
public interface CheckConditionVisitor {

  boolean check(AndCondition condition);

  boolean check(BetweenValueCondition condition);

  boolean check(CompareValueCondition condition);

  boolean check(InCondition condition);

  boolean check(NotCondition condition);

  boolean check(OrCondition condition);

}
