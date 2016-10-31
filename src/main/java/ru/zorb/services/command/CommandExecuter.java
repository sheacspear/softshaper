package ru.zorb.services.command;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * CommandExecuter
 *
 * @author ashek
 *
 * @param <T>
 * @param <K>
 */
public interface CommandExecuter<T extends Serializable, K extends Serializable> {

  /**
   * execute
   *
   * @param id uuid command guid
   * @param object
   * @return CommandResult
   */
  CommandResult<T> execute(String id, K object);

  /**
   * @return CommandName
   */
  String getCommandName();

  /**
   * Load data from Json
   * @param data
   * @return
   */
  K loadFromMapRequest(Map<String, Object> data);

}
