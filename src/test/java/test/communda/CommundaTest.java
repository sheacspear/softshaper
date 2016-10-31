package test.communda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.zorb.conf.EventConfig;
import ru.zorb.conf.MetaConfig;
import ru.zorb.services.workflow.action.UserService;
import test.conf.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MetaConfig.class,ru.zorb.conf.CommundaConfig.class, TestConfig.class,EventConfig.class })
@Transactional
public class CommundaTest {

  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private ManagementService managementService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private UserService userService;
  @Autowired
  private FormService formService;


  //@Test
   @Ignore
  @Rollback
  public void test() {
    // параметры запуска
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("class", "docClass");
    dataMap.put("obj.attr.id", "12");
    // Список доступных бизнес процессов,с категорией docClass
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionCategory("docClass").latestVersion().versionTag("test3").singleResult();
    // Фильтрация условий запуска бизнес процессов
    // processDefinitionList = checkCond(processDefinitionList, dataMap);
    if (processDefinition != null) {
      // for (ProcessDefinition processDefinition : processDefinitionList) {
      // Запуск бизнес процессов
      startProcess(processDefinition, dataMap);
      // }
    }

  }

  private void startProcess(ProcessDefinition processDefinition, Map<String, Object> dataMap) {
    // Параметры для старта инстанца бизнес процесса
    dataMap.put("data", 2);
    // Создание нового инстанца бизнес процесса для объекта с идентификатором "123@doc
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), "123@doc", dataMap);
    System.out.println("startProcess");
    // Получение заданий для пользователей,тут dsl может быть фильтр
    List<Task> tasks = taskService.createTaskQuery().processDefinitionId(processDefinition.getId()).list();
    for (Task task : tasks) {
      System.out.println("alexander getTask " + task.getName());
      // Взял задачу в работу,теперь ее никто не может взять
      taskService.claim(task.getId(), "alexander");
      // завершил задачу
      taskService.complete(task.getId());
    }
    // Задачи назначенные для пользователя alex
    List<Task> alexTasks = taskService.createTaskQuery().taskAssignee("alex").list();

    for (Task alexTask : alexTasks) {
      System.out.println("alex getTask " + alexTask.getName());
      // Параметры вводит пользователь,типа на форме
      dataMap.put("data", 0);
      dataMap.put("status", -2);
      // Закрыл задачу
      taskService.complete(alexTask.getId(), dataMap);
    }
    // Задачи назначенные для пользователя lopos
    List<Task> loposTasks = taskService.createTaskQuery().taskAssignee("lopos").list();
    if (loposTasks != null) {
      for (Task loposTask : loposTasks) {
        if (loposTask != null) {
          // Закрыл задачу
          System.out.println("lopos getTask " + loposTask.getName());
          taskService.complete(loposTask.getId());
        }
      }
    }
    // Задачи назначенные для группы пользователей control
    List<Task> controlTasks = taskService.createTaskQuery().taskCandidateGroup("control").list();
    for (Task controlTask : controlTasks) {
      // Закрыл задачу
      System.out.println("control getTask " + controlTask.getName());
      taskService.complete(controlTask.getId());
    }
    // Оставшиеся задачи пользователей,не завершенные
    System.out.println(taskService.createTaskQuery().list().size());
    // Не завершенные бизнес процесссы
    System.out.println(runtimeService.createProcessInstanceQuery().active().list().size());
  }

  /**
   * Метод фильтрует список доступных бизнес процеччов,на основании сферического коня в вакууме
   */
  private List<ProcessDefinition> checkCond(List<ProcessDefinition> processDefinitionList, Map<String, Object> dataMap) {
    List<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
    if (processDefinitionList != null) {
      for (ProcessDefinition processDefinition : processDefinitionList) {
        String name = processDefinition.getName();
        String[] conds = name.split(";");
        boolean validate = true;
        for (String cond : conds) {
          String[] keyVal = cond.split("==");
          Object val = dataMap.get(keyVal[0]);
          if (val == null || !val.toString().equals(keyVal[1])) {
            validate = false;
            break;
          }
        }
        if (validate) {
          result.add(processDefinition);
        }
      }
    }
    return result;
  }
}
