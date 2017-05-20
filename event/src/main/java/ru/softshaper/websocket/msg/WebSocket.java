package ru.softshaper.websocket.msg;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.softshaper.services.event.UserSessionStorage;
import ru.softshaper.services.security.token.Token;
import ru.softshaper.services.security.token.TokenManager;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/ws", configurator = WsEndpointConfigurator.class)
public class WebSocket {
  public static final Logger log = LoggerFactory.getLogger(WebSocket.class);


  @Autowired
  @Qualifier("EventBus")
  private EventBus eventBus;

  @Autowired
  private UserSessionStorage sessionStorage;

  @Autowired
  private TokenManager tokenManager;

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
    Object tokenId = session.getUserProperties().get("myresttoken");
    if (tokenId != null) {
      Token token = tokenManager.getToken(tokenId.toString());
      if (token != null) {
        sessionStorage.register(token.getLogin(), session);
      }
    }
    eventBus.register(new WebSocketMsgLisiner(session));
  }

  @OnMessage
  public String onMessage(String message, Session session) {
    switch (message) {
    case "quit":
      try {
        session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
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
    sessionStorage.unregister(session);
  }
}
