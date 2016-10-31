package ru.zorb.security.token.storage;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import ru.zorb.security.token.ClientType;
import ru.zorb.security.token.Token;
import ru.zorb.storage.jooq.tables.pojos.SecToken;

/**
 * Конвертер токена безопасности и бина токена в базе
 */
@Service
public class DbTokenConverter {

  public Token toSecurityToken(SecToken tokenBean) {
    return new Token(tokenBean.getId(), tokenBean.getLogin(), ClientType.USER,
        tokenBean.getCreateDate(), tokenBean.getExpirationDate());
  }

  public SecToken toDbToken(Token token) {
    return new SecToken(token.getId(), new Timestamp(token.getCreateDate().getTime()), new Timestamp(token.getExpirationDate().getTime()), token.getLogin());
  }

}
