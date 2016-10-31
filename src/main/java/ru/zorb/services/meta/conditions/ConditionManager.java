package ru.zorb.services.meta.conditions;

import ru.zorb.services.meta.MetaField;

/**
 * Created by Sunchise on 13.09.2016.
 */
public interface ConditionManager {
  <T> ConditionField<T> field(MetaField metaField);

  <T> ConditionField<T> field(String metaClassCode, String metaFieldCode);
}
