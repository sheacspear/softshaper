package ru.zorb.services.meta.conditions;

import java.util.Collection;

/**
 * Created by arostov on 30.08.2016.
 */
public interface ConditionField<T> {

  Condition equal(T value);

  Condition less(T value);

  Condition great(T value);

  Condition between(T first, T last);

  Condition in(Collection<T> values);

  Condition like(T value);

  Condition startWith(T value);

  Condition endWith(T value);
}
