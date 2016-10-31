package ru.zorb.services.meta.jooq;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.*;
import ru.zorb.services.meta.conditions.ConvertConditionVisitor;
import ru.zorb.services.meta.conditions.impl.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 14.09.2016.
 */
@Component
@Qualifier("jooq")
public class ConvertConditionToJooqVisitor implements ConvertConditionVisitor<Condition> {

  @Autowired
  private DataSourceStorage dataSourceStorage;

  @Override
  public Condition convert(AndCondition condition) {
    return condition.getFirstCondition().convert(this).and(condition.getSecondCondition().convert(this));
  }

  @Override
  public Condition convert(BetweenValueCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public Condition convert(CompareValueCondition condition) {
    Object value = condition.getValue();
    MetaField field = condition.getField();
    if (field.getType().equals(FieldType.LINK)) {
      MetaClass linkToMetaClass = field.getLinkToMetaClass();
      if (linkToMetaClass != null) {
        ContentDataSource<?> linkedDataSource = dataSourceStorage.get(linkToMetaClass);
        Class<?> idType = linkedDataSource.getIdType(linkToMetaClass.getCode());
        if (value != null) {
          if (!idType.equals(value.getClass())) {
            if (idType.equals(Long.class)) {
              value = Long.valueOf(value.toString());
            } else if (idType.equals(Integer.class)) {
              value = Integer.valueOf(value.toString());
            } else if (idType.equals(String.class)) {
              value = value.toString();
            }
          }
        } else {
          value = DSL.castNull(idType);
        }
      }
    }
    CompareOperation compareOperation = condition.getCompareOperation();
    if (field.getType().equals(FieldType.MULTILINK)) {
      //todo: подумать, может получение ролей по пользователю, стоит вынести в какой то кэш
      ContentDataSource<?> dataSource = dataSourceStorage.get(field.getOwner());
      Collection<String> objectsIds = dataSource.getObjectsIdsByMultifield(field.getOwner().getCode(), field.getCode(), value.toString(), true);
      if (objectsIds == null || objectsIds.isEmpty()) {
        return DSL.field(field.getOwner().getIdColumn()).equal(0);
      }
      return DSL.field(field.getOwner().getIdColumn()).in(objectsIds.stream().map(Long::valueOf).collect(Collectors.toSet()));
    } else {
      Field<Object> jooqField = DSL.field(field.getColumn());
      switch (compareOperation) {
        case EQUAL:
          return jooqField.eq(value);
        case GREAT:
          return jooqField.greaterThan(value);
        case LESS:
          return jooqField.lessThan(value);
        case IN:
          return jooqField.in(value);
        case LIKE:
          return jooqField.like("%" + value + "%");
        case END_WITH:
          return jooqField.like("%" + value);
        case START_WITH:
          return jooqField.like(value + "%");
      }
    }
    throw new RuntimeException("Undefined compare operation " + compareOperation);
  }

  @Override
  public Condition convert(InCondition condition) {
    throw new RuntimeException("Not implemented yet!");
  }

  @Override
  public Condition convert(NotCondition condition) {
    return condition.convert(this).not();
  }

  @Override
  public Condition convert(OrCondition condition) {
    return condition.getFirstCondition().convert(this).or(condition.getSecondCondition().convert(this));
  }
}
