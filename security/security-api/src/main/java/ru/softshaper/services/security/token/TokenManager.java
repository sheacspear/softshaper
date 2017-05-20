package ru.softshaper.services.security.token;

/**
 * Created by Sunchise on 04.04.2017.
 */
public interface TokenManager {
  Token createToken(String login);

  Token getToken(String tokenId);
}
