package ru.zorb.conf;

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
import ru.zorb.services.meta.*;
import ru.zorb.services.meta.staticcontent.meta.*;
import ru.zorb.services.workflow.beans.WFTask;
import ru.zorb.services.workflow.staticcontent.MyTaskStaticContent;
import ru.zorb.services.workflow.staticcontent.ProcessDefinitionStaticContent;
import ru.zorb.services.workflow.staticcontent.ProcessInstanceStaticContent;
import ru.zorb.services.workflow.staticcontent.TaskStaticContent;
import ru.zorb.web.bean.FieldTypeView;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.DataSourceFromViewImpl;
import ru.zorb.web.view.impl.DataSourceFromViewStoreImpl;
import ru.zorb.web.view.impl.ViewSettingFactory;
import ru.zorb.web.view.mapper.*;
import ru.zorb.web.view.mapper.workflow.MyTaskMapper;
import ru.zorb.web.view.mapper.workflow.ProcessDefinitionMapper;
import ru.zorb.web.view.mapper.workflow.ProcessInstanceMapper;
import ru.zorb.web.view.mapper.workflow.TaskMapper;

/**
 * CommandConfig for register web component
 *
 * @author ashek
 *
 */
@Configuration
@Import({ JooqConfig.class, MetaConfig.class })
@ComponentScan(basePackages = { "ru.zorb.web" })
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

	/**
	 * DataSource by MetaField
	 */
	@Autowired
	@Qualifier("metaField")
	private ContentDataSource<MetaField> metaFieldDataSource;

  @Autowired
  @Qualifier("fieldType")
  private ContentDataSource<FieldType> fieldTypeDataSource;

  @Autowired
  @Qualifier("fieldTypeView")
  private ContentDataSource<FieldTypeView> fieldTypeViewDataSource;

  @Autowired
  @Qualifier("myTask")
  private ContentDataSource<WFTask> myTaskDataSource;

  @Autowired
  @Qualifier("task")
  private ContentDataSource<Task> taskDataSource;

  @Autowired
  @Qualifier("processDefinition")
  private ContentDataSource<ProcessDefinition> processDefinitionContentDataSource;

  @Autowired
  @Qualifier("processInstance")
  private ContentDataSource<ProcessInstance> processInstanceContentDataSource;

  @Autowired
  private FileObjectDataSource fileObjectDataSource;
	/**
	 * @return Хранилище Источник данных для формы
	 */
	@Bean
	public DataSourceFromViewStore dataSourceFromViewStore() {
		DataSourceFromViewStoreImpl dataSourceFromViewStore = new DataSourceFromViewStoreImpl();
		ObjectViewMapper dataViewMapperBase = new ObjectViewMapper(viewSetting, metaStorage, dataSourceFromViewStore);
		dataSourceFromViewStore
				.setDefaultViewMapper(new DataSourceFromViewImpl<>(dataViewMapperBase, dynamicDataSource));
		dataSourceFromViewStore.registerMapper(MetaClassStaticContent.META_CLASS,
				new DataSourceFromViewImpl<>(new MetaClassViewMapper(viewSetting, metaStorage, dataSourceFromViewStore), metaClassDataSource));
		MetaFieldViewMapper metaFieldViewMapper = new MetaFieldViewMapper(viewSetting, metaStorage,
				dataSourceFromViewStore);
		dataSourceFromViewStore.registerMapper(MetaFieldStaticContent.META_CLASS,
				new DataSourceFromViewImpl<>(metaFieldViewMapper, metaFieldDataSource));
		FieldTypeViewMapper fieldTypeViewMapper = new FieldTypeViewMapper(viewSetting, metaStorage, dataSourceFromViewStore);
    dataSourceFromViewStore.registerMapper(FieldTypeStaticContent.META_CLASS,	new DataSourceFromViewImpl<>(fieldTypeViewMapper, fieldTypeDataSource));
    FieldTypeViewViewMapper fieldTypeViewViewMapper = new FieldTypeViewViewMapper(viewSetting, metaStorage, dataSourceFromViewStore);
    dataSourceFromViewStore.registerMapper(FieldTypeViewStaticContent.META_CLASS,	new DataSourceFromViewImpl<>(fieldTypeViewViewMapper, fieldTypeViewDataSource));
    dataSourceFromViewStore.registerMapper(FileObjectStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new FileViewMapper(viewSetting, metaStorage, dataSourceFromViewStore), fileObjectDataSource));
    dataSourceFromViewStore.registerMapper(MyTaskStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new MyTaskMapper(viewSetting, metaStorage, dataSourceFromViewStore), myTaskDataSource));
    dataSourceFromViewStore.registerMapper(TaskStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new TaskMapper(viewSetting, metaStorage, dataSourceFromViewStore), taskDataSource));
    dataSourceFromViewStore.registerMapper(ProcessDefinitionStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new ProcessDefinitionMapper(viewSetting, metaStorage, dataSourceFromViewStore), processDefinitionContentDataSource));
    dataSourceFromViewStore.registerMapper(ProcessInstanceStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new ProcessInstanceMapper(viewSetting, metaStorage, dataSourceFromViewStore), processInstanceContentDataSource));
		return dataSourceFromViewStore;
	}

}
