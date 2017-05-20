package ru.softshaper.services.utils;

import java.util.Collection;

public interface IResultUtil {
  
  String getMessage();
  
  boolean isError();
  
  Collection<String> getErrorMessages();
  
}
