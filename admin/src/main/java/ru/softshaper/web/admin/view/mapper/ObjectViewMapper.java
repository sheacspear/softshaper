package ru.softshaper.web.admin.view.mapper;

import javax.annotation.PostConstruct;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;

/**
 * Mapper Data to FormBean
 */
@Component
public class ObjectViewMapper extends ViewMapperBase<Record> {

  @Autowired
  private DataSourceFromViewStore store;

  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> dynamicDataSource;

  @Autowired
  public ObjectViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @PostConstruct
  private void init() {
    store.setDefault(new DataSourceFromViewImpl<>(this, dynamicDataSource));
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.view.mapper.ViewMapperBase#getId(java.lang.Object,
   * ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(Record obj, MetaClass metaClass) {
    return obj.get(metaClass.getIdColumn(), Long.class).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * ru.softshaper.web.view.mapper.ViewMapperBase#getValue(java.lang.Object,
   * ru.softshaper.services.meta.MetaField)
   */
  @Override
  public Object getValue(Record obj, MetaField field) {
    return obj.get(field.getColumn());
  }
}
