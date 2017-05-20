package ru.softshaper.services.utils;

import java.util.Map;

public interface IUtil {

  String getName();

  String getCode();

  StepUtil getNextStep(Map<String, Object> data);

  IResultUtil execute(String metaClazz, String objId, Map<String, Object> data);

}
