package ru.softshaper.services.workflow.staticcontent;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
public class ProcessInstanceStaticContent extends StaticContentBase {


  public static final String META_CLASS = "processInstance";

  public interface Field {
    String processDefinition = "processDefinition";
    String businessKey = "businessKey";
    String caseInstance = "caseInstance";
    String suspended = "suspended";
  }

  @Autowired
  public ProcessInstanceStaticContent(@Qualifier("processInstance") ContentDataSource<ProcessInstance> dataSource) {
    super(META_CLASS, "Сущность бизнес процесса", null, dataSource);
    registerField(null, FieldType.STRING).setCode(Field.businessKey).setName("Ключ");
    registerField(null, FieldType.LINK).setCode(Field.processDefinition).setName("processDefinition").setLinkToMetaClass(ProcessDefinitionStaticContent.META_CLASS);
    //todo: registerField(null, FieldType.LINK).setCode(Field.caseInstance).setName("caseInstance");
    registerField(null, FieldType.LOGICAL).setCode(Field.suspended).setName("suspended");
  }
}
