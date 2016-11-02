package ru.zorb.web.view.mapper;

import org.jooq.Record;
import ru.zorb.services.meta.MetaClass;
import ru.zorb.services.meta.MetaField;
import ru.zorb.services.meta.MetaStorage;
import ru.zorb.web.view.DataSourceFromViewStore;
import ru.zorb.web.view.impl.ViewSettingFactory;

/**
 * Mapper Data to FormBean
 */
public class ObjectViewMapper extends ViewMapperBase<Record> {


  public ObjectViewMapper(ViewSettingFactory viewSetting, MetaStorage metaStorage, DataSourceFromViewStore dataSourceFromViewStore) {
    super(viewSetting, metaStorage, dataSourceFromViewStore);
  }

  @Override
  protected String getId(Record obj, MetaClass metaClass) {
    return obj.get(metaClass.getIdColumn(), Long.class).toString();
  }

  @Override
  protected Object getValue(Record obj, MetaField field) {
    return obj.get(field.getColumn());
  }
}
