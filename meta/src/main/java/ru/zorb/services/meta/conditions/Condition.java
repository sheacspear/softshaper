package ru.zorb.services.meta.conditions;

/**
 * Created by arostov on 30.08.2016.
 */
public interface Condition {

  Condition and(Condition otherCondition);

  Condition or(Condition otherCondition);

  Condition not();

  <T> T convert(ConvertConditionVisitor<T> converter);

  boolean check(CheckConditionVisitor checker);

}
