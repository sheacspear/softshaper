package ru.softshaper.rest.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.softshaper.rest.login.bean.Login;
import ru.softshaper.services.security.token.Token;
import ru.softshaper.services.security.token.TokenService;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Контроллер работы
 */
@Path("/pb/login")
@Produces("application/json")
@Consumes("application/json")
public class LoginServiceRest {

	/**
	 *
	 */
	@Autowired
	private TokenService tokenService;

	/**
	 *
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * @param login
	 * @return
	 */
	@POST
	@Path("/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Login submitCommand(Login login) {
		Token token = tokenService.getToken(login.getLogin(), login.getPassword());
		login.setToken(token.getId());
		return login;
	}
}
