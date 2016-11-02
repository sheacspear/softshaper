package ru.zorb.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

/**
 * EventConfig for Event Bus
 *
 * @author ashek
 *
 */
@Configuration
public class EventConfig {

  /**
   * @return guava event bus
   */
  @Bean(name = "EventBus")
  public EventBus eventBus() {
    return new EventBus(); // guava event bus
  }



}
