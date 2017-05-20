package ru.softshaper.services.utils.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.ResultUtil;
import ru.softshaper.services.utils.StepUtil;

@Service
public class UtilsEngine implements IUtilsEngine {

  /**
   * System utils
   */
  private final Collection<IUtil> systemUtils = Lists.newArrayList();

  /**
   * Object utils
   */
  private final Map<String, IUtil> objectUtils = Maps.newHashMap();

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.services.utils.IUtilsEngine#registerSystemUtil(ru.softshaper.
   * services.utils.IUtil)
   */
  @Override
  public synchronized void registerSystemUtil(IUtil util) {
    systemUtils.add(util);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.services.utils.IUtilsEngine#registerObjectUtil(ru.softshaper.
   * services.utils.IUtil)
   */
  @Override
  public synchronized void registerObjectUtil(IUtil util) {
    objectUtils.put(util.getCode(), util);
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.util.IUtilsEngine#getAvailableUtilsForToolbar()
   */
  @Override
  public Collection<IUtil> getSystemUtils() {
    return Collections.emptyList();
  }

  @Override
  public Collection<IUtil> getAvailableUtilsForObject(String metaClazz, String objId) {
    Collection<IUtil> utils = objectUtils.values();
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
    return objectUtils.get(utilCode);
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
