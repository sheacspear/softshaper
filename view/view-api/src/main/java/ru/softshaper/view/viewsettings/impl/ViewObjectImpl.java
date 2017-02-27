package ru.softshaper.view.viewsettings.impl;

import ru.softshaper.services.meta.MetaField;
import ru.softshaper.view.viewsettings.ViewObject;

import java.util.Map;

/**
 * Created by Sunchise on 21.02.2017.
 */
public class ViewObjectImpl implements ViewObject {

  private final String title;
  private final Map<String, Object> values;
  private final String id;

  public ViewObjectImpl(String id, String title, Map<String, Object> values) {
    this.id = id;
    this.title = title;
    this.values = values;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Map<MetaField, Object> getOrderedFieldsValues() {
    return null;
  }

  //@Override
  public Object getValue(String fieldCode) {
    return values.get(fieldCode);
  }
}
