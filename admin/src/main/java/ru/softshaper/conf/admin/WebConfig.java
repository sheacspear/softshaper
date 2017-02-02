package ru.softshaper.conf.admin;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.conf.db.JooqConfig;
import ru.softshaper.conf.meta.MetaConfig;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.*;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.staticcontent.meta.meta.FieldTypeStaticContent;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;
import ru.softshaper.staticcontent.workflow.ProcessDefinitionStaticContent;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.DataViewMapper;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewStoreImpl;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.mapper.FileViewMapper;
import ru.softshaper.web.admin.view.mapper.ObjectViewMapper;

/**
 * CommandConfig for register web component
 *
 * @author ashek
 */
@Configuration
@Import({JooqConfig.class, MetaConfig.class})
@ComponentScan(basePackages = {"ru.softshaper.web"})
public class WebConfig {

  /**
   * Хранилище, которое возвращает представление поля по его параметрам (табица
   * и колонка)
   */
  @Autowired
  private ViewSettingFactory viewSetting;

  /**
   * MetaStorage
   */
  @Autowired
  private MetaStorage metaStorage;
  /**
   * DataSource by Record
   */
  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> dynamicDataSource;

  /**
   * DataSource by MetaClass
   */
  @Autowired
  @Qualifier("metaClass")
  private ContentDataSource<MetaClass> metaClassDataSource;

  @Autowired
  @Qualifier(MetaClassStaticContent.META_CLASS)
  private ObjectExtractor<MetaClass> metaClassObjectExtractor;

  /**
   * DataSource by MetaField
   */
  @Autowired
  @Qualifier("metaField")
  private ContentDataSource<MetaField> metaFieldDataSource;

  @Autowired
  @Qualifier(MetaFieldStaticContent.META_CLASS)
  private ObjectExtractor<MetaField> metaFieldObjectExtractor;

  @Autowired
  @Qualifier("fieldType")
  private ContentDataSource<FieldType> fieldTypeDataSource;

  @Autowired
  @Qualifier(FieldTypeStaticContent.META_CLASS)
  private ObjectExtractor<FieldType> fieldTypeObjectExtractor;

  @Autowired
  @Qualifier("fieldTypeView")
  private ContentDataSource<FieldTypeView> fieldTypeViewDataSource;

  @Autowired
  @Qualifier(FieldTypeViewStaticContent.META_CLASS)
  private ObjectExtractor<FieldTypeView> fieldTypeViewObjectExtractor;

  @Autowired
  @Qualifier("myTask")
  private ContentDataSource<WFTask> myTaskDataSource;

  @Autowired
  @Qualifier(MyTaskStaticContent.META_CLASS)
  private ObjectExtractor<WFTask> myTaskObjectExtractor;

  @Autowired
  @Qualifier("task")
  private ContentDataSource<Task> taskDataSource;

  @Autowired
  @Qualifier(TaskStaticContent.META_CLASS)
  private ObjectExtractor<Task> taskObjectExtractor;

  @Autowired
  @Qualifier("processDefinition")
  private ContentDataSource<ProcessDefinition> processDefinitionContentDataSource;

  @Autowired
  @Qualifier(ProcessDefinitionStaticContent.META_CLASS)
  private ObjectExtractor<ProcessDefinition> processDefinitionObjectExtractor;

  @Autowired
  @Qualifier("processInstance")
  private ContentDataSource<ProcessInstance> processInstanceContentDataSource;

  @Autowired
  @Qualifier(ProcessInstanceStaticContent.META_CLASS)
  private ObjectExtractor<ProcessInstance> processInstanceObjectExtractor;

  @Autowired
  private FileObjectDataSource fileObjectDataSource;

  /**
   * @return Хранилище Источник данных для формы
   */
  @Bean
  public DataSourceFromViewStore dataSourceFromViewStore() {
    DataSourceFromViewStoreImpl dataSourceFromViewStore = new DataSourceFromViewStoreImpl();

    //мапперы на основе стандартного
    DataViewMapper<MetaClass> metaClassViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, metaClassObjectExtractor);
    DataViewMapper<MetaField> metaFieldViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, metaFieldObjectExtractor);
    DataViewMapper<FieldType> fieldTypeViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, fieldTypeObjectExtractor);
    DataViewMapper<FieldTypeView> fieldTypeViewViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, fieldTypeViewObjectExtractor);
    DataViewMapper<WFTask> myTaskViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, myTaskObjectExtractor);
    DataViewMapper<Task> taskViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, taskObjectExtractor);
    DataViewMapper<ProcessDefinition> processDefinitionViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, processDefinitionObjectExtractor);
    DataViewMapper<ProcessInstance> processInstanceViewMapper
        = new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, processInstanceObjectExtractor);
    //специальные мапперы
    FileViewMapper fileViewMapper = new FileViewMapper(viewSetting, metaStorage, dataSourceFromViewStore);
    ObjectViewMapper dataViewMapperBase = new ObjectViewMapper(viewSetting, metaStorage, dataSourceFromViewStore);
    //линкуем мапперы к хранилищам
    dataSourceFromViewStore
        .setDefaultViewMapper(new DataSourceFromViewImpl<>(dataViewMapperBase, dynamicDataSource));
    dataSourceFromViewStore.registerMapper(MetaClassStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(metaClassViewMapper, metaClassDataSource));
    dataSourceFromViewStore.registerMapper(MetaFieldStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(metaFieldViewMapper, metaFieldDataSource));
    dataSourceFromViewStore.registerMapper(FieldTypeStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(fieldTypeViewMapper, fieldTypeDataSource));
    dataSourceFromViewStore.registerMapper(FieldTypeViewStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(fieldTypeViewViewMapper, fieldTypeViewDataSource));
    dataSourceFromViewStore.registerMapper(FileObjectStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(fileViewMapper, fileObjectDataSource));
    dataSourceFromViewStore.registerMapper(MyTaskStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(myTaskViewMapper, myTaskDataSource));
    dataSourceFromViewStore.registerMapper(TaskStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(taskViewMapper, taskDataSource));
    dataSourceFromViewStore.registerMapper(ProcessDefinitionStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(processDefinitionViewMapper, processDefinitionContentDataSource));
    dataSourceFromViewStore.registerMapper(ProcessInstanceStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(processInstanceViewMapper, processInstanceContentDataSource));

    return dataSourceFromViewStore;
  }

}
