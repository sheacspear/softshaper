package ru.softshaper.datasource.workflow;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.AbstractCustomDataSource;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.conditions.CheckConditionVisitor;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.staticcontent.meta.conditions.DefaultConditionChecker;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;
import ru.softshaper.staticcontent.workflow.comporators.TaskObjectComparator;

import javax.ws.rs.NotSupportedException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("task")
public class TaskDataSource extends AbstractCustomDataSource<Task> {

  private final TaskService taskService;

  private final MetaStorage metaStorage;

  @Autowired
  public TaskDataSource(TaskService taskService, MetaStorage metaStorage,
                        @Qualifier(TaskStaticContent.META_CLASS) ObjectExtractor<Task> objectExtractor) {
    super(new TaskObjectComparator(objectExtractor), objectExtractor);
    this.taskService = taskService;
    this.metaStorage = metaStorage;
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new NotSupportedException();
  }

  @Override
  public String createObject(String contentCode, Map<String, Object> values) {
    throw new NotSupportedException();
  }

  @Override
  public void updateObject(String contentCode, String String, Map<String, Object> values) {
    throw new NotSupportedException();
  }

  @Override
  public void deleteObject(String contentCode, String id) {
    throw new NotSupportedException();
  }

  @Override
  public Integer getCntObjList(String contentCode) {
    Collection<Task> objects = getObjects(GetObjectsParams.newBuilder(metaStorage.getMetaClass(contentCode)).build());
    return objects.isEmpty() ? 0 : objects.size();
  }

  @Override
  public Class<?> getIdType(String metaClassCode) {
    return String.class;
  }

  @Override
  protected CheckConditionVisitor newCheckCondition(Task object) {
    return new DefaultConditionChecker<>(object, getObjectExtractor());
  }

  @Override
  protected Collection<Task> getAllObjects(GetObjectsParams params) {
    TaskQuery taskQuery = taskService.createTaskQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(taskQuery::taskId);
    }
    return taskQuery.active().list();
  }
}