package ru.softshaper.rest.query;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.DataSourceStorage;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.services.meta.conditions.Condition;
import ru.softshaper.services.meta.conditions.impl.CompareOperation;
import ru.softshaper.services.meta.conditions.impl.CompareValueCondition;
import ru.softshaper.services.meta.impl.GetObjectsParams;
import ru.softshaper.services.meta.impl.SortOrder;
import ru.softshaper.staticcontent.meta.folder.FolderStaticContent;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.staticcontent.organizations.EmployeeStaticContent;
import ru.softshaper.staticcontent.organizations.OrganizationStaticContent;
import ru.softshaper.staticcontent.organizations.PositionStaticContent;
import ru.softshaper.staticcontent.sec.SecRoleStaticContent;
import ru.softshaper.staticcontent.sec.SecUserStaticContent;
import ru.softshaper.storage.jooq.tables.Folder;
import ru.softshaper.web.bean.nav.folder.FolderView;
import ru.softshaper.web.bean.obj.FullObjectView;
import ru.softshaper.web.bean.objlist.TableObjectsView;
import ru.softshaper.web.view.DataSourceFromView;
import ru.softshaper.web.view.DataSourceFromViewStore;
import ru.softshaper.web.view.impl.ViewSettingFactory;
import ru.softshaper.web.view.utils.FieldCollection;
import ru.softshaper.web.view.utils.ViewObjectParamsBuilder;
import ru.softshaper.web.view.utils.ViewObjectsParams;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Контроллер работы со словарями
 */
@Path("/pr/queryservices")
public class QueryServiceRest {

	/**
	 *
	 */
	@Autowired
	@Qualifier("metaClass")
	private ContentDataSource<MetaClass> metaClassDataSource;

	/**
	 *
	 */
	@Autowired
	@Qualifier("data")
	private ContentDataSource<Record> recordDataSource;

	/**
	 * Хранилище Источник данных для формы
	 */
	@Autowired
	private DataSourceFromViewStore dataSourceFromViewStore;

	/**
	 *
	 */
	@Autowired
	private DataSourceStorage dataSourceStorage;

	/**
	 *
	 */
	@Autowired
	private MetaStorage metaStorage;

	/**
	 *
	 */
	@Autowired
	private ViewSettingFactory viewSetting;

