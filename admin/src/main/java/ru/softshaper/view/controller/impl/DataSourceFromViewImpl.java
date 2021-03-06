package ru.softshaper.view.controller.impl;

import com.google.common.collect.Maps;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.view.bean.obj.impl.FullObjectView;
import ru.softshaper.view.bean.obj.impl.TitleObjectView;
import ru.softshaper.view.bean.objlist.IListObjectsView;
import ru.softshaper.view.bean.objlist.ITableObjectsView;
import ru.softshaper.view.controller.DataSourceFromView;
import ru.softshaper.view.controller.IViewObjectController;
import ru.softshaper.view.params.ViewObjectsParams;

import java.util.Collection;
import java.util.Map;

/**
 * Источник данных для формы
 *
 * @param <D>
 * @author asheknew
 */
public class DataSourceFromViewImpl<D> implements DataSourceFromView {

  /**
   * Mapper Data to FormBean
   */
  private final IViewObjectController viewObjectController;

  /**
   * DataSource
   */
  private final ContentDataSource<D> dataSource;


  /**
   * @param dataSource
   */
  public DataSourceFromViewImpl(IViewObjectController viewObjectController, ContentDataSource<D> dataSource) {
    super();
    this.viewObjectController = viewObjectController;
    this.dataSource = dataSource;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getObjectsIdsByMultifield(java.
   * lang.String, java.lang.String, java.lang.String, boolean)
   */
  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    return dataSource.getObjectsIdsByMultifield(contentCode, multyfieldCode, id, reverse);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getNewObject(ru.softshaper.web.
   * rest.query. bean.NewObjParam)
   */
  @Override
  public FullObjectView getNewObject(String contentCode, String backLinkAttr, String objId) {
    Map<String, Object> defValue = Maps.newHashMap();
    defValue.put(backLinkAttr, objId);
    return viewObjectController.getEmptyObj(contentCode, defValue, dataSource.getObjectExtractor());
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getTableObjects(ru.softshaper.web
   * .view.utils.ViewObjectsParams)
   */
  @Override
  public ITableObjectsView getTableObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
      // для погинации общее кол-во объектов
      int cntAll = dataSource.getCntObjList(params.getParams().getMetaClass().getCode());
      return viewObjectController.convertTableObjects(objects, params.getParams().getMetaClass().getCode(), cntAll, dataSource.getObjectExtractor());
    }
    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getListObjects(ru.softshaper.web.
   * view.utils.ViewObjectsParams)
   */
  @Override
  public IListObjectsView getListObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
      return viewObjectController.convertListObjects(objects, params.getParams().getMetaClass().getCode(), objects.size(), dataSource.getObjectExtractor());
    }
    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getFullObject(ru.softshaper.web.
   * view.utils.ViewObjectsParams)
   */
  @Override
  public FullObjectView getFullObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : viewObjectController.convertFullObject(obj, params.getParams().getMetaClass().getCode(), dataSource.getObjectExtractor());
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * ru.softshaper.web.view.DataSourceFromView#getTitleObject(ru.softshaper.web.
   * view.utils.ViewObjectsParams)
   */
  @Override
  public TitleObjectView getTitleObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : viewObjectController.convertTitleObject(obj, params.getParams().getMetaClass().getCode(), dataSource.getObjectExtractor());
  }
}
