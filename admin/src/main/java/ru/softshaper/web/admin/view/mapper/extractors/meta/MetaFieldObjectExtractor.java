package ru.softshaper.web.admin.view.mapper.extractors.meta;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;
import ru.softshaper.web.admin.view.impl.DataSourceFromViewImpl;
import ru.softshaper.web.admin.view.mapper.DefaultViewMapper;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(MetaFieldStaticContent.META_CLASS)
public class MetaFieldObjectExtractor extends AbstractObjectExtractor<MetaField> {

  /**
   * DataSource by MetaField
   */
  @Autowired
  @Qualifier("metaField")
  private ContentDataSource<MetaField> metaFieldDataSource;

  @PostConstruct
  private void init() {
    store.register(MetaFieldStaticContent.META_CLASS,
        new DataSourceFromViewImpl<>(new DefaultViewMapper<>(viewSetting, metaStorage, store, this), metaFieldDataSource));
  }

  public MetaFieldObjectExtractor() {
    registerFieldExtractor(MetaFieldStaticContent.Field.code, MetaField::getCode);
    registerFieldExtractor(MetaFieldStaticContent.Field.name, MetaField::getName);
    registerFieldExtractor(MetaFieldStaticContent.Field.column, MetaField::getColumn);
    registerFieldExtractor(MetaFieldStaticContent.Field.owner, field -> field.getOwner().getId());
    registerFieldExtractor(MetaFieldStaticContent.Field.type, field -> field.getType().getId());
    registerFieldExtractor(MetaFieldStaticContent.Field.linkToMetaClass,
        field -> field.getLinkToMetaClass() == null ? null : field.getLinkToMetaClass().getId());
    registerFieldExtractor(MetaFieldStaticContent.Field.backReferenceField,
        field -> field.getBackReferenceField() == null ? null : field.getBackReferenceField().getId());
  }

  @Override
  public String getId(MetaField obj, MetaClass metaClass) {
    return obj.getId();
  }
}
