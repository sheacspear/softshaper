package ru.zorb.services.meta.staticcontent.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;

@Component
public class FieldTypeStaticContent extends StaticContentBase {

  public static final String META_CLASS = "fieldType";

  public interface Field {
    String code = "code";
    String name = "name";
  }

  @Autowired
  private FieldTypeStaticContent(@Qualifier("fieldType") ContentDataSource<FieldType> dataSource) {
    super(META_CLASS, "Тип поля", null, dataSource);
    this.registerField(null,FieldType.STRING).setName("Код").setCode(Field.code);
    this.registerField(null,FieldType.STRING).setName("Название").setCode(Field.name);
  }
}
