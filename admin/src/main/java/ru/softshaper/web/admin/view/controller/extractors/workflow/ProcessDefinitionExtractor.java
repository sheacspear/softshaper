package ru.softshaper.web.admin.view.controller.extractors.workflow;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.ProcessDefinitionStaticContent;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(ProcessDefinitionStaticContent.META_CLASS)
public class ProcessDefinitionExtractor extends AbstractObjectExtractor<ProcessDefinition> {

  @Autowired
  @Qualifier("processDefinition")
  private ContentDataSource<ProcessDefinition> processDefinitionContentDataSource;

  /**
   * Mapper Data to FormBean
   */
  @Autowired
  private IViewObjectController viewObjectController;

  @PostConstruct
  private void init() {
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
    store.register(ProcessDefinitionStaticContent.META_CLASS, new DataSourceFromViewImpl<>(viewObjectController, processDefinitionContentDataSource, this));
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
