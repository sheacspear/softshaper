package ru.softshaper.services.meta.conditions;

import ru.softshaper.services.meta.conditions.impl.*;

/**
 * Created by Sunchise on 14.09.2016.
 */
public interface ConvertConditionVisitor<T> {

  T convert(AndCondition condition);

  T convert(BetweenValueCondition condition);

  T convert(CompareValueCondition condition);

  T convert(InCondition condition);

  T convert(NotCondition condition);

  T convert(OrCondition condition);

}
