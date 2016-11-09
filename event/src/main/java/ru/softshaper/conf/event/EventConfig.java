package ru.softshaper.conf.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

/**
 * EventConfig for Event Bus
 *
 * @author ashek
 *
 */
@Configuration
@ComponentScan({"ru.softshaper.services.event"})
public class EventConfig {

  /**
   * @return guava event bus
   */
  @Bean(name = "EventBus")
  public EventBus eventBus() {
    return new EventBus(); // guava event bus
  }



}
