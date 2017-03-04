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
   * @param data
   * @return
   */
  ResultUtil execute(String utilCode, Map<String, Object> data);

  /**
   * @param util
   */
  void registerObjectUtil(IUtil util);

  /**
   * @param util
   */
  void registerSystemUtil(IUtil util);

}
