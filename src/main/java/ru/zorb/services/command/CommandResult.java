package ru.zorb.services.command;

import java.io.Serializable;

/**
 * CommandResult
 *
 * @author ashek
 *
 * @param <T>
 */
public class CommandResult<T extends Serializable> {

  /**
   * Result executed command
   */
  private CommandStatus commandStatus;

  /**
   * Result
   */
  private T result;

  /**
   * @param commandStatus
   * @param result
   */
  public CommandResult(CommandStatus commandStatus, T result) {
    super();
    this.commandStatus = commandStatus;
    this.result = result;
  }

  /**
   * @return the commandStatus
   */
  public CommandStatus getCommandStatus() {
    return commandStatus;
  }

  /**
   * @param commandStatus the commandStatus to set
   */
  public void setCommandStatus(CommandStatus commandStatus) {
    this.commandStatus = commandStatus;
  }

  /**
   * @return the result
   */
  public T getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(T result) {
    this.result = result;
  }

}
