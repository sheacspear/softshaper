package ru.softshaper.staticcontent.organizations;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.staticcontent.meta.StaticContentBase;
import ru.softshaper.storage.jooq.tables.EstablishedPost;

/**
 * Created by Sunchise on 06.09.2016.
 */
@Component
public class EstablishedPostStaticContent extends StaticContentBase {

  public static final String META_CLASS = "establishedPost";

  public interface Field {
    String position = "position";
    String organization = "organization";
    String employee = "employee";
  }

  @Autowired
  public EstablishedPostStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Штатная единица", EstablishedPost.ESTABLISHED_POST.getName(), dataSource);
    EstablishedPost table = EstablishedPost.ESTABLISHED_POST;
    registerField(table.EMPLOYEE,FieldType.LINK).setCode(Field.employee).setName("Сотрудник").setLinkToMetaClass(EmployeeStaticContent.META_CLASS);
    registerField(table.ORGANIZATION,FieldType.LINK).setCode(Field.organization).setName("Организация").setLinkToMetaClass(OrganizationStaticContent.META_CLASS);
    registerField(table.POSITION,FieldType.LINK).setCode(Field.position).setName("Должность").setLinkToMetaClass(PositionStaticContent.META_CLASS);
  }

}
