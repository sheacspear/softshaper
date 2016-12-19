package ru.softshaper.rest.login.bean;

/**
 * Login bean
 * 
 * @author ashek
 *
 */
public class Login {
  /**
   * login
   */
  private String login;
  /**
   * password
   */
  private String password;

  /**
   * token
   */
  private String token;

  /**
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * @param login the login to set
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * @param token the token to set
   */
  public void setToken(String token) {
    this.token = token;
  }
}
