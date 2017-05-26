package ru.softshaper.rest.admin.query;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.*;
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
import ru.softshaper.view.bean.nav.folder.FolderView;
import ru.softshaper.view.bean.obj.IFullObjectView;
import ru.softshaper.view.bean.obj.IObjectView;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.bean.objlist.ITableObjectsView;
import ru.softshaper.view.controller.DataSourceFromView;
import ru.softshaper.view.controller.IViewObjectController;
import ru.softshaper.view.params.FieldCollection;
import ru.softshaper.view.params.ViewObjectParamsBuilder;
import ru.softshaper.view.params.ViewObjectsParams;
import ru.softshaper.view.viewsettings.store.ViewSettingStore;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
  private ViewSettingStore viewSetting;

  @Autowired
  protected IViewObjectController viewObjectController;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
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
  @Produces(MediaType.APPLICATION_JSON)
  public IFullObjectView getObject(@PathParam("contentCode") String contentCode, @PathParam("objectId") String objectId) {
    MetaClass metaClass = metaStorage.getMetaClass(contentCode);
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(contentCode);
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
  @Produces(MediaType.APPLICATION_JSON)
  public IFullObjectView updateObject(@PathParam("contentCode") String contentCode, @PathParam("objectId") String objectId, Map<String, Object> attrs) {
    MetaClass metaClass = metaStorage.getMetaClass(contentCode);
    attrs = viewObjectController.parseAttrsFromView(metaClass, attrs);
    dataSourceStorage.get(metaClass).updateObject(contentCode, objectId, attrs);
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(contentCode);
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
  @Produces(MediaType.APPLICATION_JSON)
  public IFullObjectView createObject(@PathParam("contentCode") String contentCode, Map<String, Object> attrs) {
    MetaClass metaClass = metaStorage.getMetaClass(contentCode);
    attrs = viewObjectController.parseAttrsFromView(metaClass, attrs);
    String objectId = dataSourceStorage.get(metaClass).createObject(contentCode, attrs);
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(contentCode);
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
  @Produces(MediaType.APPLICATION_JSON)
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
  @Produces(MediaType.APPLICATION_JSON)
  public IFullObjectView getNewObject(@PathParam("contentCode") String contentCode, @PathParam("backLinkAttr") String backLinkAttr,
      @PathParam("objId") String objId) {
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(contentCode);
    return dataSourceFromView.getNewObject(contentCode, backLinkAttr, objId);
  }

  /**
   * @return навигатор
   */
  @GET
  @Path("/nav")
  @Produces(MediaType.APPLICATION_JSON)
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
    if (false) {
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
      params = GetObjectsParams.newBuilder(metaClassClass).orderFields().add(metaClassClass.getField(MetaClassStaticContent.Field.name), SortOrder.ASC).build();
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
      folderView.setId(MetaClassStaticContent.META_CLASS + "/page/1");
      folderView.setName("Контент");
      folders2.add(folderView);
      // Пользователи
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(SecUserStaticContent.META_CLASS + "/page/1");
      folderView.setName("Пользователи");
      folders2.add(folderView);
      // Роли
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(SecRoleStaticContent.META_CLASS + "/page/1");
      folderView.setName("Роли");
      folders2.add(folderView);
      // Папки
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(FolderStaticContent.META_CLASS + "/page/1");
      folderView.setName("Папки");
      folders2.add(folderView);
      // Организации
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(OrganizationStaticContent.META_CLASS + "/page/1");
      folderView.setName("Организации");
      folders2.add(folderView);
      // Должность
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(PositionStaticContent.META_CLASS + "/page/1");
      folderView.setName("Должность");
      folders2.add(folderView);
      // Сотрудники
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId(EmployeeStaticContent.META_CLASS + "/page/1");
      folderView.setName("Сотрудники");
      folders2.add(folderView);
      // Классы
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId("metaClass/page/1");
      folderView.setName("Классы");
      folders2.add(folderView);
      // Аудит
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId("audit/page/1");
      folderView.setName("Аудит");
      folders2.add(folderView);
      // Все задачи
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId("task/page/1");
      folderView.setName("Все задачи");
      folders2.add(folderView);

      // Мои задачи
      folderView = new FolderView();
      folderView.setType("objlist");
      folderView.setId("myTask/page/1");
      folderView.setName("Мои задачи");
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
    }
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
  @Produces(MediaType.APPLICATION_JSON)
  public IListObjectsView getQuickInput(@PathParam("contentCode") String contentCode, @QueryParam("value") String value) {
    Preconditions.checkNotNull(contentCode);
    Preconditions.checkArgument(!StringUtils.isEmpty(value));
    MetaClass metaClass = metaStorage.getMetaClass(contentCode);
    Preconditions.checkNotNull(metaClass);
    Condition condition = metaClass.getFields().stream().filter(field -> viewSetting.getView(field).isTitleField())
        .map(field -> (Condition) new CompareValueCondition<>(field, value, CompareOperation.LIKE)).reduce(Condition::or).get();
    DataSourceFromView dataSourceFromView = viewObjectController.getDataSourceFromView(metaClass.getCode());
    ViewObjectsParams params = ViewObjectsParams.newBuilder(metaClass).setCondition(condition).setFieldCollection(FieldCollection.TITLE).build();
    return dataSourceFromView.getListObjects(params);
  }

  /**
   * Получние списка объектов
   *
   * @param metaClassCode
   * @param offset
   * @param limit
   * @return
   */
  /*
   * @GET
   *
   * @Path("/obj/{contentCode}")
   *
   * @Produces(MediaType.APPLICATION_JSON) public ITableObjectsView
   * getObjectList(@PathParam("contentCode") String contentCode,
   *
   * @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,
   *
   * @QueryParam("sortColumn") String sortColumn, @QueryParam("sortDirection")
   * String sortDirection) { MetaClass metaClass =
   * metaStorage.getMetaClass(contentCode); DataSourceFromView
   * dataSourceFromView =
   * viewObjectController.getDataSourceFromView(contentCode);
   * Preconditions.checkNotNull(dataSourceFromView); ViewObjectParamsBuilder
   * paramsBuilder = ViewObjectsParams.newBuilder(metaClass).setOffset(offset)
   * .setLimit(limit != null ? limit :
   * 50).setFieldCollection(FieldCollection.TABLE); if (sortColumn != null) {
   * MetaField orderField = metaClass.getField(sortColumn); if (orderField !=
   * null) { paramsBuilder.orderFields().add(orderField,
   * "DESC".equals(sortDirection) ? SortOrder.DESC : SortOrder.ASC); } } return
   * dataSourceFromView.getTableObjects(paramsBuilder.build()); }
   */

  @GET
  @Path("/obj/{contentCode}/")
  @Produces(MediaType.APPLICATION_JSON)
  public ITableObjectsView getObjectList(@PathParam("contentCode") String metaClassCode, @QueryParam("limit") int limit, @QueryParam("offset") int offset,
      @QueryParam("sortColumn") String sortColumn, @QueryParam("sortDirection") String sortDirection, @QueryParam("query") String query) {
    Preconditions.checkNotNull(metaClassCode);
    MetaClass metaClass = metaStorage.getMetaClass(metaClassCode);
    Preconditions.checkNotNull(metaClass);
    ViewObjectParamsBuilder paramsBuilder = ViewObjectsParams.newBuilder(metaClass);
    if (query != null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
      Map<String, Object> values = new HashMap<String, Object>();
      try {
        String content = new String(Base64.getDecoder().decode(query.getBytes("UTF-8")), "UTF-8");
        values = mapper.readValue(content, new TypeReference<Map<String, String>>() {
        });
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
      if (values != null && !values.isEmpty()) {

        Condition condition = null;
        for (Map.Entry<String, Object> valueEntry : values.entrySet()) {
          String fieldKey = valueEntry.getKey();
          if (fieldKey.equals("id")) {
            paramsBuilder.addIds(Arrays.asList((String) valueEntry.getValue()));
          } else {

            MetaField field = metaClass.getField(fieldKey);
            Condition condition1;
            if (FieldType.LINK.equals(field.getType())) {
              MetaClass linkToMetaClass = field.getLinkToMetaClass();
              Collection<? extends MetaField> titleFieldsOfLinkedClass = linkToMetaClass.getFields().stream()
                  .filter(fieldOfLinkedClass -> viewSetting.getView(fieldOfLinkedClass).isTitleField()).collect(Collectors.toSet());
              ViewObjectParamsBuilder linkedClassParams = ViewObjectsParams.newBuilder(linkToMetaClass);
              Condition linkedCondition = titleFieldsOfLinkedClass.stream()
                  .map(o -> (Condition) new CompareValueCondition<>(o, valueEntry.getValue(), CompareOperation.LIKE)).reduce(Condition::or).get();
              linkedClassParams.setCondition(linkedCondition);
              IListObjectsView listObjects = viewObjectController.getDataSourceFromView(metaClassCode).getListObjects(linkedClassParams.build());
              Collection<String> linkedIds = listObjects.getObjects().stream().map(IObjectView::getId).collect(Collectors.toSet());
              condition1 = new CompareValueCondition<>(field, linkedIds, CompareOperation.IN);
            } else {
              condition1 = new CompareValueCondition<>(field, valueEntry.getValue(), CompareOperation.LIKE);
            }
            if (condition == null) {
              condition = condition1;
            } else {
              condition = condition.and(condition1);
            }
          }
          paramsBuilder.setCondition(condition);
        }
      }
    }

    paramsBuilder.setLimit(limit);
    paramsBuilder.setOffset(offset);
    if (sortColumn != null) {
      MetaField orderField = metaClass.getField(sortColumn);
      if (orderField != null) {
        paramsBuilder.orderFields().add(orderField, "DESC".equals(sortDirection) ? SortOrder.DESC : SortOrder.ASC);
      }
    }
    paramsBuilder.setFieldCollection(FieldCollection.TABLE);
    return viewObjectController.getDataSourceFromView(metaClassCode).getTableObjects(paramsBuilder.build());
  }
}
