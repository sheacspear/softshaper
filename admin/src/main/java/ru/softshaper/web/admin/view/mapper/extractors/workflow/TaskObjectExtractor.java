package ru.softshaper.web.admin.view.mapper.extractors.workflow;

import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(TaskStaticContent.META_CLASS)
public class TaskObjectExtractor extends AbstractObjectExtractor<Task> {

  public TaskObjectExtractor() {
    registerFieldExtractor(TaskStaticContent.Field.name, Task::getName);
    registerFieldExtractor(TaskStaticContent.Field.description, Task::getDescription);
    registerFieldExtractor(TaskStaticContent.Field.assignee, Task::getAssignee);
    registerFieldExtractor(TaskStaticContent.Field.owner, Task::getOwner);
    registerFieldExtractor(TaskStaticContent.Field.taskDefinitionKey, Task::getTaskDefinitionKey);
    registerFieldExtractor(TaskStaticContent.Field.caseDefinitionId, Task::getCaseDefinitionId);
    registerFieldExtractor(TaskStaticContent.Field.caseExecutionId, Task::getCaseExecutionId);
    registerFieldExtractor(TaskStaticContent.Field.caseInstanceId, Task::getCaseInstanceId);
    registerFieldExtractor(TaskStaticContent.Field.createTime, Task::getCreateTime);
    registerFieldExtractor(TaskStaticContent.Field.dueDate, Task::getDueDate);
    registerFieldExtractor(TaskStaticContent.Field.followUpDate, Task::getFollowUpDate);
    registerFieldExtractor(TaskStaticContent.Field.executionId, Task::getExecutionId);
    registerFieldExtractor(TaskStaticContent.Field.parentTaskId, Task::getParentTaskId);
    registerFieldExtractor(TaskStaticContent.Field.priority, Task::getPriority);
    registerFieldExtractor(TaskStaticContent.Field.processInstance, Task::getProcessInstanceId);
    registerFieldExtractor(TaskStaticContent.Field.processDefinition, Task::getProcessDefinitionId);
    registerFieldExtractor(TaskStaticContent.Field.suspended, Task::isSuspended);
    registerFieldExtractor(TaskStaticContent.Field.tenantId, Task::getTenantId);
  }

  @Override
  public String getId(Task obj, MetaClass metaClass) {
    return obj.getId();
  }
}
