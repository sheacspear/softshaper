package ru.softshaper.web.admin.view.mapper.extractors.meta;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.staticcontent.meta.meta.FieldTypeViewStaticContent;
import ru.softshaper.web.admin.view.mapper.extractors.AbstractObjectExtractor;

@Component
@Qualifier(FieldTypeViewStaticContent.META_CLASS)
public class FieldTypeViewExtractor extends AbstractObjectExtractor<FieldTypeView> {

  public FieldTypeViewExtractor() {
    registerFieldExtractor(FieldTypeViewStaticContent.Field.code, FieldTypeView::getCode);
    registerFieldExtractor(FieldTypeViewStaticContent.Field.name, FieldTypeView::getName);
  }

  @Override
  public String getId(FieldTypeView obj, MetaClass metaClass) {
    return obj.getCode();
  }
}
