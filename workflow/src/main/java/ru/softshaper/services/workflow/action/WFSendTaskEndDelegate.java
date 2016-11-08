package ru.softshaper.services.workflow.action;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import ru.softshaper.web.msg.websocket.MsgBean;
import rx.Observable;
import rx.schedulers.Schedulers;

@Component("msgSendEndTask")
public class WFSendTaskEndDelegate implements JavaDelegate {

  @Autowired
  private EventBus eventBus;

  public void execute(DelegateExecution execution)  {
    Observable.just(new MsgBean("Бизнес процесс по документу закончен", "Бизнес процесс по документу закончен")).subscribeOn(Schedulers.newThread()).subscribe(msg->{
      try {
        Thread.sleep(5000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      eventBus.post(msg);
    });
    System.out.println(" java executer WFSendTaskEndDelegate" + execution.getCurrentActivityName());
  }
}
