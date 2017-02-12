package ru.softshaper.web.admin.view.controller.extractors.meta;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(MetaClassStaticContent.META_CLASS)
public class MetaClassObjectExtractor extends AbstractObjectExtractor<MetaClass> {

  /**
   * DataSource by MetaClass
   */
  @Autowired
  @Qualifier("metaClass")
  private ContentDataSource<MetaClass> metaClassDataSource;

  @PostConstruct
  private void init() {
    registerFieldExtractor(MetaClassStaticContent.Field.code, MetaClass::getCode);
    registerFieldExtractor(MetaClassStaticContent.Field.name, MetaClass::getName);
    registerFieldExtractor(MetaClassStaticContent.Field.table, MetaClass::getTable);
    registerFieldExtractor(MetaClassStaticContent.Field.fields, MetaClass::getFields);
    registerFieldExtractor(MetaClassStaticContent.Field.fixed, MetaClass::isFixed);
    registerFieldExtractor(MetaClassStaticContent.Field.checkObjectSecurity, MetaClass::isCheckObjectSecurity);
    registerFieldExtractor(MetaClassStaticContent.Field.checkSecurity, MetaClass::isCheckSecurity);
    store.register(MetaClassStaticContent.META_CLASS, new DataSourceFromViewImpl<>(viewObjectController, metaClassDataSource, this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
   * ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(MetaClass obj, MetaClass metaClass) {
    return obj.getId();
  }
}
