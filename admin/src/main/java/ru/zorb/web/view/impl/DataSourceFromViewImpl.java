package ru.zorb.web.view.impl;

import com.google.common.collect.Maps;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.web.bean.obj.FullObjectView;
import ru.zorb.web.bean.obj.TitleObjectView;
import ru.zorb.web.bean.objlist.ListObjectsView;
import ru.zorb.web.bean.objlist.TableObjectsView;
import ru.zorb.web.view.DataSourceFromView;
import ru.zorb.web.view.DataViewMapper;
import ru.zorb.web.view.utils.ViewObjectsParams;

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
  private final DataViewMapper<D> mapper;

  /**
   * DataSource
   */
  private final ContentDataSource<D> dataSource;

  /**
   * @param mapper
   * @param dataSource
   */
  public DataSourceFromViewImpl(DataViewMapper<D> mapper, ContentDataSource<D> dataSource) {
    super();
    this.mapper = mapper;
    this.dataSource = dataSource;
  }


  @Override
  public Collection<String> getObjectsIdsByMultifield(String contentCode, String multyfieldCode, String id, boolean reverse) {
    return dataSource.getObjectsIdsByMultifield(contentCode, multyfieldCode, id, reverse);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.web.view.DataSourceFromView#getNewObject(ru.zorb.web.rest.query. bean.NewObjParam)
   */
  @Override
  public FullObjectView getNewObject(String contentCode, String backLinkAttr, String objId) {
    Map<String, Object> defValue = Maps.newHashMap();
    defValue.put(backLinkAttr, objId);
    return mapper.getEmptyObj(contentCode, defValue);
  }

  @Override
  public TableObjectsView getTableObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
    	//для погинации общее кол-во объектов
    	int cntAll = dataSource.getCntObjList(params.getParams().getMetaClass().getCode());
      return mapper.convertTableObjects(objects, params.getParams().getMetaClass().getCode(), cntAll, null);
    }
    return null;
  }

  @Override
  public ListObjectsView getListObjects(ViewObjectsParams params) {
    Collection<D> objects = dataSource.getObjects(params.getParams());
    if (objects != null) {
      return mapper.convertListObjects(objects, params.getParams().getMetaClass().getCode(), objects.size(), null);
    }
    return null;
  }

  @Override
  public FullObjectView getFullObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : mapper.convertFullObject(obj, params.getParams().getMetaClass().getCode());
  }

  @Override
  public TitleObjectView getTitleObject(ViewObjectsParams params) {
    D obj = dataSource.getObj(params.getParams());
    return obj == null ? null : mapper.convertTitleObject(obj, params.getParams().getMetaClass().getCode());
  }
}
