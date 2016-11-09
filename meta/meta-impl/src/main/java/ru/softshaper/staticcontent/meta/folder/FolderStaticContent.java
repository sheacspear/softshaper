package ru.softshaper.staticcontent.meta.folder;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.staticcontent.meta.StaticContentBase;
import ru.softshaper.staticcontent.meta.meta.MetaClassStaticContent;
import ru.softshaper.storage.jooq.tables.Folder;

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
