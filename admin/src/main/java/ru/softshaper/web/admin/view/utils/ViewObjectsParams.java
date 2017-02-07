package ru.softshaper.web.admin.view.utils;

import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.impl.GetObjectsParams;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class ViewObjectsParams {
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

  public FieldCollection getFieldCollection() {
    return fieldCollection;
  }
}
