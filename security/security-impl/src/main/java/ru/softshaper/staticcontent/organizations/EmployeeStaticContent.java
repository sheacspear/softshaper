package ru.softshaper.staticcontent.organizations;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.datasource.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.staticcontent.meta.StaticContentBase;
import ru.softshaper.staticcontent.sec.SecUserStaticContent;
import ru.softshaper.storage.jooq.tables.Employee;

/**
 * Created by Sunchise on 06.09.2016.
 */
@Component
public class EmployeeStaticContent extends StaticContentBase {

  public static final String META_CLASS = "employee";

  public interface Field {
    String firstName = "firstName";
    String secondName = "secondName";
    String lastName = "lastName";
    String login = "login";
  }

  @Autowired
  public EmployeeStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Сотрудник", Employee.EMPLOYEE.getName(), dataSource);
    Employee table = Employee.EMPLOYEE;
    this.registerField(table.FIRST_NAME,FieldType.STRING).setCode(Field.firstName).setName("Имя");
    this.registerField(table.SECOND_NAME,FieldType.STRING).setCode(Field.secondName).setName("Отчество");
    this.registerField(table.LAST_NAME,FieldType.STRING).setCode(Field.lastName).setName("Фамилия");
    this.registerField(table.LOGIN,FieldType.LINK).setCode(Field.login).setName("Пользователь").setLinkToMetaClass(SecUserStaticContent.META_CLASS);
  }
}
