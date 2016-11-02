package ru.zorb.services.meta.staticcontent.sec;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.Users;

/**
 * @author ashek
 *
 */
@Component
public class SecUserStaticContent extends StaticContentBase {

  public static final String META_CLASS = "secUser";

  public interface Field {
    String login = "login";
    String password = "password";
    String roles = "roles";
  }

  @Autowired
  private SecUserStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Пользователь", Users.USERS.getName(), dataSource);
    this.registerField(Users.USERS.USERNAME,FieldType.STRING).setName("Логин").setCode(Field.login);
    this.registerField(Users.USERS.PASSWORD,FieldType.STRING).setName("Пароль").setCode(Field.password);
    this.registerField(null,FieldType.MULTILINK).setLinkToMetaClass("secRole").setName("Роли").setCode(Field.roles).setNxMTableName("users_roles");
  }
}
