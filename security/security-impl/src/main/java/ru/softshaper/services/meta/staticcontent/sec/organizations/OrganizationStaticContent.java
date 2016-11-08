package ru.softshaper.services.meta.staticcontent.sec.organizations;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;
import ru.softshaper.storage.jooq.tables.Organization;

/**
 * Created by Sunchise on 06.09.2016.
 */
@Component
public class OrganizationStaticContent extends StaticContentBase {

  public static final String META_CLASS = "organization";

  public interface Field {
    String name = "name";
    String fullName = "fullName";
  }

  @Autowired
  public OrganizationStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Организация", Organization.ORGANIZATION.getName(), dataSource);
    Organization table = Organization.ORGANIZATION;
    registerField(table.FULL_NAME,FieldType.STRING).setCode(Field.fullName).setName("Полное наименование");
    registerField(table.NAME,FieldType.STRING).setCode(Field.name).setName("Наименование");
  }
}
