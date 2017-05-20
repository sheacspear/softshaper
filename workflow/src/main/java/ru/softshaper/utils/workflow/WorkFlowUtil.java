package ru.softshaper.utils.workflow;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.ResultUtil;
import ru.softshaper.services.utils.StepUtil;

/**
 *
 */
@Component
public class WorkFlowUtil implements IUtil {

  @Autowired
  private IUtilsEngine utilsEngine;

  @PostConstruct
  public void init() {
    utilsEngine.registerObjectUtil(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getName()
   */
  @Override
  public String getName() {
    return "WorkFlowUtil";
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getCode()
   */
  @Override
  public String getCode() {
    return "WorkFlowUtil";
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getNextStep(java.util.Map)
   */
  @Override
  public StepUtil getNextStep(Map<String, Object> data) {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#execute(java.util.Map)
   */
  @Override
  public ResultUtil execute(Map<String, Object> data) {
    // TODO Auto-generated method stub
    return null;
  }
}
