package ru.softshaper.services.utils;

import java.util.Collection;
import java.util.Map;

public interface IUtilsEngine {

  Collection<IUtil> getAvailableUtilsForToolbar();

  Collection<IUtil> getAvailableUtilsForObject(String metaClazz, String objId);

  IUtil getUtilByCode(String utilCode);

  StepUtil getNextStep(String utilCode, Map<String, Object> data);

  ResultUtil execute(String utilCode, Map<String, Object> data);

  void registerUtils(IUtil util);

}
