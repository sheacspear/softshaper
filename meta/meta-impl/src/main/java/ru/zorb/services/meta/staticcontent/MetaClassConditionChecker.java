package ru.zorb.services.meta.staticcontent;

import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.conditions.CheckConditionVisitor;
import ru.zorb.services.meta.conditions.impl.*;
import ru.zorb.services.meta.staticcontent.meta.MetaClassStaticContent;

/**
 * Created by Sunchise on 14.09.2016.
 */
public class MetaClassConditionChecker implements CheckConditionVisitor {

  private final MetaClass metaClass;

  public MetaClassConditionChecker(MetaClass metaClass) {
    this.metaClass = metaClass;
  }

  @Override
  public boolean check(AndCondition condition) {
    return condition.getFirstCondition().check(this) && condition.getSecondCondition().check(this);
  }

  @Override
  public boolean check(BetweenValueCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public boolean check(CompareValueCondition condition) {
    switch (condition.getField().getCode()) {
      case MetaClassStaticContent.Field.checkObjectSecurity:
        boolean value = metaClass.isCheckObjectSecurity();
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return value == (Boolean) condition.getValue();
        }
        throw new RuntimeException("Not supported!");
      case MetaClassStaticContent.Field.checkSecurity:
        value = metaClass.isCheckObjectSecurity();
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return value == (Boolean) condition.getValue();
        }
        throw new RuntimeException("Not supported!");
      case MetaClassStaticContent.Field.fixed:
        value = metaClass.isFixed();
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return value == (Boolean) condition.getValue();
        }
        throw new RuntimeException("Not supported!");
      case MetaClassStaticContent.Field.code:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaClass.getCode().equals(condition.getValue().toString());
          case LESS:
            return metaClass.getCode().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaClass.getCode().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
      case MetaClassStaticContent.Field.name:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaClass.getName().equals(condition.getValue().toString());
          case LESS:
            return metaClass.getName().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaClass.getName().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
      case MetaClassStaticContent.Field.table:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaClass.getTable().equals(condition.getValue().toString());
          case LESS:
            return metaClass.getTable().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaClass.getTable().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
    }
    throw new RuntimeException("Not supported for field " + condition.getField().getCode());
  }

  @Override
  public boolean check(InCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public boolean check(NotCondition condition) {
    return !condition.check(this);
  }

  @Override
  public boolean check(OrCondition condition) {
    return condition.getFirstCondition().check(this) || condition.getSecondCondition().check(this);
  }
}
