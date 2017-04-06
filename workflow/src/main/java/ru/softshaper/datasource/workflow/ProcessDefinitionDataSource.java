package ru.softshaper.datasource.workflow;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.datasource.meta.AbstractObjectExtractor;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.workflow.ProcessDefinitionStaticContent;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("processDefinition")
public class ProcessDefinitionDataSource extends AbstractCustomDataSource<ProcessDefinition> {

  private final RepositoryService repositoryService;

  private final static ObjectExtractor<ProcessDefinition> objectExtractor = new ProcessDefinitionExtractor();

  @Autowired
  public ProcessDefinitionDataSource(RepositoryService repositoryService) {
    super(objectExtractor);
    this.repositoryService = repositoryService;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.AbstractCustomDataSource#filterByIds(ru.softshaper.services.meta.impl.GetObjectsParams, java.util.stream.Stream)
   */
  @Override
  protected Stream<ProcessDefinition> filterByIds(GetObjectsParams params, Stream<ProcessDefinition> stream) {
    // уже отфильтровано в getAllObjects
    return stream;
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
  protected Collection<ProcessDefinition> getAllObjects(GetObjectsParams params) {
    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
    if (params.getIds() != null) {
      params.getIds().forEach(processDefinitionQuery::processDefinitionId);
    }
    return processDefinitionQuery.active().list();
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
    return repositoryService.createProcessDefinitionQuery().active().list().size();
  }

  /* (non-Javadoc)
   * @see ru.softshaper.datasource.meta.ContentDataSource#getIdType(java.lang.String)
   */
  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  public static class ProcessDefinitionExtractor extends AbstractObjectExtractor<ProcessDefinition> {

    private ProcessDefinitionExtractor() {
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.suspended, ProcessDefinition::isSuspended);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.description, ProcessDefinition::getDescription);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.versionTag, ProcessDefinition::getVersionTag);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.category, ProcessDefinition::getCategory);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.name, ProcessDefinition::getName);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.key, ProcessDefinition::getKey);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.version, ProcessDefinition::getVersion);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.resourceName, ProcessDefinition::getResourceName);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.deploymentId, ProcessDefinition::getDeploymentId);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.diagramResourceName, ProcessDefinition::getDiagramResourceName);
      registerFieldExtractor(ProcessDefinitionStaticContent.Field.tenantId, ProcessDefinition::getTenantId);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(ProcessDefinition obj, MetaClass metaClass) {
      return obj.getId();
    }
  }

}
