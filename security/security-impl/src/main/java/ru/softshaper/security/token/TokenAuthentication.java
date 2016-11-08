package ru.softshaper.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Класс данных аутентификации по токену
 */
class TokenAuthentication implements Authentication {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
  private Collection<? extends GrantedAuthority> authorities;
  private boolean isAuthenticated;
  private UserDetails principal;
  private Object details;

  TokenAuthentication(String token, Object details) {
    this.token = token;
    this.details = details;
  }

  TokenAuthentication(String token, Collection<GrantedAuthority> authorities, boolean isAuthenticated,
                             UserDetails principal) {
    this.token = token;
    this.authorities = authorities;
    this.isAuthenticated = isAuthenticated;
    this.principal = principal;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return details;
  }

  @Override
  public String getName() {
    if (principal != null)
      return principal.getUsername();
    else
      return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  @Override
  public void setAuthenticated(boolean b) throws IllegalArgumentException {
    isAuthenticated = b;
  }

  public String getToken() {
    return token;
  }
}
