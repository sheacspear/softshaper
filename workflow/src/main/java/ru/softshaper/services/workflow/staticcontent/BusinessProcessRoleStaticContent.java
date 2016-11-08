package ru.softshaper.services.workflow.staticcontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;
import ru.softshaper.services.meta.staticcontent.sec.SecUserStaticContent;
import ru.softshaper.storage.jooq.tables.BpRole;

/**
 * Created by Sunchise on 06.10.2016.
 */
@Component
public class BusinessProcessRoleStaticContent extends StaticContentBase {

  public static final String META_CLASS = "bpRole";

  public interface Field {
    String name = "name";
    String code = "code";
    String users = "users";
  }

  @Autowired
  public BusinessProcessRoleStaticContent(@Qualifier("data") ContentDataSource<?> dataSource) {
    super(META_CLASS, "Роль бизнес процесса", BpRole.BP_ROLE.getName(), dataSource);
    registerField(BpRole.BP_ROLE.CODE, FieldType.STRING).setCode(Field.code).setName("Код");
    registerField(BpRole.BP_ROLE.NAME, FieldType.STRING).setCode(Field.name).setName("Наименование");
    this.registerField(null,FieldType.MULTILINK)
        .setLinkToMetaClass(SecUserStaticContent.META_CLASS)
        .setName("Пользователи")
        .setCode(Field.users)
        .setNxMTableName("lnk_bp_role_2_sec_user");
  }
}
