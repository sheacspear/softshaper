package ru.softshaper.services.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис получения нового токена по логину-паролю
 */
@Service
public class TokenService {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenManager tokenManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Token getToken(String login, String password) {
    if (login == null || password == null)
      return null;
    User user = (User) userDetailsService.loadUserByUsername(login);
    if (passwordEncoder.matches(password, user.getPassword())) {
      return tokenManager.createToken(login);
    } else {
      throw new RuntimeException("Authentication error");
    }
  }

}
