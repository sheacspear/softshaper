package ru.zorb.web.view.mapper.workflow;

import com.google.common.base.Preconditions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.services.workflow.staticcontent.ProcessInstanceStaticContent;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.ViewSettingFactory;
import ru.zorb.web.view.mapper.ViewMapperBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunchise on 09.10.2016.
 */
public class ProcessInstanceMapper extends ViewMapperBase<ProcessInstance> {

  private static final Map<String, Extractor<ProcessInstance, ?>> valueExtractorByField = new HashMap<>();

  static {
    valueExtractorByField.put(ProcessInstanceStaticContent.Field.suspended, ProcessInstance::isSuspended);
    valueExtractorByField.put(ProcessInstanceStaticContent.Field.businessKey, ProcessInstance::getBusinessKey);
    valueExtractorByField.put(ProcessInstanceStaticContent.Field.processDefinition, ProcessInstance::getProcessDefinitionId);
  }

  public ProcessInstanceMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected Object getValue(ProcessInstance obj, MetaField field) {
    Extractor<ProcessInstance, ?> taskExtractor = valueExtractorByField.get(field.getCode());
    Preconditions.checkNotNull(taskExtractor);
    return taskExtractor.value(obj);
  }

  @Override
  protected String getId(ProcessInstance obj, MetaClass metaClass) {
    return obj.getId();
  }
}
