package ru.softshaper.web.admin.view.controller.extractors.meta;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.FieldTypeStaticContent;
import ru.softshaper.web.admin.view.controller.ViewObjectController;
import ru.softshaper.web.admin.view.controller.extractors.AbstractObjectExtractor;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;

@Component
@Qualifier(FieldTypeStaticContent.META_CLASS)
public class FieldTypeExtractor extends AbstractObjectExtractor<FieldType> {

  @Autowired
  @Qualifier("fieldType")
  private ContentDataSource<FieldType> fieldTypeDataSource;

  @PostConstruct
  private void init() {
    store.register(FieldTypeStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new ViewObjectController<>(viewSetting, metaStorage, store, this), fieldTypeDataSource));
  }

  public FieldTypeExtractor() {
    registerFieldExtractor(FieldTypeStaticContent.Field.code, FieldType::getCode);
    registerFieldExtractor(FieldTypeStaticContent.Field.name, FieldType::getName);
  }

  @Override
  public String getId(FieldType obj, MetaClass metaClass) {
    return obj.getId().toString();
  }
}
