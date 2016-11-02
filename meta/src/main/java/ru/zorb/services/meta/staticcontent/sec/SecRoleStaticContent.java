package ru.zorb.services.meta.staticcontent.sec;

import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.Roles;

/**
 * @author ashek
 *
 */
@Component
public class SecRoleStaticContent extends StaticContentBase {

  public static final String META_CLASS = "secRole";

  @Autowired
  private SecRoleStaticContent(@Qualifier("data") ContentDataSource<Record> dataSource) {
    super(META_CLASS, "Роль", Roles.ROLES.getName(), dataSource);
		this.registerField(Roles.ROLES.ROLE, FieldType.STRING).setName("Роль");
  }
}
