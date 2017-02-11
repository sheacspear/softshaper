package ru.softshaper.web.admin.view.mapper.extractors.workflow;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(MyTaskStaticContent.META_CLASS)
public class MyTaskExtractor extends AbstractObjectExtractor<WFTask> {

  public MyTaskExtractor() {
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
  }

  @Override
  public String getId(WFTask obj, MetaClass metaClass) {
    return obj.getId();
  }
}
