package ru.softshaper.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.ResultUtil;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;

import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by arostov on 20.10.2016.
 */
@Component
public class SystemInitUtil implements IUtil {

  @Autowired
  private MetaInitializer metaInitializer;

  @Autowired
  private ViewSettingStore viewSettingFactory;

  @Autowired
  private IUtilsEngine utilsEngine;
  
  @PostConstruct
  public void init() {
    utilsEngine.registerUtils(this);
  }

  /* (non-Javadoc)
   * @see ru.softshaper.services.utils.IUtil#getName()
   */
  @Override
  public String getName() {
    return "SystemInitUtil";
  }

  /* (non-Javadoc)
   * @see ru.softshaper.services.utils.IUtil#getCode()
   */
  @Override
  public String getCode() {
    return "SystemInitUtil";
  }

  @Override
  public StepUtil getNextStep(Map<String, Object> data) {
    return null;
  }

  @Override
  public ResultUtil execute(Map<String, Object> data) {
    metaInitializer.init();
    viewSettingFactory.init();
    return null;
  }
}
