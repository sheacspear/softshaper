package ru.softshaper.staticcontent.meta.extractors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.FieldTypeStaticContent;

/**
 * Created by Sunchise on 12.11.2016.
 */
@Component
@Qualifier(FieldTypeStaticContent.META_CLASS)
public class FieldTypeExtractor extends AbstractObjectExtractor<FieldType> {

  public FieldTypeExtractor() {
    registerFieldExtractor(FieldTypeStaticContent.Field.code, FieldType::getCode);
    registerFieldExtractor(FieldTypeStaticContent.Field.name, FieldType::getName);
  }

  @Override
  public String getId(FieldType obj, MetaClass metaClass) {
    return obj.getId().toString();
  }
}
