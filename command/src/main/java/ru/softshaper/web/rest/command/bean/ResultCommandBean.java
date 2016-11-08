package ru.softshaper.web.rest.command.bean;

import java.io.Serializable;

public class ResultCommandBean <T extends Serializable>{

  private CommandBean commandBean;

  private CommandStatusBean commandStatus;

  private T result;

  public ResultCommandBean(CommandBean commandBean, CommandStatusBean commandStatus, T result) {
    super();
    this.commandBean = commandBean;
    this.commandStatus = commandStatus;
    this.result = result;
  }

  /**
   * @param commandBean the commandBean to set
   */
  public void setCommandBean(CommandBean commandBean) {
    this.commandBean = commandBean;
  }

  /**
   * @return the commandBean
   */
  public CommandBean getCommandBean() {
    return commandBean;
  }

  /**
   * @return the commandStatus
   */
  public CommandStatusBean getCommandStatus() {
    return commandStatus;
  }

  /**
   * @param commandStatus the commandStatus to set
   */
  public void setCommandStatus(CommandStatusBean commandStatus) {
    this.commandStatus = commandStatus;
  }

  /**
   * @return the result
   */
  public Object getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(T result) {
    this.result = result;
  }

}
