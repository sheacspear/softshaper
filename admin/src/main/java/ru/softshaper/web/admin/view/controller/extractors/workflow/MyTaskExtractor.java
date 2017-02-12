package ru.softshaper.web.admin.view.controller.extractors.workflow;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(MyTaskStaticContent.META_CLASS)
public class MyTaskExtractor extends AbstractObjectExtractor<WFTask> {

  @Autowired
  @Qualifier("myTask")
  private ContentDataSource<WFTask> myTaskDataSource;

  @PostConstruct
  private void init() {
    registerFieldExtractor(MyTaskStaticContent.Field.name, WFTask::getName);
    registerFieldExtractor(MyTaskStaticContent.Field.description, WFTask::getDescription);
    registerFieldExtractor(MyTaskStaticContent.Field.assignee, WFTask::getAssignee);
    registerFieldExtractor(MyTaskStaticContent.Field.owner, WFTask::getOwner);
    registerFieldExtractor(MyTaskStaticContent.Field.createTime, WFTask::getCreateTime);
    registerFieldExtractor(MyTaskStaticContent.Field.dueDate, WFTask::getDueDate);
    registerFieldExtractor(MyTaskStaticContent.Field.followUpDate, WFTask::getFollowUpDate);
    registerFieldExtractor(MyTaskStaticContent.Field.priority, WFTask::getPriority);
    registerFieldExtractor(MyTaskStaticContent.Field.linkedObject, WFTask::getLinkedObject);
    registerFieldExtractor(MyTaskStaticContent.Field.suspended, WFTask::isSuspended);
    store.register(MyTaskStaticContent.META_CLASS, new DataSourceFromViewImpl<>(viewObjectController, myTaskDataSource, this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
   * ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(WFTask obj, MetaClass metaClass) {
    return obj.getId();
  }
}
