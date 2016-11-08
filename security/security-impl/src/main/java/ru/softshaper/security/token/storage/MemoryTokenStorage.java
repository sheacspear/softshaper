package ru.softshaper.security.token.storage;

import com.google.common.collect.Maps;
import ru.softshaper.security.token.Token;

import java.util.Map;

/**
 * Хранилище токенов
 * //todo: подумать, что делать с просрочеными токенами, которыми никто не пользовался уже давно
 */
public class MemoryTokenStorage implements TokenStorage {
  private final Map<String, Token> tokens = Maps.newHashMap();
  private final Map<String, Token> tokensByLogin = Maps.newHashMap();

  @Override
  public Token getToken(String tokenId) {
    return tokens.get(tokenId);
  }

  @Override
  public void addToken(Token token) {
    Token oldToken = tokensByLogin.get(token.getLogin());
    if (oldToken != null) {
      //если уже был токен для этого пользователя, значит он больше не нужен. удаляем его
      removeToken(oldToken);
    }
    tokensByLogin.put(token.getLogin(), token);
    tokens.put(token.getId(), token);
  }

  @Override
  public void removeToken(Token token) {
    tokensByLogin.remove(token.getLogin());
    tokens.remove(token.getId());
  }


}
