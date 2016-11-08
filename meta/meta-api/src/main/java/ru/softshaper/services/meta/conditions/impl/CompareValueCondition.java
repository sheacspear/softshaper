package ru.softshaper.services.meta.conditions.impl;

import com.google.common.base.Preconditions;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.ConvertConditionVisitor;

/**
 * Created by arostov on 30.08.2016.
 */
public class CompareValueCondition<T> extends AbstractCondition {

  private final MetaField field;

  private final T value;

  private final CompareOperation compareOperation;

  public CompareValueCondition(MetaField field, T value, CompareOperation compareOperation) {
    Preconditions.checkNotNull(field);
    Preconditions.checkNotNull(compareOperation);
    this.field = field;
    this.value = value;
    this.compareOperation = compareOperation;
  }

  public MetaField getField() {
    return field;
  }

  public T getValue() {
    return value;
  }

  public CompareOperation getCompareOperation() {
    return compareOperation;
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
