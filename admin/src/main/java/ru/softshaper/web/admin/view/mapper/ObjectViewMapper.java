package ru.softshaper.web.admin.view.mapper;

import org.jooq.Record;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.impl.ViewSettingFactory;

/**
 * Mapper Data to FormBean
 */
public class ObjectViewMapper extends ViewMapperBase<Record> {

  public ObjectViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage,
      DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
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
