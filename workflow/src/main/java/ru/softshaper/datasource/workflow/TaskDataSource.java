package ru.softshaper.datasource.workflow;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.NotSupportedException;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.datasource.meta.AbstractObjectExtractor;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("task")
public class TaskDataSource extends AbstractCustomDataSource<Task> {

  private final TaskService taskService;

  private final MetaStorage metaStorage;

  private final static ObjectExtractor<Task> objectExtractor = new TaskObjectExtractor();

  @Autowired
  public TaskDataSource(TaskService taskService, MetaStorage metaStorage) {
    super(objectExtractor);
    this.taskService = taskService;
    this.metaStorage = metaStorage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#getObjectsIdsByMultifield(
   * java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new NotSupportedException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#createObject(java.lang.
   * String, java.util.Map)
   */
  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new NotSupportedException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#updateObject(java.lang.
   * String, java.lang.String, java.util.Map)
   */
  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new NotSupportedException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#deleteObject(java.lang.
   * String, java.lang.String)
   */
  @Override
  public void deleteObject(String contentCode, String id) {
    throw new NotSupportedException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#getCntObjList(java.lang.
   * String)
   */
  @Override
  public Integer getCntObjList(String contentCode) {
    Collection<Task> objects = getObjects(GetObjectsParams.newBuilder(metaStorage.getMetaClass(contentCode)).build());
    return objects.isEmpty() ? 0 : objects.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.ContentDataSource#getIdType(java.lang.String)
   */
  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.datasource.meta.AbstractCustomDataSource#getAllObjects(ru.
   * softshaper.services.meta.impl.GetObjectsParams)
   */
  @Override
  protected Collection<Task> getAllObjects(GetObjectsParams params) {
    TaskQuery taskQuery = taskService.createTaskQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(taskQuery::taskId);
    }
    return taskQuery.active().list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.datasource.meta.ContentDataSource#getObjectExtractor()
   */
  @Override
  public ObjectExtractor<Task> getObjectExtractor() {
    return objectExtractor;
  }

  public static class TaskObjectExtractor extends AbstractObjectExtractor<Task> {

    private TaskObjectExtractor() {
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

    /*
     * (non-Javadoc)
     * 
     * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
     * ru.softshaper.services.meta.MetaClass)
     */
    @Override
    public String getId(Task obj, MetaClass metaClass) {
      return obj.getId();
    }
  }
}