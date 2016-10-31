package ru.zorb.web.view.utils;

import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.impl.GetObjectsParams;

/**
 * Created by Sunchise on 29.09.2016.
 */
public class ViewObjectsParams {

  public static ViewObjectParamsBuilder newBuilder(MetaClass metaClass) {
    return new ViewObjectParamsBuilder(metaClass);
  }

  private final GetObjectsParams params;

  private final FieldCollection fieldCollection;

  ViewObjectsParams(GetObjectsParams params, FieldCollection fieldCollection) {
    this.params = params;
    this.fieldCollection = fieldCollection;
  }

  public GetObjectsParams getParams() {
    return params;
  }

  public FieldCollection getFieldCollection() {
    return fieldCollection;
  }
}
