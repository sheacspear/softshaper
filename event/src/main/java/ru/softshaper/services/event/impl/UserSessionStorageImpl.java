package ru.softshaper.services.event.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.softshaper.services.event.UserSessionStorage;

import javax.websocket.Session;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class UserSessionStorageImpl implements UserSessionStorage {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  private final BiMap<String, Session> storage = HashBiMap.create();

  @Override
  public void register(String userLogin, Session session) {
    lock.writeLock().lock();
    storage.put(userLogin, session);
    lock.writeLock().unlock();
  }

  @Override
  public void unregister(String userLogin) {
    lock.writeLock().lock();
    storage.remove(userLogin);
    lock.writeLock().unlock();
  }

  public void unregister(Session session) {
    lock.writeLock().lock();
    storage.inverse().remove(session);
    lock.writeLock().unlock();
  }

  @Override
  public Session getSession(String userLogin) {
    lock.readLock().lock();
    Session session = storage.get(userLogin);
    lock.readLock().unlock();
    return session;
  }

}
