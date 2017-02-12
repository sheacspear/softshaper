package ru.softshaper.web.admin.view.controller.extractors.meta;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(FieldTypeViewStaticContent.META_CLASS)
public class FieldTypeViewExtractor extends AbstractObjectExtractor<FieldTypeView> {

  @Autowired
  @Qualifier("fieldTypeView")
  private ContentDataSource<FieldTypeView> fieldTypeViewDataSource;

  @PostConstruct
  private void init() {
    registerFieldExtractor(FieldTypeViewStaticContent.Field.code, FieldTypeView::getCode);
    registerFieldExtractor(FieldTypeViewStaticContent.Field.name, FieldTypeView::getName);
    store.register(FieldTypeViewStaticContent.META_CLASS, new DataSourceFromViewImpl<>(viewObjectController, fieldTypeViewDataSource, this));
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.meta.ObjectExtractor#getId(java.lang.Object,
   * ru.softshaper.services.meta.MetaClass)
   */
  @Override
  public String getId(FieldTypeView obj, MetaClass metaClass) {
    return obj.getCode();
  }
}
