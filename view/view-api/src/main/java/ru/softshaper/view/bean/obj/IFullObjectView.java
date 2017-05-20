package ru.softshaper.view.bean.obj;

import ru.softshaper.view.viewsettings.ViewSetting;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IFullObjectView extends IObjectView {

  List<IFieldView> getFields();

  Map<String, ViewSetting> getViewSettig();

  Map<String, ?> getValues();

  Map<String, Collection<IObjectView>> getVariants();

}
