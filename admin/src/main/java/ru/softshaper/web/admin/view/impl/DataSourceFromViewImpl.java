package ru.softshaper.web.admin.view.impl;

import com.google.common.collect.Maps;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.web.admin.bean.obj.impl.FullObjectView;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.objlist.ListObjectsView;
import ru.softshaper.web.admin.bean.objlist.TableObjectsView;
import ru.softshaper.web.admin.view.DataSourceFromView;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.params.ViewObjectsParams;

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
  private final IViewObjectController<D> mapper;

  /**
   * DataSource
   */
  private final ContentDataSource<D> dataSource;

  /**
   * @param mapper
   * @param dataSource
   */
  public DataSourceFromViewImpl(IViewObjectController<D> mapper, ContentDataSource<D> dataSource) {
    super();
    this.mapper = mapper;
    this.dataSource = dataSource;
  }


  /* (non-Javadoc)
   * @see ru.softshaper.web.view.DataSourceFromView#getObjectsIdsByMultifield(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    return dataSource.getObjectsIdsByMultifield(contentCode, multyfieldCode, id, reverse);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.web.view.DataSourceFromView#getNewObject(ru.softshaper.web.rest.query. bean.NewObjParam)
   */
  @Override
  public FullObjectView getNewObject(String contentCode, String backLinkAttr, String objId) {
    Map<String, Object> defValue = Maps.newHashMap();
    defValue.put(backLinkAttr, objId);
    return mapper.getEmptyObj(contentCode, defValue);
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.DataSourceFromView#getTableObjects(ru.softshaper.web.view.utils.ViewObjectsParams)
   */
  @Override
  public TableObjectsView getTableObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
    	//для погинации общее кол-во объектов
    	int cntAll = dataSource.getCntObjList(params.getParams().getMetaClass().getCode());
      return mapper.convertTableObjects(objects, params.getParams().getMetaClass().getCode(), cntAll);
    }
    return null;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.DataSourceFromView#getListObjects(ru.softshaper.web.view.utils.ViewObjectsParams)
   */
  @Override
  public ListObjectsView getListObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
      return mapper.convertListObjects(objects, params.getParams().getMetaClass().getCode(), objects.size());
    }
    return null;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.DataSourceFromView#getFullObject(ru.softshaper.web.view.utils.ViewObjectsParams)
   */
  @Override
  public FullObjectView getFullObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : mapper.convertFullObject(obj, params.getParams().getMetaClass().getCode());
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.DataSourceFromView#getTitleObject(ru.softshaper.web.view.utils.ViewObjectsParams)
   */
  @Override
  public TitleObjectView getTitleObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : mapper.convertTitleObject(obj, params.getParams().getMetaClass().getCode());
  }
}
