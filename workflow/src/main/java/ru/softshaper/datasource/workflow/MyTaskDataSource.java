package ru.softshaper.datasource.workflow;

import com.google.common.base.Preconditions;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.*;
import ru.softshaper.services.meta.conditions.Condition;
import ru.softshaper.services.meta.conditions.ConditionManager;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.security.DynamicContentSecurityManager;
import ru.softshaper.staticcontent.workflow.BusinessProcessRoleStaticContent;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sunchise on 06.10.2016.
 */
@Component
@Qualifier("myTask")
public class MyTaskDataSource extends AbstractCustomDataSource<WFTask> {

  private final TaskService taskService;

  private final RuntimeService runtimeService;

  private final ContentDataSource<Record> bpRolesDataSource;

  private final DynamicContentSecurityManager securityManager;

  private final MetaStorage metaStorage;

  private final ConditionManager conditionManager;

  @Autowired
  public MyTaskDataSource(@Qualifier(MyTaskStaticContent.META_CLASS) ObjectExtractor<WFTask> objectExtractor,
                          TaskService taskService, RuntimeService runtimeService,
                          @Qualifier("data") ContentDataSource<Record> bpRolesDataSource,
                          DynamicContentSecurityManager securityManager,
                          MetaStorage metaStorage,
                          ConditionManager conditionManager) {
    super(objectExtractor);
    this.taskService = taskService;
    this.runtimeService = runtimeService;
    this.bpRolesDataSource = bpRolesDataSource;
    this.securityManager = securityManager;
    this.metaStorage = metaStorage;
    this.conditionManager = conditionManager;
  }

  @Override
  public Collection<WFTask> getObjects(GetObjectsParams params) {
    List<WFTask> taskList = new ArrayList<>();
    Long currentUserId = securityManager.getCurrentUserId();
    MetaClass roleClass = metaStorage.getMetaClass(BusinessProcessRoleStaticContent.META_CLASS);
    Condition condition = conditionManager.field(roleClass.getField(BusinessProcessRoleStaticContent.Field.users)).equal(currentUserId);
    Collection<Record> roles = bpRolesDataSource.getObjects(GetObjectsParams.newBuilder(roleClass).setCondition(condition).build());
    if (roles != null && !roles.isEmpty()) {
      TaskQuery taskQuery = taskService.createTaskQuery();
      if (params.getIds() != null && !params.getIds().isEmpty()) {
        params.getIds().forEach(taskQuery::taskId);
      }
      List<String> rolesCodes = roles.stream()
          .map(record -> record.get(BusinessProcessRoleStaticContent.Field.code, String.class))
          .collect(Collectors.toList());
      taskQuery = taskQuery.taskCandidateGroupIn(rolesCodes);
      List<Task> subList = taskQuery.active().list();
      if (subList != null) {
        taskList.addAll(convertWFTaskList(subList));
      }
    }
    TaskQuery taskQuery = taskService.createTaskQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(taskQuery::taskId);
    }
    List<Task> subList = taskQuery.taskAssignee(securityManager.getCurrentUserLogin()).active().list();
    if (subList != null) {
      taskList.addAll(convertWFTaskList(subList));
    }
    taskQuery = taskService.createTaskQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(taskQuery::taskId);
    }
    subList = taskQuery.taskUnassigned().list();
    if (subList != null) {
      taskList.addAll(convertWFTaskList(subList));
    }
    return taskList.stream().skip(params.getOffset())
        .limit(params.getLimit())
        .collect(Collectors.toList());
  }

  private List<WFTask> convertWFTaskList(List<Task> subList) {
    return subList.stream()
        .map(task -> WFTask.newBuilder()
            .setAssignee(task.getAssignee())
            .setCreateTime(task.getCreateTime())
            .setDescription(task.getDescription())
            .setDueDate(task.getDueDate())
            .setName(task.getName())
            .setFollowUpDate(task.getFollowUpDate())
            .setOwner(task.getOwner())
            .setPriority(task.getPriority())
            .setSuspended(task.isSuspended())
            .setLinkedObject(getLinkedObjectId(task))
            .setId(task.getId())
            .build())
        .collect(Collectors.toList());
  }

  private String getLinkedObjectId(Task task) {
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
    Preconditions.checkNotNull(processInstance);
    return processInstance.getBusinessKey();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Collection<WFTask> getAllObjects(GetObjectsParams params) {
    throw new RuntimeException("Not supported operation");
  }

  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(String contentCode, String id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    Collection<WFTask> objects = getObjects(GetObjectsParams.newBuilder(metaStorage.getMetaClass(contentCode)).build());
    return objects.isEmpty() ? 0 : objects.size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }
}
