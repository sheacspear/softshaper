package ru.softshaper.web.view.mapper.workflow;

import com.google.common.base.Preconditions;
import org.camunda.bpm.engine.task.Task;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.workflow.staticcontent.TaskStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;
import ru.softshaper.web.view.mapper.ViewMapperBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
public class TaskMapper extends ViewMapperBase<Task> {

  private static final Map<String, Extractor<Task, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(TaskStaticContent.Field.name, Task::getName);
    valueExtractorByField.put(TaskStaticContent.Field.description, Task::getDescription);
    valueExtractorByField.put(TaskStaticContent.Field.assignee, Task::getAssignee);
    valueExtractorByField.put(TaskStaticContent.Field.owner, Task::getOwner);
    valueExtractorByField.put(TaskStaticContent.Field.taskDefinitionKey, Task::getTaskDefinitionKey);
    valueExtractorByField.put(TaskStaticContent.Field.caseDefinitionId, Task::getCaseDefinitionId);
    valueExtractorByField.put(TaskStaticContent.Field.caseExecutionId, Task::getCaseExecutionId);
    valueExtractorByField.put(TaskStaticContent.Field.caseInstanceId, Task::getCaseInstanceId);
    valueExtractorByField.put(TaskStaticContent.Field.createTime, Task::getCreateTime);
    valueExtractorByField.put(TaskStaticContent.Field.dueDate, Task::getDueDate);
    valueExtractorByField.put(TaskStaticContent.Field.followUpDate, Task::getFollowUpDate);
    valueExtractorByField.put(TaskStaticContent.Field.executionId, Task::getExecutionId);
    valueExtractorByField.put(TaskStaticContent.Field.parentTaskId, Task::getParentTaskId);
    valueExtractorByField.put(TaskStaticContent.Field.priority, Task::getPriority);
    valueExtractorByField.put(TaskStaticContent.Field.processInstance, Task::getProcessInstanceId);
    valueExtractorByField.put(TaskStaticContent.Field.processDefinition, Task::getProcessDefinitionId);
    valueExtractorByField.put(TaskStaticContent.Field.suspended, Task::isSuspended);
    valueExtractorByField.put(TaskStaticContent.Field.tenantId, Task::getTenantId);
  }

  public TaskMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected Object getValue(Task obj, MetaField field) {
    Extractor<Task, ?> taskExtractor = valueExtractorByField.get(field.getCode());
    Preconditions.checkNotNull(taskExtractor);
    return taskExtractor.value(obj);
  }

  @Override
  protected String getId(Task obj, MetaClass metaClass) {
    return obj.getId();
  }

}
