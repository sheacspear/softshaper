package ru.softshaper.services.workflow.staticcontent;

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
public class TaskStaticContent extends StaticContentBase {


  public static final String META_CLASS = "task";

  public interface Field {
    String name = "name";
    String description = "description";
    String priority = "priority";
    String owner = "owner";
    String assignee = "assignee";
    String processInstance = "processInstance";
    String executionId = "executionId";
    String processDefinition = "processDefinition";
    String caseInstanceId = "caseInstanceId";
    String caseExecutionId = "caseExecutionId";
    String caseDefinitionId = "caseDefinitionId";
    String taskDefinitionKey = "taskDefinitionKey";
    String createTime = "createTime";
    String dueDate = "dueDate";
    String followUpDate = "followUpDate";
    String parentTaskId = "parentTaskId";
    String suspended = "suspended";
    String tenantId = "tenantId";
  }


  @Autowired
  public TaskStaticContent(@Qualifier("task") ContentDataSource<?> dataSource) {
    super(META_CLASS, "Задачи бизнес процесса", null, dataSource);
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.name).setName("Наименование");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.description).setName("description");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.priority).setName("priority");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.owner).setName("owner");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.assignee).setName("assignee");
    registerField(null, FieldType.LINK).setCode(TaskStaticContent.Field.processInstance).setName("processInstance").setLinkToMetaClass(ProcessInstanceStaticContent.META_CLASS);
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.executionId).setName("executionId");
    registerField(null, FieldType.LINK).setCode(TaskStaticContent.Field.processDefinition).setName("processDefinition").setLinkToMetaClass(ProcessDefinitionStaticContent.META_CLASS);
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.caseInstanceId).setName("caseInstanceId");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.caseExecutionId).setName("caseExecutionId");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.caseDefinitionId).setName("caseDefinitionId");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.taskDefinitionKey).setName("taskDefinitionKey");
    registerField(null, FieldType.DATE).setCode(TaskStaticContent.Field.createTime).setName("createTime");
    registerField(null, FieldType.DATE).setCode(TaskStaticContent.Field.dueDate).setName("dueDate");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.followUpDate).setName("followUpDate");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.parentTaskId).setName("parentTaskId");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.suspended).setName("suspended");
    registerField(null, FieldType.STRING).setCode(TaskStaticContent.Field.tenantId).setName("tenantId");
  }
}
