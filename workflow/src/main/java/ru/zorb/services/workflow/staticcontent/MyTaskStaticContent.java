package ru.zorb.services.workflow.staticcontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.staticcontent.StaticContentBase;

/**
 * Created by Sunchise on 06.10.2016.
 */
@Component
public class MyTaskStaticContent extends StaticContentBase {


  public static final String META_CLASS = "myTask";

  public interface Field {
    String name = "name";
    String description = "description";
    String priority = "priority";
    String owner = "owner";
    String assignee = "assignee";
    String linkedObject = "linkedObject";
    String createTime = "createTime";
    String dueDate = "dueDate";
    String followUpDate = "followUpDate";
    String suspended = "suspended";
  }


  @Autowired
  public MyTaskStaticContent(@Qualifier("myTask") ContentDataSource<?> dataSource) {
    super(META_CLASS, "Мои задачи", null, dataSource);
    registerField(null, FieldType.STRING).setCode(Field.name).setName("Наименование");
    registerField(null, FieldType.STRING).setCode(Field.description).setName("Описание");
    registerField(null, FieldType.STRING).setCode(Field.priority).setName("Приоритет");
    registerField(null, FieldType.STRING).setCode(Field.owner).setName("Владелец");
    registerField(null, FieldType.STRING).setCode(Field.assignee).setName("Назначен");
    registerField(null, FieldType.UNIVERSAL_LINK).setCode(Field.linkedObject).setName("Объект");
    registerField(null, FieldType.DATE).setCode(Field.createTime).setName("Создан");
    registerField(null, FieldType.DATE).setCode(Field.dueDate).setName("Взят в работу");
    registerField(null, FieldType.STRING).setCode(Field.followUpDate).setName("followUpDate");
    registerField(null, FieldType.LOGICAL).setCode(Field.suspended).setName("Выполнен");
  }
}
