package ru.softshaper.web.admin.view.mapper.extractors.workflow;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(ProcessInstanceStaticContent.META_CLASS)
public class ProcessInstanceExtractor extends AbstractObjectExtractor<ProcessInstance> {

  @Autowired
  @Qualifier("processInstance")
  private ContentDataSource<ProcessInstance> processInstanceContentDataSource;

  @PostConstruct
  private void init() {
    store.register(ProcessInstanceStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new DefaultViewMapper<>(viewSetting, metaStorage, store, this), processInstanceContentDataSource));
  }

  public ProcessInstanceExtractor() {
    registerFieldExtractor(ProcessInstanceStaticContent.Field.suspended, ProcessInstance::isSuspended);
    registerFieldExtractor(ProcessInstanceStaticContent.Field.businessKey, ProcessInstance::getBusinessKey);
    registerFieldExtractor(ProcessInstanceStaticContent.Field.processDefinition, ProcessInstance::getProcessDefinitionId);
  }

  @Override
  public String getId(ProcessInstance obj, MetaClass metaClass) {
    return obj.getId();
  }
}
