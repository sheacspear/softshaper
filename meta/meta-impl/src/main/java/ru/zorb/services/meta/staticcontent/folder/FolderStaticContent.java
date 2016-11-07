package ru.zorb.services.meta.staticcontent.folder;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.services.meta.staticcontent.meta.MetaClassStaticContent;
import ru.zorb.storage.jooq.tables.Folder;

@Component
public class FolderStaticContent extends StaticContentBase {

  public static final String META_CLASS = "folder";

  public interface Field {
    String name = "name";
    String parents = "parents";
    String children = "children";
    String type = "type";
    String contentObject = "contentObject";
    String metaClass = "metaClass";
  }

  @Autowired
  public FolderStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Папка", Folder.FOLDER.getName(), dataSource);
    Folder folderTable = Folder.FOLDER;
    this.registerField(folderTable.NAME,FieldType.STRING).setName("Наименование").setCode(Field.name);
    this.registerField(folderTable.TYPE,FieldType.STRING).setName("Тип").setCode(Field.type);
    this.registerField(folderTable.CONTENT_OBJECT,FieldType.UNIVERSAL_LINK).setName("Объект").setCode(Field.contentObject);
    this.registerField(folderTable.META_CLASS,FieldType.LINK).setName("Класс привязаных объектов").setCode(Field.metaClass).setLinkToMetaClass(MetaClassStaticContent.META_CLASS);
    this.registerField(null,FieldType.MULTILINK).setName("Родительская папка").setCode(Field.parents).setLinkToMetaClass(getCode()).setNxMTableName("folder_folder");
    this.registerField(null,FieldType.BACK_REFERENCE).setName("Дочерние папки").setCode(Field.children).setLinkToMetaClass(getCode()).setBackReferenceField(Field.parents);
  }
}
