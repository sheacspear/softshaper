package ru.softshaper.web.bean.obj;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.softshaper.web.view.IViewSetting;

public interface IFullObjectView extends IObjectView {

  List<IFieldView> getFields();

  Map<String, IViewSetting> getViewSettig();

  Map<String, ?> getValues();

  Map<String, Collection<IObjectView>> getVariants();

}
