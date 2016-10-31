package ru.zorb.exceptions;

/**
 * Created by Sunchise on 15.09.2016.
 */
public class NotImplementedException extends RuntimeException {

  public NotImplementedException(Throwable cause) {
    super("Not implemented yet!", cause);
  }

  public NotImplementedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public NotImplementedException() {
    super("Not implemented yet!");
  }

  public NotImplementedException(String message) {
    super(message);
  }

  public NotImplementedException(String message, Throwable cause) {
    super(message, cause);
  }
}
