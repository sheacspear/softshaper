package ru.softshaper.services.meta.conditions.impl;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.conditions.ConditionField;
import ru.softshaper.services.meta.conditions.ConditionManager;

/**
 * Created by arostov on 30.08.2016.
 */
@Component
public class ConditionManagerImpl implements ConditionManager {

  private final MetaStorage metaStorage;

  @Autowired
  public ConditionManagerImpl(MetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Override public <T> ConditionField<T> field(MetaField metaField) {
    return new ConditionFieldImpl<>(metaField);
  }

  @Override public <T> ConditionField<T> field(String metaClassCode, String metaFieldCode) {
    Preconditions.checkNotNull(metaClassCode);
    Preconditions.checkNotNull(metaFieldCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    MetaField field = metaClass.getField(metaFieldCode);
    Preconditions.checkNotNull(field);
    return new ConditionFieldImpl<>(field);
  }

}
