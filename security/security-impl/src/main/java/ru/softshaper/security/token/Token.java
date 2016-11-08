package ru.softshaper.security.token;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Токен
 */
public class Token {

  static Token EXPIRED_TOKEN = new Token("") {
    @Override
    public Date getExpirationDate() {
      return new Date(0L);
    }
  };

  private final String id;

  private final String login;

  private final ClientType clientType;

  private final Date createDate;

  private final Date expirationDate;

  public Token(String login) {
    id = UUID.randomUUID().toString();
    this.login = login;
    clientType = ClientType.USER;
    Calendar calendar = Calendar.getInstance();
    createDate = calendar.getTime();
    //calendar.add(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    expirationDate = calendar.getTime();
  }

  public Token(String id, String login, ClientType clientType, Date createDate, Date expirationDate) {
    this.id = id;
    this.login = login;
    this.clientType = clientType;
    this.createDate = createDate;
    this.expirationDate = expirationDate;
  }

  public String getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public ClientType getClientType() {
    return clientType;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }
}
