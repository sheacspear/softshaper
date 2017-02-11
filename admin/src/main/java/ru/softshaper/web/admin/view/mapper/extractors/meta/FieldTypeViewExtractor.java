package ru.softshaper.web.admin.view.mapper.extractors.meta;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(FieldTypeViewStaticContent.META_CLASS)
public class FieldTypeViewExtractor extends AbstractObjectExtractor<FieldTypeView> {

  @Autowired
  @Qualifier("fieldTypeView")
  private ContentDataSource<FieldTypeView> fieldTypeViewDataSource;

  @PostConstruct
  private void init() {
    store.register(FieldTypeViewStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new DefaultViewMapper<>(viewSetting, metaStorage, store, this), fieldTypeViewDataSource));
  }

  public FieldTypeViewExtractor() {
    registerFieldExtractor(FieldTypeViewStaticContent.Field.code, FieldTypeView::getCode);
    registerFieldExtractor(FieldTypeViewStaticContent.Field.name, FieldTypeView::getName);
  }

  @Override
  public String getId(FieldTypeView obj, MetaClass metaClass) {
    return obj.getCode();
  }
}
