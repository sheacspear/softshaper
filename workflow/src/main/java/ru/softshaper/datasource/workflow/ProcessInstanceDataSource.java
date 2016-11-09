package ru.softshaper.datasource.workflow;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.impl.GetObjectsParams;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("processInstance")
public class ProcessInstanceDataSource implements ContentDataSource<ProcessInstance> {

  @Autowired
  private RuntimeService runtimeService;

  @Override
  public Collection<ProcessInstance> getObjects(GetObjectsParams params) {
    ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(processInstanceQuery::processInstanceId);
    }
    return processInstanceQuery.active().list();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public ProcessInstance getObj(GetObjectsParams params) {
    Collection<ProcessInstance> objects = getObjects(params);
    return objects == null || objects.isEmpty() ? null : objects.iterator().next();
  }

  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public void deleteObject(String contentCode, String id) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    return runtimeService.createProcessInstanceQuery().active().list().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }
}
