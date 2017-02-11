package ru.softshaper.web.admin.view.mapper.extractors.workflow;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.ProcessDefinitionStaticContent;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(ProcessDefinitionStaticContent.META_CLASS)
public class ProcessDefinitionExtractor extends AbstractObjectExtractor<ProcessDefinition> {

  public ProcessDefinitionExtractor() {
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

  @Override
  public String getId(ProcessDefinition obj, MetaClass metaClass) {
    return obj.getId();
  }
}
