package ru.softshaper.datasource.workflow;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.datasource.meta.AbstractObjectExtractor;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("processInstance")
public class ProcessInstanceDataSource extends AbstractCustomDataSource<ProcessInstance> {

  private final RuntimeService runtimeService;

  private final static ObjectExtractor<ProcessInstance> objectExtractor = new ProcessInstanceExtractor();

  @Autowired
  public ProcessInstanceDataSource(RuntimeService runtimeService) {
    super(objectExtractor);
    this.runtimeService = runtimeService;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#getObjectsIdsByMultifield(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new RuntimeException("Not supported exception");
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.AbstractCustomDataSource#getAllObjects(ru.softshaper.services.meta.impl.GetObjectsParams)
   */
  @Override
  protected Collection<ProcessInstance> getAllObjects(GetObjectsParams params) {
    ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(processInstanceQuery::processInstanceId);
    }
    return processInstanceQuery.active().list();
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.AbstractCustomDataSource#filterByIds(ru.softshaper.services.meta.impl.GetObjectsParams, java.util.stream.Stream)
   */
  @Override
  protected Stream<ProcessInstance> filterByIds(GetObjectsParams params, Stream<ProcessInstance> stream) {
    // уже отфильтровано в getAllObjects
    return stream;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#createObject(java.lang.String, java.util.Map)
   */
  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new RuntimeException("Not supported exception");
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#updateObject(java.lang.String, java.lang.String, java.util.Map)
   */
  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new RuntimeException("Not supported exception");
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#deleteObject(java.lang.String, java.lang.String)
   */
  @Override
  public void deleteObject(String contentCode, String id) {
    throw new RuntimeException("Not supported exception");
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#getCntObjList(java.lang.String)
   */
  @Override
  public Integer getCntObjList(String contentCode) {
    return runtimeService.createProcessInstanceQuery().active().list().size();
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#getIdType(java.lang.String)
   */
  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#getObjectExtractor()
   */
  @Override
  public ObjectExtractor<ProcessInstance> getObjectExtractor() {
    return objectExtractor;
  }

  public static class ProcessInstanceExtractor extends AbstractObjectExtractor<ProcessInstance> {

    public ProcessInstanceExtractor() {
      registerFieldExtractor(ProcessInstanceStaticContent.Field.suspended, ProcessInstance::isSuspended);
      registerFieldExtractor(ProcessInstanceStaticContent.Field.businessKey, ProcessInstance::getBusinessKey);
      registerFieldExtractor(ProcessInstanceStaticContent.Field.processDefinition, ProcessInstance::getProcessDefinitionId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(ProcessInstance obj, MetaClass metaClass) {
      return obj.getId();
    }
  }
}
