package ru.zorb.services.meta.staticcontent.organizations;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.Position;

/**
 * Created by Sunchise on 06.09.2016.
 */
@Component
public class PositionStaticContent extends StaticContentBase {

  public static final String META_CLASS = "position";

  public interface Field {
    String name = "name";
  }

  @Autowired
  public PositionStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Должность", Position.POSITION.getName(), dataSource);
    Position table = Position.POSITION;
    registerField(table.NAME,FieldType.STRING).setCode(Field.name).setName("Наименование");
  }
}
