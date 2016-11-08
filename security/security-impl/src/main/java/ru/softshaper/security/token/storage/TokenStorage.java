package ru.softshaper.security.token.storage;

import ru.softshaper.security.token.Token;

/**
 * Created by Sunchise on 06.07.2016.
 */
public interface TokenStorage {
  Token getToken(String tokenId);

  void addToken(Token token);

  void removeToken(Token token);

}
