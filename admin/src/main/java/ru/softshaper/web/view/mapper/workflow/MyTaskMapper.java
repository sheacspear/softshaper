package ru.softshaper.web.view.mapper.workflow;

import com.google.common.base.Preconditions;

import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;
import ru.softshaper.web.view.mapper.ViewMapperBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 06.10.2016.
 */
public class MyTaskMapper extends ViewMapperBase<WFTask> {

  private static final Map<String, Extractor<WFTask, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(MyTaskStaticContent.Field.name, WFTask::getName);
    valueExtractorByField.put(MyTaskStaticContent.Field.description, WFTask::getDescription);
    valueExtractorByField.put(MyTaskStaticContent.Field.assignee, WFTask::getAssignee);
    valueExtractorByField.put(MyTaskStaticContent.Field.owner, WFTask::getOwner);
    valueExtractorByField.put(MyTaskStaticContent.Field.createTime, WFTask::getCreateTime);
    valueExtractorByField.put(MyTaskStaticContent.Field.dueDate, WFTask::getDueDate);
    valueExtractorByField.put(MyTaskStaticContent.Field.followUpDate, WFTask::getFollowUpDate);
    valueExtractorByField.put(MyTaskStaticContent.Field.priority, WFTask::getPriority);
    valueExtractorByField.put(MyTaskStaticContent.Field.linkedObject, WFTask::getLinkedObject);
    valueExtractorByField.put(MyTaskStaticContent.Field.suspended, WFTask::isSuspended);
  }

  public MyTaskMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected Object getValue(WFTask obj, MetaField field) {
    Extractor<WFTask, ?> taskExtractor = valueExtractorByField.get(field.getCode());
    Preconditions.checkNotNull(taskExtractor);
    return taskExtractor.value(obj);
  }

  @Override
  protected String getId(WFTask obj, MetaClass metaClass) {
    return obj.getId();
  }

}
