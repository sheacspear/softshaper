package ru.softshaper.staticcontent.meta.comparators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.comparators.ObjectComparator;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;

/**
 * Created by Sunchise on 09.11.2016.
 */
@Component
@Qualifier(MetaFieldStaticContent.META_CLASS)
public class MetaFieldComparator implements ObjectComparator<MetaField> {

  @Override
  public int compareField(MetaField metaField, MetaField o1, MetaField o2) {
    int compareResult = 0;
    switch (metaField.getCode()) {
      case MetaFieldStaticContent.Field.code:
        compareResult = o1.getCode().compareTo(o2.getCode());
        break;
      case MetaFieldStaticContent.Field.name:
        compareResult = o1.getName().compareTo(o2.getName());
        break;
      case MetaFieldStaticContent.Field.column:
        compareResult = compareStrings(o1.getColumn(), o2.getColumn());
        break;
      case MetaFieldStaticContent.Field.type:
        compareResult = o1.getType().getName().compareTo(o2.getType().getName());
        break;
      case MetaFieldStaticContent.Field.owner:
        compareResult = o1.getOwner().getName().compareTo(o2.getOwner().getName());
        break;
      case MetaFieldStaticContent.Field.linkToMetaClass:
        compareResult = o1.getLinkToMetaClass() == null ? o2.getLinkToMetaClass() == null ? 0 : -1 : o2.getLinkToMetaClass() == null ? 1 : o1.getLinkToMetaClass().getName().compareTo(o2.getLinkToMetaClass().getName());
        break;
      case MetaFieldStaticContent.Field.backReferenceField:
        compareResult = o1.getBackReferenceField() == null ?  o2.getBackReferenceField() == null ? 0 : -1 : o2.getBackReferenceField() == null ? 1 : o1.getBackReferenceField().getName().compareTo(o2.getBackReferenceField().getName());
        break;
    }
    return compareResult;
  }


  private int compareStrings(String s1, String s2) {
    return s1 == null ? s2 == null ? 0 : -1 : s2 == null ? 1 : s1.compareTo(s2);
  }
}
