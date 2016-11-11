package ru.softshaper.staticcontent.workflow.comporators;

import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.staticcontent.meta.comparators.DefaultObjectComparator;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;

/**
 * Created by Sunchise on 11.11.2016.
 */
@Component
@Qualifier(TaskStaticContent.META_CLASS)
public class TaskObjectComparator extends DefaultObjectComparator<Task> {

  @Autowired
  public TaskObjectComparator(@Qualifier(TaskStaticContent.META_CLASS) ObjectExtractor<Task> objectExtractor) {
    super(objectExtractor);
  }
}
