package ru.softshaper.services.command;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.softshaper.storage.jooq.tables.daos.CommandDao;
import ru.softshaper.storage.jooq.tables.pojos.Command;

/**
 * @author ashek
 *
 */
@Component
public class CommandService implements ICommandService {

  /**
   * Фабрика для выполнения команд
   */
  @Autowired
  private CommandExecuterFactory commandExecuterFactory;
  /**
   * Command log DAOs
   */
  @Autowired
  private CommandDao commandLogRepositroy;

  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.command.ICommandService#executeCommand(java.lang.String, java.lang.String, java.lang.Object)
   */
  @Override
  public <T extends Serializable, K extends Serializable> CommandResult<T> executeCommand(String id, String type,
      K object) {
    @SuppressWarnings("unchecked")
    // получение обработчика
    CommandExecuter<T, K> commandExecuter = (CommandExecuter<T, K>) commandExecuterFactory.getCommandExecuter(type);
    // выполнение
    CommandResult<T> execute = commandExecuter.execute(id, object);
    // Логирование
    commandLogRepositroy
        .insert(new Command(null, object.toString(), execute != null ? execute.getResult().toString() : null,
            execute != null ? execute.getCommandStatus().name() : null, type, id));

    return execute;
  }


  /*
   * (non-Javadoc)
   *
   * @see ru.softshaper.services.command.ICommandService#executeCommand(java.lang.String, java.lang.String, java.lang.Object)
   */
  @Override
  public <T extends Serializable, K extends Serializable> CommandResult<T> executeCommand(String id, String type,
      Map<String, Object> object) {
    @SuppressWarnings("unchecked")
    // получение обработчика
    CommandExecuter<T, K> commandExecuter = (CommandExecuter<T, K>) commandExecuterFactory.getCommandExecuter(type);
    // выполнение
    CommandResult<T> execute = commandExecuter.execute(id, commandExecuter.loadFromMapRequest(object));
    // Логирование
    commandLogRepositroy
        .insert(new Command(null, object.toString(), execute != null ? execute.getResult().toString() : null,
            execute != null ? execute.getCommandStatus().name() : null, type, id));

    return execute;
  }
}
