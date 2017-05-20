package ru.softshaper.services.utils;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public interface IUtilsEngine {

  /**
   * @return
   */
  Collection<IUtil> getSystemUtils();

  /**
   * @param metaClazz
   * @param objId
   * @return
   */
  Collection<IUtil> getAvailableUtilsForObject(String metaClazz, String objId);

  /**
   * @param utilCode
   * @return
   */
  IUtil getUtilByCode(String utilCode);

  /**
   * @param utilCode
   * @param data
   * @return
   */
  StepUtil getNextStep(String utilCode, Map<String, Object> data);

  /**
   * @param utilCode
   * @param objId
   * @param metaClazz
   * @param data
   * @return
   */
  IResultUtil execute(String utilCode, String metaClazz, String objId, Map<String, Object> data);

  /**
   * @param util
   */
  void registerObjectUtil(IUtil util, String metaClazz);

  /**
   * @param util
   */
  void registerSystemUtil(IUtil util);

}
