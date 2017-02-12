package ru.softshaper.web.admin.view.controller.extractors.obj;

import javax.annotation.PostConstruct;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.web.admin.view.DataSourceFromViewStore;
import ru.softshaper.web.admin.view.IViewObjectController;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier("ObjectMetaExtractor")
public class ObjectMetaExtractor implements ObjectExtractor<Record> {

  @Autowired
  private DataSourceFromViewStore store;

  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> dynamicDataSource;

  @Autowired
  private IViewObjectController viewObjectController;

  @PostConstruct
  private void init() {
    store.setDefault(new DataSourceFromViewImpl<>(viewObjectController, dynamicDataSource, this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
   * ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(Record obj, MetaClass metaClass) {
    return obj.get(metaClass.getIdColumn(), Long.class).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.meta.ObjectExtractor#getValue(java.lang.Object,
   * ru.softshaper.services.meta.MetaField)
   */
  @Override
  public Object getValue(Record obj, MetaField field) {
    return obj.get(field.getColumn());
  }
}
