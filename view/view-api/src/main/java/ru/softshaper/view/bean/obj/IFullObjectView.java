package ru.softshaper.view.bean.obj;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.softshaper.view.viewsettings.ViewSetting;

public interface IFullObjectView extends IObjectView {

  List<IFieldView> getFields();
  
  List<IUtilView> getUtils();  

  Map<String, ViewSetting> getViewSettig();

  Map<String, ?> getValues();

  Map<String, Collection<IObjectView>> getVariants();

}
