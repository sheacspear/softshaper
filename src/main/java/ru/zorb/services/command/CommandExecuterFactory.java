package ru.zorb.services.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.concurrent.ThreadSafe;

import org.springframework.stereotype.Component;

/**
 *
 * Фабрика для выполнения команд
 *
 * @author ashek
 *
 */
@Component
@ThreadSafe
public class CommandExecuterFactory {

  /**
   * registered CommandExecuter
   */
  private Map<String, CommandExecuter<?, ?>> executers = new HashMap<String, CommandExecuter<?, ?>>();

  /**
   * Lock
   */
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * @param commandExecuter CommandExecuter
   */
  public void registerCommandExecuter(CommandExecuter<?, ?> commandExecuter) {
    WriteLock writeLock = lock.writeLock();
    try {
      writeLock.lock();
      executers.put(commandExecuter.getCommandName(), commandExecuter);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * CommandExecuter
   *
   * @param commandType
   * @return CommandExecuter
   */
  public CommandExecuter<?, ?> getCommandExecuter(String commandType) {
    ReadLock readLock = lock.readLock();
    try {
      readLock.lock();
      return executers.get(commandType);
    } finally {
      readLock.unlock();
    }
  }
}
