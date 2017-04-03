package ru.softshaper.services.event;

import javax.websocket.Session;

/**
 * Created by Sunchise on 31.03.2017.
 */
public interface UserSessionStorage {
  void register(String userId, Session session);

  void unregister(String userId);

  void unregister(Session session);

  Session getSession(String userId);
}
