package ru.softshaper.services.utils.bean.impl;

import java.util.Collection;

import ru.softshaper.services.utils.IResultUtil;

public class ResultUtil implements IResultUtil {

  private final String message;

  private final boolean error;

  Collection<String> errorMessages;

  public ResultUtil(String message, boolean error, Collection<String> errorMessages) {
    super();
    this.message = message;
    this.error = error;
    this.errorMessages = errorMessages;
  }

  public Collection<String> getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(Collection<String> errorMessages) {
    this.errorMessages = errorMessages;
  }

  public String getMessage() {
    return message;
  }

  public boolean isError() {
    return error;
  }

}
