package ru.softshaper.services.meta.staticcontent.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;
import ru.softshaper.storage.jooq.tables.DynamicContent;

@Component
public class MetaClassStaticContent extends StaticContentBase {

  public static final String META_CLASS = "metaClass";

  public interface Field {
    String code = "code";
    String name = "name";
    String table = "table";
    String checkSecurity = "checkSecurity";
    String checkObjectSecurity = "checkObjectSecurity";
    String fixed = "fixed";
    String fields = "fields";
  }

  @Autowired
  private MetaClassStaticContent(@Qualifier("metaClass") ContentDataSource<MetaClass> metaClassDataSource) {
    super(META_CLASS, "Динамический контент", DynamicContent.DYNAMIC_CONTENT.getName(), metaClassDataSource);
    DynamicContent dcTable = DynamicContent.DYNAMIC_CONTENT;
    this.registerField(dcTable.CODE,FieldType.STRING).setCode(Field.code).setName("Код");
    this.registerField(dcTable.NAME,FieldType.STRING).setCode(Field.name).setName("Наименование");
    this.registerField(dcTable.TABLE_CONTENT,FieldType.STRING).setCode(Field.table).setName("Таблица");
    this.registerField(dcTable.CHECKSECURITY,FieldType.LOGICAL).setCode(Field.checkSecurity).setName("Проверять безопасность на класс");
    this.registerField(dcTable.CHECKOBJECTSECURITY,FieldType.LOGICAL).setCode(Field.checkObjectSecurity).setName("Проверять безопасность объектов");
    this.registerField(dcTable.FIXED,FieldType.LOGICAL).setCode(Field.fixed).setName("Заперт динамического изменения");
    this.registerField(null,FieldType.BACK_REFERENCE).setCode(Field.fields).setName("Поля").setLinkToMetaClass(MetaFieldStaticContent.META_CLASS).setBackReferenceField(MetaFieldStaticContent.Field.owner);
  }
}
