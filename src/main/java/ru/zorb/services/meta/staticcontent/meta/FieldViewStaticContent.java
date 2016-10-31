package ru.zorb.services.meta.staticcontent.meta;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.FieldView;

/**
 * @author ashek
 *
 */
@Component
public class FieldViewStaticContent extends StaticContentBase {

  public static final String META_CLASS = "fieldView";

  public interface Field {
    String tableField = "tableField";
  }

  @Autowired
  private FieldViewStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Представление поля", FieldView.FIELD_VIEW.getName(), dataSource);
    FieldView fvTable = FieldView.FIELD_VIEW;
    this.registerField(fvTable.COLUMN_CONTENT,FieldType.STRING).setName("Поле");
    this.registerField(fvTable.TABLE_CONTENT,FieldType.STRING).setName("Класс");
    this.registerField(fvTable.NUMBER,FieldType.NUMERIC_INTEGER).setName("Порядковый номер");
    this.registerField(fvTable.READONLY,FieldType.LOGICAL).setName("Только для чтения");
    this.registerField(fvTable.REQUIRED,FieldType.LOGICAL).setName("Обязательный");
    this.registerField(fvTable.TITLE,FieldType.STRING).setName("Заголовок");
    this.registerField(fvTable.TITLEFIELD,FieldType.LOGICAL).setName("Поле участвует в формировании заголовка");
    this.registerField(fvTable.TABLEFIELD,FieldType.LOGICAL).setCode(Field.tableField).setName("Поле отображается в таблице");
    this.registerField(fvTable.TYPEVIEWCODE,FieldType.LINK).setName("Представление").setLinkToMetaClass(FieldTypeViewStaticContent.META_CLASS);
  }
}
