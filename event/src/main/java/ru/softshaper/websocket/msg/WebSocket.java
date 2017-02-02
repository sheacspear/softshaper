package ru.softshaper.websocket.msg;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.common.eventbus.EventBus;

@ServerEndpoint(value = "/ws")
public class WebSocket {
  public static final Logger log = LoggerFactory.getLogger(WebSocket.class);


  @Autowired
  @Qualifier("EventBus")
  private EventBus eventBus;

  /**
   * inject this from spring context
   */
  //@PostConstruct
  //public WebSocket() {

  //}


  public WebSocket() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }

  @OnOpen
  public void onOpen(Session session) {
    log.info("Connected ... " + session.getId());
   //try {
     // session.getBasicRemote().sendText("connect");
   // } catch (IOException e) {
    //  throw new RuntimeException(e);
    //}
    eventBus.register(new WebSocketMsgLisiner(session));
  }

  @OnMessage
  public String onMessage(String message, Session session) {
    switch (message) {
    case "quit":
      try {
        session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      break;
    }
    return message;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
  }
}
