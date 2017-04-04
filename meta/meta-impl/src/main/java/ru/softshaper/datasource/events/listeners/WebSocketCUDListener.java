package ru.softshaper.datasource.events.listeners;

import com.google.common.eventbus.Subscribe;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.services.event.UserSessionStorage;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by Sunchise on 04.04.2017.
 */
public class WebSocketCUDListener {

  private final UserSessionStorage userSessionStorage;

  public WebSocketCUDListener(UserSessionStorage userSessionStorage) {
    this.userSessionStorage = userSessionStorage;
  }

  @Subscribe
  public void updateMessageListener(ObjectUpdated event) {
    Session session = userSessionStorage.getSession(event.getUserLogin());
    if (session != null) {
      try {
        session.getBasicRemote().sendText("Объект обновлён");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
