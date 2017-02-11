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

import ru.softshaper.bean.file.FileObject;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.beans.workflow.WFTask;
import ru.softshaper.conf.db.JooqConfig;
import ru.softshaper.conf.meta.MetaConfig;
import ru.softshaper.datasource.file.FileObjectDataSource;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.staticcontent.file.FileObjectStaticContent;
import ru.softshaper.staticcontent.meta.meta.FieldTypeStaticContent;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;
import ru.softshaper.staticcontent.workflow.MyTaskStaticContent;
import ru.softshaper.staticcontent.workflow.ProcessDefinitionStaticContent;
import ru.softshaper.staticcontent.workflow.ProcessInstanceStaticContent;
import ru.softshaper.staticcontent.workflow.TaskStaticContent;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.DataViewMapper;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewStoreImpl;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.mapper.ObjectViewMapper;

/**
 * CommandConfig for register web component
 *
 * @author ashek
 */
@Configuration
@Import({ JooqConfig.class, MetaConfig.class })
@ComponentScan(basePackages = { "ru.softshaper.web" })
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
  @Qualifier(FileObjectStaticContent.META_CLASS)
  private ObjectExtractor<FileObject> fileObjectExtractor;

  @Autowired
  private FileObjectDataSource fileObjectDataSource;

  /**
   * @return Хранилище Источник данных для формы
   */
  @Bean
  public DataSourceFromViewStore dataSourceFromViewStore() {
    DataSourceFromViewStoreImpl store = new DataSourceFromViewStoreImpl();
    // мапперы на основе стандартного
    store.register(MetaClassStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, metaClassObjectExtractor, metaClassDataSource));
    store.register(MetaFieldStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, metaFieldObjectExtractor, metaFieldDataSource));
    store.register(FieldTypeStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, fieldTypeObjectExtractor, fieldTypeDataSource));
    store.register(FieldTypeViewStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, fieldTypeViewObjectExtractor, fieldTypeViewDataSource));
    store.register(FileObjectStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, fileObjectExtractor, fileObjectDataSource));
    store.register(MyTaskStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, myTaskObjectExtractor, myTaskDataSource));
    store.register(TaskStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, taskObjectExtractor, taskDataSource));
    store.register(ProcessDefinitionStaticContent.META_CLASS,getDataSourceFromViewByExtractor(store, processDefinitionObjectExtractor, processDefinitionContentDataSource));
    store.register(ProcessInstanceStaticContent.META_CLASS, getDataSourceFromViewByExtractor(store, processInstanceObjectExtractor, processInstanceContentDataSource));
    return store;
  }

  private <T> DataSourceFromView getDataSourceFromView(DataViewMapper<T> mapper, ContentDataSource<T> dataSource) {
    return new DataSourceFromViewImpl<T>(mapper, dataSource);
  }

  private <T> DataSourceFromView getDataSourceFromViewByExtractor(DataSourceFromViewStoreImpl dataSourceFromViewStore, ObjectExtractor<T> objectExtractor,
      ContentDataSource<T> dataSource) {
    return getDataSourceFromView(getDefaultMapper(dataSourceFromViewStore, objectExtractor), dataSource);
  }

  private <T> DataViewMapper<T> getDefaultMapper(DataSourceFromViewStoreImpl dataSourceFromViewStore, ObjectExtractor<T> objectExtractor) {
    return new DefaultViewMapper<>(viewSetting, metaStorage, dataSourceFromViewStore, objectExtractor);
  }
}
