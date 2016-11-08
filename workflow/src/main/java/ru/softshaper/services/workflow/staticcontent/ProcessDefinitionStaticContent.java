package ru.softshaper.services.workflow.staticcontent;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.softshaper.services.meta.ContentDataSource;
import ru.softshaper.services.meta.FieldType;
import ru.softshaper.services.meta.staticcontent.StaticContentBase;

/**
 * Created by Sunchise on 09.10.2016.
 */
@Component
public class ProcessDefinitionStaticContent extends StaticContentBase {


  public static final String META_CLASS = "processDefinition";

  public interface Field {
    String description = "description";
    String suspended = "suspended";
    String versionTag = "versionTag";
    String category = "category";
    String name = "name";
    String key = "key";
    String version = "version";
    String resourceName = "resourceName";
    String deploymentId = "deploymentId";
    String diagramResourceName = "diagramResourceName";
    String tenantId = "tenantId";
  }

  @Autowired
  public ProcessDefinitionStaticContent(@Qualifier("processDefinition") ContentDataSource<ProcessDefinition> dataSource) {
    super(META_CLASS, "Бизнес процесс", null, dataSource);
    registerField(null, FieldType.STRING).setCode(Field.description).setName("Описание");
    registerField(null, FieldType.STRING).setCode(Field.versionTag).setName("Версия");
    registerField(null, FieldType.LOGICAL).setCode(Field.suspended).setName("suspended");
    registerField(null, FieldType.STRING).setCode(Field.category).setName("category");
    registerField(null, FieldType.STRING).setCode(Field.name).setName("name");
    registerField(null, FieldType.STRING).setCode(Field.key).setName("key");
    registerField(null, FieldType.NUMERIC_INTEGER).setCode(Field.version).setName("version");
    registerField(null, FieldType.STRING).setCode(Field.resourceName).setName("resourceName");
    registerField(null, FieldType.STRING).setCode(Field.deploymentId).setName("deploymentId");
    registerField(null, FieldType.STRING).setCode(Field.diagramResourceName).setName("diagramResourceName");
    registerField(null, FieldType.STRING).setCode(Field.tenantId).setName("tenantId");
  }
}
