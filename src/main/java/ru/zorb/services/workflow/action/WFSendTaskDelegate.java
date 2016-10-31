package ru.zorb.services.workflow.action;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import ru.zorb.web.msg.websocket.MsgBean;
import rx.Observable;
import rx.schedulers.Schedulers;

@Component("msgSendTask")
public class WFSendTaskDelegate implements JavaDelegate {

  @Autowired
  private EventBus eventBus;

  public void execute(DelegateExecution execution) throws Exception {

    Observable.just(new MsgBean("Новая задача согласовать документ!", "Новая задача согласовать документ!")).subscribeOn(Schedulers.newThread()).subscribe(msg->{
      try {
        Thread.sleep(5000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      eventBus.post(msg);
    });

    System.out.println(" java executer WFSendTaskDelegate" + execution.getCurrentActivityName());
  }
}
