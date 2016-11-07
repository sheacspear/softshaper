package ru.zorb.conf.bean;

/**
 * Created by Sunchise on 27.09.2016.
 */
public class MongoDBConfig {

  private final String url;
  private final String base;
  private final String login;
  private final String password;

  public MongoDBConfig(String url, String base, String login, String password) {
    this.url = url;
    this.base = base;
    this.login = login;
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public String getBase() {
    return base;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
