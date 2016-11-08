package ru.softshaper.services.meta.staticcontent;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.conditions.impl.*;
import ru.softshaper.services.meta.staticcontent.meta.MetaFieldStaticContent;

/**
 * Created by Sunchise on 14.09.2016.
 */
public class MetaFieldConditionChecker implements CheckConditionVisitor {

  private final MetaField metaField;

  public MetaFieldConditionChecker(MetaField metaField) {
    this.metaField = metaField;
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
      case MetaFieldStaticContent.Field.code:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getCode().equals(condition.getValue());
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.name:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField != null && metaField.getName().equals(condition.getValue());
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.owner:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getOwner().getId().equals(condition.getValue());
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.type:
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getType().getId().toString().equals(condition.getValue());
          case LESS:
            return metaField.getType().getId().toString().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaField.getType().getId().toString().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.column:
        if (metaField.getColumn() == null) {
          return false;
        }
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getColumn().equals(condition.getValue().toString());
          case LESS:
            return metaField.getColumn().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaField.getColumn().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.linkToMetaClass:
        if (metaField.getLinkToMetaClass() == null) {
          return false;
        }
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getLinkToMetaClass().getId().equals(condition.getValue().toString());
          case LESS:
            return metaField.getLinkToMetaClass().getId().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaField.getLinkToMetaClass().getId().compareTo(condition.getValue().toString()) == 1;
        }
        throw new RuntimeException("Not supported!");
      case MetaFieldStaticContent.Field.backReferenceField:
        if (metaField.getBackReferenceField() == null) {
          return false;
        }
        switch (condition.getCompareOperation()) {
          case EQUAL:
            return metaField.getBackReferenceField().getId().equals(condition.getValue().toString());
          case LESS:
            return metaField.getBackReferenceField().getId().compareTo(condition.getValue().toString()) == -1;
          case GREAT:
            return metaField.getBackReferenceField().getId().compareTo(condition.getValue().toString()) == 1;
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
