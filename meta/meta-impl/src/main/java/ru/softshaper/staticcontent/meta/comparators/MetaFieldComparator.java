package ru.softshaper.staticcontent.meta.comparators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.ObjectExtractor;
import ru.softshaper.services.meta.comparators.ObjectComparator;
import ru.softshaper.staticcontent.meta.meta.MetaFieldStaticContent;

/**
 * Created by Sunchise on 09.11.2016.
 */
@Component
@Qualifier(MetaFieldStaticContent.META_CLASS)
public class MetaFieldComparator extends AbstractObjectComparator<MetaField> implements ObjectComparator<MetaField> {

  @Autowired
  public MetaFieldComparator(@Qualifier(MetaFieldStaticContent.META_CLASS) ObjectExtractor<MetaField> objectExtractor) {
    super(objectExtractor);
  }
}
