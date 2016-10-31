package ru.zorb.services.command;

import java.io.Serializable;
import java.util.Map;

/**
 * Сервис выполнения комманд
 */
public interface ICommandService {

  /**
   * executeCommand
   *
   * @param id uuid guid
   * @param type type Command
   * @param object object
   * @return CommandResult
   */
  <T extends Serializable, K extends Serializable> CommandResult<T> executeCommand(String id, String type, K object);

  /**
   * executeCommand
   *
   * @param id uuid guid
   * @param type type Command
   * @param data object
   * @return CommandResult
   */
  <T extends Serializable, K extends Serializable> CommandResult<T> executeCommand(String id, String type, Map<String, Object> data);
}
