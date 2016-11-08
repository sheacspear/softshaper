package ru.softshaper.web.view.mapper.workflow;

import com.google.common.base.Preconditions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.workflow.staticcontent.ProcessDefinitionStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;
import ru.softshaper.web.view.mapper.ViewMapperBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
public class ProcessDefinitionMapper extends ViewMapperBase<ProcessDefinition> {

  private static final Map<String, Extractor<ProcessDefinition, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.suspended, ProcessDefinition::isSuspended);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.description, ProcessDefinition::getDescription);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.versionTag, ProcessDefinition::getVersionTag);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.category, ProcessDefinition::getCategory);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.name, ProcessDefinition::getName);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.key, ProcessDefinition::getKey);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.version, ProcessDefinition::getVersion);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.resourceName, ProcessDefinition::getResourceName);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.deploymentId, ProcessDefinition::getDeploymentId);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.diagramResourceName, ProcessDefinition::getDiagramResourceName);
    valueExtractorByField.put(ProcessDefinitionStaticContent.Field.tenantId, ProcessDefinition::getTenantId);
  }

  public ProcessDefinitionMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected Object getValue(ProcessDefinition obj, MetaField field) {
    Extractor<ProcessDefinition, ?> taskExtractor = valueExtractorByField.get(field.getCode());
    Preconditions.checkNotNull(taskExtractor);
    return taskExtractor.value(obj);
  }

  @Override
  protected String getId(ProcessDefinition obj, MetaClass metaClass) {
    return obj.getId();
  }

}
