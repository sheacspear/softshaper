package ru.softshaper.datasource.workflow;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaInitializer;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.impl.GetObjectsParams;

import javax.ws.rs.NotSupportedException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
@Qualifier("task")
public class TaskDataSource implements ContentDataSource<Task> {

  @Autowired
  private TaskService taskService;

  @Autowired
  private MetaStorage metaStorage;

  @Override
  public Collection<Task> getObjects(GetObjectsParams params) {
    TaskQuery taskQuery = taskService.createTaskQuery();
    if (params.getIds() != null && !params.getIds().isEmpty()) {
      params.getIds().forEach(taskQuery::taskId);
    }
    return taskQuery.active().list();
  }

  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    throw new NotSupportedException();
  }

  @Override
  public void setMetaInitializer(MetaInitializer metaInitializer) {

  }

  @Override
  public Task getObj(GetObjectsParams params) {
    if (params.getIds() != null && params.getIds().size() == 1) {
      Collection<Task> objects = getObjects(params);
      return objects.isEmpty() ? null : objects.iterator().next();
    }
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
}