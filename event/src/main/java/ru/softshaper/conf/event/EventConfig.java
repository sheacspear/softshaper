package ru.softshaper.conf.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * EventConfig for Event Bus
 *
 * @author ashek
 *
 */
@Configuration
@ComponentScan({"ru.softshaper.services.event"})
public class EventConfig {

  private static final Logger log = LoggerFactory.getLogger(EventConfig.class);
  
  /**
   * @return guava event bus
   */
  @Bean(name = "EventBus")
  public EventBus eventBus() {   
    
    return new EventBus(new SubscriberExceptionHandler(){

      @Override
      public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error(exception.getMessage() ,exception);
        //log.error(context.toString());
      }
      
    }); // guava event bus
  }



}
