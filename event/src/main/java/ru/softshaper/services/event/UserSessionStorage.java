package ru.softshaper.services.event;

import javax.websocket.Session;

/**
 * Created by Sunchise on 31.03.2017.
 */
public interface UserSessionStorage {
  void register(String userLogin, Session session);

  void unregister(String userLogin);

  void unregister(Session session);

  Session getSession(String userLogin);
}
