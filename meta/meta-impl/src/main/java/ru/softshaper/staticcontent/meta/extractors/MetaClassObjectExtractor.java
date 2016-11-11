package ru.softshaper.staticcontent.meta.extractors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;

/**
 * Created by Sunchise on 11.11.2016.
 */
@Component
@Qualifier(MetaClassStaticContent.META_CLASS)
public class MetaClassObjectExtractor extends AbstractObjectExtractor<MetaClass> {

  public MetaClassObjectExtractor() {
    registerFieldExtractor(MetaClassStaticContent.Field.code, MetaClass::getCode);
    registerFieldExtractor(MetaClassStaticContent.Field.name, MetaClass::getName);
    registerFieldExtractor(MetaClassStaticContent.Field.table, MetaClass::getTable);
    registerFieldExtractor(MetaClassStaticContent.Field.fields, MetaClass::getFields);
    registerFieldExtractor(MetaClassStaticContent.Field.fixed, MetaClass::isFixed);
    registerFieldExtractor(MetaClassStaticContent.Field.checkObjectSecurity, MetaClass::isCheckObjectSecurity);
    registerFieldExtractor(MetaClassStaticContent.Field.checkSecurity, MetaClass::isCheckSecurity);
  }

  @Override
  public String getId(MetaClass obj, MetaClass metaClass) {
    return obj.getId();
  }
}
