package ru.softshaper.view.params;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.impl.GetObjectsParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 29.09.2016.
 */
public final class ViewObjectsParams {

  private final Map<MetaField, Object> linkedFieldsValues = new HashMap<>();

  private final GetObjectsParams params;

  private final FieldCollection fieldCollection;

  ViewObjectsParams(GetObjectsParams params, FieldCollection fieldCollection) {
    this.params = params;
    this.fieldCollection = fieldCollection;
  }

  /**
   * @param metaClass
   * @return
   */
  public static ViewObjectParamsBuilder newBuilder(MetaClass metaClass) {
    return new ViewObjectParamsBuilder(metaClass);
  }

  public GetObjectsParams getParams() {
    return params;
  }

  public Map<MetaField, Object> getLinkedFieldsValues() {
    return linkedFieldsValues;
  }

  public FieldCollection getFieldCollection() {
    return fieldCollection;
  }

}
