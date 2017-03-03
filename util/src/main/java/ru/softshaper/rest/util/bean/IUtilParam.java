package ru.softshaper.rest.util.bean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.softshaper.view.bean.obj.IFieldView;
import ru.softshaper.view.bean.obj.IObjectView;
import ru.softshaper.view.viewsettings.ViewSetting;

public interface IUtilParam {
  List<IFieldView> getFields();

  Map<String, ViewSetting> getViewSettig();

  Map<String, ?> getValues();

  Map<String, Collection<IObjectView>> getVariants();

}
