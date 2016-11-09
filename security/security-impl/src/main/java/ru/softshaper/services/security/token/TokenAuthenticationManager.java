package ru.softshaper.services.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Менеджер авторизации по токену
 */
@Service
public class TokenAuthenticationManager implements AuthenticationManager {


  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenManager tokenManager;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication instanceof TokenAuthentication) {
      return processAuthentication((TokenAuthentication) authentication);
    } else {
      authentication.setAuthenticated(false);
      return authentication;
    }
  }

  private TokenAuthentication processAuthentication(TokenAuthentication authentication) throws AuthenticationException {
    String tokenId = authentication.getToken();
    Token token = tokenManager.getToken(tokenId);
    if (token == null) {
      throw new AuthenticationServiceException("Invalid token");
    }
    if (token != Token.EXPIRED_TOKEN) {
      return buildFullTokenAuthentication(authentication, token);
    } else {
      throw new AuthenticationServiceException("Token expired date error");
    }
  }

  private TokenAuthentication buildFullTokenAuthentication(TokenAuthentication authentication, Token token) {
    User user = (User) userDetailsService.loadUserByUsername(token.getLogin());
    if (user.isEnabled()) {
      Collection<GrantedAuthority> authorities = user.getAuthorities();
      return new TokenAuthentication(authentication.getToken(), authorities, true, user);
    } else {
      throw new AuthenticationServiceException("User disabled");
    }
  }
}
