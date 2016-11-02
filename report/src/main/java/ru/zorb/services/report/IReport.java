package ru.zorb.services.report;

import java.util.Map;

/**
 * @author ashek
 *
 */
public interface IReport {

  /**
   * @param params
   * @return
   */
  byte[] build(Map<String, Object> params);

}
