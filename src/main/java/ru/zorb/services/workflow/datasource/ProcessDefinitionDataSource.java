package ru.zorb.services.workflow.datasource;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.MetaInitializer;
import ru.zorb.services.meta.impl.GetObjectsParams;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("processDefinition")
public class ProcessDefinitionDataSource implements ContentDataSource<ProcessDefinition> {

  @Autowired
  private RepositoryService repositoryService;

  @Override
  public Collection<ProcessDefinition> getObjects(GetObjectsParams params) {
    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
    if (params.getIds() != null) {
      params.getIds().forEach(processDefinitionQuery::processDefinitionId);
    }
    return processDefinitionQuery.active().list();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }

  @Override
  public ProcessDefinition getObj(GetObjectsParams params) {
    Collection<ProcessDefinition> objects = getObjects(params);
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
    return repositoryService.createProcessDefinitionQuery().active().list().size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }
}
