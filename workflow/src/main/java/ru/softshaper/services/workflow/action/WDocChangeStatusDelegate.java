package ru.softshaper.services.workflow.action;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.softshaper.services.meta.ContentDataSource;

@Component("changeDocTask")
public class WDocChangeStatusDelegate implements JavaDelegate {

  @Autowired
  @Qualifier("data")
  private ContentDataSource<Record> dataSource;

  public void execute(DelegateExecution execution) throws Exception {
    String businessKey = execution.getBusinessKey();
    String[] arg = businessKey.split("@");
    String key = arg[0];
    String claszz = arg[1];
    Map<String, Object> values = new HashMap<String, Object>();
    values.put("status", 3);
    dataSource.updateObject(claszz, key, values);
    System.out.println(" java executer changeDocTask " + execution.getCurrentActivityName());
  }
}
