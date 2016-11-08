package ru.softshaper.services.meta.staticcontent.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;

/**
 * @author ashek
 *
 */
@Component
public class MetaFieldStaticContent extends StaticContentBase {

  public static final String META_CLASS = "metaField";

  public interface Field {
    String code = "code";
    String name = "name";
    String column = "column";
    String type = "type";
    String owner = "owner";
    String linkToMetaClass = "linkToMetaClass";
    String backReferenceField = "backReferenceField";
  }

  @Autowired
  private MetaFieldStaticContent(@Qualifier("metaField") ContentDataSource<MetaField> fieldClassDataSource) {
    super(META_CLASS, "Поле динамического контента", ru.softshaper.storage.jooq.tables.DynamicField.DYNAMIC_FIELD.getName(), fieldClassDataSource);
    ru.softshaper.storage.jooq.tables.DynamicField dfTable = ru.softshaper.storage.jooq.tables.DynamicField.DYNAMIC_FIELD;
    this.registerField(dfTable.CODE,FieldType.STRING).setName("Код");
    this.registerField(dfTable.NAME,FieldType.STRING).setName("Наименование");
    this.registerField(dfTable.COLUMN_FIELD,FieldType.STRING).setCode("column").setName("Колонка");
    this.registerField(dfTable.TYPE_FIELD,FieldType.LINK).setCode("type").setName("Тип").setLinkToMetaClass(FieldTypeStaticContent.META_CLASS);
    this.registerField(dfTable.DYNAMIC_CONTENT_ID,FieldType.LINK).setCode(Field.owner).setName("Владелец").setLinkToMetaClass(MetaClassStaticContent.META_CLASS);
    this.registerField(dfTable.LINK_TO_DYNAMIC_CONTENT,FieldType.LINK).setCode(Field.linkToMetaClass).setName("Ссылка на класс").setLinkToMetaClass(MetaClassStaticContent.META_CLASS);
    this.registerField(dfTable.BACK_REFERENCE_FIELD,FieldType.LINK).setCode(Field.backReferenceField).setName("Поле обратной ссылки").setLinkToMetaClass(MetaFieldStaticContent.META_CLASS);
  }
}
