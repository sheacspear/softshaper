package test.event;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ru.zorb.conf.EventConfig.class })
public class EventTest {

  @Autowired
  private EventBus eventBus;

  @Test
  @Ignore
  public void before() {
    eventBus.register(new Lisiner());
    eventBus.post("msg");
  }

  private static class Lisiner {

    @Subscribe
    public void msg(String msg) {
      System.out.println("msg " + msg);
    }

  }

}
