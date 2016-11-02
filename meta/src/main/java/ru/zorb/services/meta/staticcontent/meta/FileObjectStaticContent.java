package ru.zorb.services.meta.staticcontent.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.FileObjectDataSource;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.FileObject;

/**
 * Created by Sunchise on 08.09.2016.
 */
@Component
public class FileObjectStaticContent extends StaticContentBase {

  public static final String META_CLASS = "fileObject";

  public interface Field {

    String name = "name";

    String size = "size";

    String modificationDate = "modificationDate";

    String mimeType = "mimeType";

    String data = "data";
  }

  @Autowired
  public FileObjectStaticContent(@Qualifier("fileObjectDataSource") FileObjectDataSource dataSource) {
    super(META_CLASS, "Файл", FileObject.FILE_OBJECT.getName(), dataSource);
    FileObject table = FileObject.FILE_OBJECT;
    registerField(table.NAME,FieldType.STRING).setCode(Field.name).setName("Наименование");
    registerField(table.SIZE,FieldType.NUMERIC_INTEGER).setCode(Field.size).setName("Размер");
    registerField(table.MODIFY_DATE,FieldType.DATE).setCode(Field.modificationDate).setName("Дата модификации");
    registerField(table.MIME_TYPE,FieldType.STRING).setCode(Field.mimeType).setName("Тип");
    registerField(table.FILE_DATA,FieldType.FILE).setCode(Field.data).setName("Данные");
  }
}
