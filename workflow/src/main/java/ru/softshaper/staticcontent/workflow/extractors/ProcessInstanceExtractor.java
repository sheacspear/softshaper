package ru.softshaper.staticcontent.workflow.extractors;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.extractors.AbstractObjectExtractor;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;

@Component
@Qualifier(ProcessInstanceStaticContent.META_CLASS)
public class ProcessInstanceExtractor extends AbstractObjectExtractor<ProcessInstance> {

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
