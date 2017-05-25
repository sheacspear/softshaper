package ru.softshaper.services.drools;

/**
 * Service for Rule Designer
 *
 */
public interface IRuleDisigner {
  /**
   * 
   * @param id
   * @return
   */
  String validateRule(String id);

  /**
   * 
   * @param id
   * @return
   */
  String validateSuit(String id);

  /**
   * 
   * @param id
   * @return
   */
  SuitResult runSuit(String id);

}
