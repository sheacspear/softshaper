package ru.softshaper.view.viewsettings;

import ru.softshaper.services.meta.MetaField;

import java.util.Map;

/**
 * Created by Sunchise on 21.02.2017.
 */
public interface ViewObject {

  String getTitle();

  Map<MetaField, Object> getOrderedFieldsValues();

  String getId();

}