	/**
	 * inject this from spring context
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * Получние списка объектов
	 *
	 * @param contentCode
	 * @param offset
	 * @param limit
	 * @return
	 */
	@GET
	@Path("/obj/{contentCode}")
	@Produces("application/json")
	public TableObjectsView getObjectList(@PathParam("contentCode") String contentCode,
			@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,
			@QueryParam("sortColumn") String sortColumn, @QueryParam("sortDirection") String sortDirection) {
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(contentCode);
		Preconditions.checkNotNull(dataSourceFromView);
		ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaClass).setOffset(offset)
				.setLimit(limit != null ? limit : 50).setFieldCollection(FieldCollection.TABLE);
		if (sortColumn != null) {
			MetaField orderField = metaClass.getField(sortColumn);
			if (orderField != null) {
				paramsBuilder.orderFields().add(orderField, "DESC".equals(sortDirection) ? SortOrder.DESC : SortOrder.ASC);
			}
		}
		return dataSourceFromView.getTableObjects(paramsBuilder.build());
	}

	/**
	 * Получение объекта
	 *
	 * @param contentCode
	 * @param objectId
	 * @return
	 */
	@GET
	@Path("/obj/{contentCode}/{objectId}")
	@Produces("application/json")
	public FullObjectView getObject(@PathParam("contentCode") String contentCode,
			@PathParam("objectId") String objectId) {
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(contentCode);
		return dataSourceFromView.getFullObject(ViewObjectsParams.newBuilder(metaClass).ids().add(objectId).build());
	}

	/**
	 * Обновлени ебъекта
	 *
	 * @param contentCode
	 * @param objectId
	 * @param attrs
	 * @return
	 */
	@PUT
	@Path("/obj/{contentCode}/{objectId}")
	@Produces("application/json")
	public FullObjectView updateObject(@PathParam("contentCode") String contentCode,
			@PathParam("objectId") String objectId, Map<String, Object> attrs) {
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		dataSourceStorage.get(metaClass).updateObject(contentCode, objectId, attrs);
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(contentCode);
		return dataSourceFromView.getFullObject(ViewObjectsParams.newBuilder(metaClass).ids().add(objectId).build());
	}

	/**
	 * Создание ебъекта
	 *
	 * @param contentCode
	 * @param attrs
	 * @return
	 */
	@POST
	@Path("/obj/{contentCode}")
	@Produces("application/json")
	public FullObjectView createObject(@PathParam("contentCode") String contentCode, Map<String, Object> attrs) {
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		String objectId = dataSourceStorage.get(metaClass).createObject(contentCode, attrs);
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(contentCode);
		return dataSourceFromView.getFullObject(ViewObjectsParams.newBuilder(metaClass).ids().add(objectId).build());
	}

	/**
	 * Удаление объекта
	 *
	 * @param contentCode
	 * @param objectId
	 */
	@DELETE
	@Path("/obj/{contentCode}/{objectId}")
	@Produces("application/json")
	public void delete(@PathParam("contentCode") String contentCode, @PathParam("objectId") String objectId) {
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		dataSourceStorage.get(metaClass).deleteObject(contentCode, objectId);
	}

	/**
	 * Пустой объект
	 *
	 * @param contentCode
	 * @param backLinkAttr
	 * @param objId
	 * @return
	 */
	@GET
	@Path("/newObj/{contentCode}/{backLinkAttr}/{objId}")
	@Produces("application/json")
	public FullObjectView getNewObject(@PathParam("contentCode") String contentCode,
			@PathParam("backLinkAttr") String backLinkAttr, @PathParam("objId") String objId) {
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(contentCode);
		return dataSourceFromView.getNewObject(contentCode, backLinkAttr, objId);
	}

	/**
	 * @return навигатор
	 */
	@GET
	@Path("/nav")
	@Produces("application/json")
	public Collection<FolderView> getNav() {
		List<FolderView> result = new ArrayList<>();
		MetaClass folderMetaClass = metaStorage.getMetaClass(FolderStaticContent.META_CLASS);
		GetObjectsParams params = GetObjectsParams.newBuilder(metaStorage.getMetaClass(folderMetaClass.getCode())).build();
		Collection<Record> foldersRecords = recordDataSource.getObjects(params);
		foldersRecords.forEach(record -> {
			FolderView folderView = new FolderView();
			folderView.setName(record.get(Folder.FOLDER.NAME, String.class));
			Long folderObjectsMetaClassId = record.get(Folder.FOLDER.META_CLASS, Long.class);
			if (folderObjectsMetaClassId != null) {
				MetaClass folderObjectsMetaClass = metaStorage.getMetaClassById(folderObjectsMetaClassId.toString());
				if (folderObjectsMetaClass != null) {
					folderView.setId(folderObjectsMetaClass.getCode() + "/page/1");
				}
			}
			folderView.setType(record.get(Folder.FOLDER.TYPE, String.class));
			result.add(folderView);
		});

		FolderView navigatorView = new FolderView();
		navigatorView.setName("Контент");
		final List<FolderView> folders1 = new ArrayList<FolderView>();
		FolderView folderView = null;
		// obj
		// folderView = new FolderView();
		// folderView.setType("obj");
		// folderView.setName("Объект obj id = 1");
		// folderView.setId("1");
		// folders.add(folderView);
		// objlist
		MetaClass metaClassClass = metaStorage.getMetaClass(MetaClassStaticContent.META_CLASS);
		params = GetObjectsParams.newBuilder(metaClassClass).orderFields()
				.add(metaClassClass.getField(MetaClassStaticContent.Field.name), SortOrder.ASC).build();
		metaClassDataSource.getObjects(params).forEach(conf -> {
			FolderView folderView2 = new FolderView();
			folderView2.setType("objlist");
			folderView2.setId(conf.getCode() + "/page/1");
			folderView2.setName(conf.getName());
			folders1.add(folderView2);
		});
		// nav2
		folderView = new FolderView();
		navigatorView.setFolders(folders1);
		result.add(navigatorView);
		navigatorView = new FolderView();
		navigatorView.setName("Настройки");
		final List<FolderView> folders2 = new ArrayList<FolderView>();
		// dynamicContents
		/*
		 * folderView = new FolderView(); folderView.setType("objlist");
		 * folderView.setId("dynamicContent/page/1"); folderView.setName(
		 * "Список меты"); folders1.add(folderView);
		 */

    // контент
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(MetaClassStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Контент");
    folders2.add(folderView);
    // Пользователи
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(SecUserStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Пользователи");
    folders2.add(folderView);
    // Роли
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(SecRoleStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Роли");
    folders2.add(folderView);
    // Папки
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(FolderStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Папки");
    folders2.add(folderView);
    // Организации
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(OrganizationStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Организации");
    folders2.add(folderView);
    // Должность
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(PositionStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Должность");
    folders2.add(folderView);
    // Сотрудники
    folderView = new FolderView();
    folderView.setType("objlist");
    folderView.setId(EmployeeStaticContent.META_CLASS+ "/page/1");
    folderView.setName("Сотрудники");
    folders2.add(folderView);
		// workflowdesign
		folderView = new FolderView();
		folderView.setType("workflowdesign");
		folderView.setName("Настройки БП");
		folderView.setId(null);
		folders2.add(folderView);
		// workflowlist
		folderView = new FolderView();
		folderView.setType("workflowlist");
		folderView.setName("Список БП");
		folderView.setId(null);
		folders2.add(folderView);




		navigatorView.setFolders(folders2);
		result.add(navigatorView);
		return result;
	}

	/**
	 * Поиск простой
	 *
	 * @param contentCode
	 * @param value
	 * @return
	 */
	@GET
	@Path("/quickinput/{contentCode}")
	@Produces("application/json")
	public TableObjectsView getQuickInput(@PathParam("contentCode") String contentCode,
			@QueryParam("value") String value) {
		Preconditions.checkNotNull(contentCode);
		Preconditions.checkArgument(!StringUtils.isEmpty(value));
		MetaClass metaClass = metaStorage.getMetaClass(contentCode);
		Preconditions.checkNotNull(metaClass);
		Condition condition = metaClass.getFields().stream()
				.filter(field -> viewSetting.getView(field).isTitleField())
				.map(field -> (Condition) new CompareValueCondition<>(field, value, CompareOperation.LIKE))
				.reduce(Condition::or).get();
		DataSourceFromView dataSourceFromView = dataSourceFromViewStore.get(metaClass.getCode());
		ViewObjectsParams params = ViewObjectsParams.newBuilder(metaClass).setCondition(condition)
				.setFieldCollection(FieldCollection.TABLE).build();
		return dataSourceFromView.getTableObjects(params);
	}

	@POST
	@Path("/search/{contentCode}")
	@Produces("application/json")
	public TableObjectsView search(@PathParam("contentCode") String metaClassCode, Map<String, Object> values, int limit,
			int offset, String orderFieldCode, String sortDirection) {
		Preconditions.checkNotNull(metaClassCode);
		MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
		Preconditions.checkNotNull(metaClass);
		ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaClass);
		if (values != null && !values.isEmpty()) {
			Condition condition = values.entrySet().stream()
					.map(valueEntry -> (Condition) new CompareValueCondition<>(metaClass.getField(valueEntry.getKey()),
							valueEntry.getValue(), CompareOperation.LIKE))
					.reduce(Condition::and).get();
			paramsBuilder.setCondition(condition);
		}
		paramsBuilder.setLimit(limit);
		paramsBuilder.setOffset(offset);
		if (orderFieldCode != null) {
			MetaField orderField = metaClass.getField(orderFieldCode);
			if (orderField != null) {
				paramsBuilder.orderFields().add(orderField, "DESC".equals(sortDirection) ? SortOrder.DESC : SortOrder.ASC);
			}
		}
		paramsBuilder.setFieldCollection(FieldCollection.TABLE);
		return dataSourceFromViewStore.get(metaClassCode).getTableObjects(paramsBuilder.build());
	}
}
