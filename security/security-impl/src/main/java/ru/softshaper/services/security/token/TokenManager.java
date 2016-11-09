package ru.softshaper.services.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.softshaper.services.security.token.storage.TokenStorage;

import java.util.Date;

/**
 * Менеджер для работы с токенами
 */
@Service
class TokenManager {

  @Autowired
  private TokenStorage tokenStorage;

  public Token createToken(String login) {
    Token token = new Token(login);
    tokenStorage.addToken(token);
    return token;
  }

  public Token getToken(String tokenId) {
    Token token = tokenStorage.getToken(tokenId);
    if (token == null) {
      return null;
    } else if (token.getExpirationDate().before(new Date())) {
      tokenStorage.removeToken(token);
      return Token.EXPIRED_TOKEN;
    }
    return token;
  }

}
