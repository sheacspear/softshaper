package ru.zorb.web.rest.command;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.zorb.services.command.CommandResult;
import ru.zorb.services.command.ICommandService;
import ru.zorb.web.rest.command.bean.CommandBean;
import ru.zorb.web.rest.command.bean.CommandStatusBean;
import ru.zorb.web.rest.command.bean.ResultCommandBean;

/**
 * Контроллер работы
 */
@Path("/pr/commandservice")
@Produces("application/json")
@Consumes("application/json")
public class CommandServiceRest {

  /**
   * Сервис выполнения комманд
   */
  @Autowired
  private ICommandService commandService;

  /**
   * inject this from spring context
   */
  @PostConstruct
  public void init() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }
  /**
   * Отправка команды
   *
   * @return данные всех словарей
   */
  @POST
  @Path("/submitCommand")
  @Produces("application/json")
  @Consumes("application/json")
  public ResultCommandBean<Serializable> submitCommand(CommandBean commandBean) {
    CommandResult<Serializable> commandResult = commandService.executeCommand(commandBean.getId(),
        commandBean.getType(), (Map<String,Object> )commandBean.getObject());
    return new ResultCommandBean<Serializable>(commandBean,
        CommandStatusBean.valueOf(commandResult.getCommandStatus().name()), commandResult.getResult());
  }
}
