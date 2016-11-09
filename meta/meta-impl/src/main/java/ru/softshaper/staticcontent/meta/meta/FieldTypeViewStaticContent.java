package ru.softshaper.staticcontent.meta.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.staticcontent.meta.StaticContentBase;

/**
 * Created by Sunchise on 11.10.2016.
 */
@Component
public class FieldTypeViewStaticContent extends StaticContentBase {

  public static final String META_CLASS = "fieldTypeView";

  public interface Field {
    String code = "code";
    String name = "name";
  }

  @Autowired
  public FieldTypeViewStaticContent(@Qualifier("fieldTypeView") ContentDataSource<?> dataSource) {
    super(META_CLASS, "Представление типа поля", null, dataSource);
    registerField(null, FieldType.STRING).setCode(Field.code).setName("Код");
    registerField(null, FieldType.STRING).setCode(Field.name).setName("Наименование");
  }
}
