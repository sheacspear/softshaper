package ru.softshaper.datasource.workflow;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("processInstance")
public class ProcessInstanceDataSource extends AbstractCustomDataSource<ProcessInstance> {

  private final RuntimeService runtimeService;

  @Autowired
  public ProcessInstanceDataSource(@Qualifier(ProcessInstanceStaticContent.META_CLASS) ObjectExtractor<ProcessInstance> objectExtractor,
                                   RuntimeService runtimeService) {
    super(objectExtractor);
    this.runtimeService = runtimeService;
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not supported exception");
  }

  @Override
  protected Collection<ProcessInstance> getAllObjects(GetObjectsParams params) {
    ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(processInstanceQuery::processInstanceId);
    }
    return processInstanceQuery.active().list();
  }

  @Override
  protected Stream<ProcessInstance> filterByIds(GetObjectsParams params, Stream<ProcessInstance> stream) {
    //уже отфильтровано в getAllObjects
    return stream;
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
