package ru.softshaper.services.utils.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.ResultUtil;
import ru.softshaper.services.utils.StepUtil;

@Service
public class UtilsEngine implements IUtilsEngine {

  /**
  *
  */
  private final Map<String, IUtil> utilsMap = Maps.newHashMap();

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.view.IViewObjectController#registerAttrController(
   * ru.softshaper.services.meta.FieldType,
   * ru.softshaper.view.IViewAttrController)
   */
  @Override
  public synchronized void registerUtils(IUtil util) {
    utilsMap.put(util.getCode(), util);
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.util.IUtilsEngine#getAvailableUtilsForToolbar()
   */
  @Override
  public Collection<IUtil> getAvailableUtilsForToolbar() {
    return Collections.emptyList();
  }

  @Override
  public Collection<IUtil> getAvailableUtilsForObject(String metaClazz, String objId) {
    Collection<IUtil> utils = utilsMap.values();
    // check security
    utils = checkSecurity(utils, metaClazz, objId);
    // check condition
    utils = checkCondition(utils, metaClazz, objId);
    return utils;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.services.util.IUtilsEngine#getUtilByCode(java.lang.String)
   */
  @Override
  public IUtil getUtilByCode(String utilCode) {
    return utilsMap.get(utilCode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.util.IUtilsEngine#getNextStep(java.lang.String,
   * java.util.Map)
   */
  @Override
  public StepUtil getNextStep(String utilCode, Map<String, Object> data) {
    IUtil util = this.getUtilByCode(utilCode);
    Preconditions.checkNotNull(util);
    return util.getNextStep(data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.util.IUtilsEngine#execute(java.lang.String,
   * java.util.Map)
   */
  @Override
  public ResultUtil execute(String utilCode, Map<String, Object> data) {
    IUtil util = this.getUtilByCode(utilCode);
    Preconditions.checkNotNull(util);
    return util.execute(data);
  }

  /**
   * @param utils
   * @param metaClazz
   * @param objId
   * @return
   */
  private Collection<IUtil> checkCondition(Collection<IUtil> utils, String metaClazz, String objId) {
    return utils;
  }

  /**
   * @param utils
   * @param metaClazz
   * @param objId
   * @return
   */
  private Collection<IUtil> checkSecurity(Collection<IUtil> utils, String metaClazz, String objId) {
    return utils;
  }
}
