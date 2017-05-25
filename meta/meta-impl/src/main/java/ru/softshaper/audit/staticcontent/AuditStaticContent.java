package ru.softshaper.audit.staticcontent;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.staticcontent.meta.StaticContentBase;
import ru.softshaper.storage.jooq.tables.Audit;


@Component
public class AuditStaticContent extends StaticContentBase {
  public static final String META_CLASS = "audit";

  public interface Field {
    String date = "date";
    String type = "type";
    String description = "description";
    String user = "user";
    String object = "object";
  }

  @Autowired
  public AuditStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Журнал изменений объектов", Audit.AUDIT.getName(), dataSource);
    this.registerField(Audit.AUDIT.DATE, FieldType.DATE).setName("Дата").setCode(Field.date);
    this.registerField(Audit.AUDIT.TYPE, FieldType.STRING).setName("Тип").setCode(Field.type);
    this.registerField(Audit.AUDIT.DESCRIPTION, FieldType.STRING).setName("Описание").setCode(Field.description);
    this.registerField(Audit.AUDIT.USER, FieldType.STRING).setName("Пользователь").setCode(Field.user);
    this.registerField(Audit.AUDIT.OBJECT_LINK, FieldType.UNIVERSAL_LINK).setName("Объект").setCode(Field.object);
  }
}
