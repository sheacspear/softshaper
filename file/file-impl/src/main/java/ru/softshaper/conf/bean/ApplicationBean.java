package ru.softshaper.conf.bean;

/**
 * Bean from file application.properties
 *
 * @author ashek
 *
 */
public class ApplicationBean {
  /**
   * url database
   */
  private String bdUrl;
  /**
   * login database
   */
  private String bdLogin;
  /**
   * pass database
   */
  private String bdPass;
  /**
   * driver database
   */
  private String bdDriverClassName;
  /**
   * type database
   */
  private String bdDatabaseType;

  private MongoDBConfig mongodb;

  /**
   * @return
   */
  public String getBdPass() {
    return bdPass;
  }

  /**
   * @param bdPass
   */
  public void setBdPass(String bdPass) {
    this.bdPass = bdPass;
  }

  /**
   * @return
   */
  public String getBdLogin() {
    return bdLogin;
  }

  /**
   * @param bdLogin
   */
  public void setBdLogin(String bdLogin) {
    this.bdLogin = bdLogin;
  }

  /**
   * @return
   */
  public String getBdUrl() {
    return bdUrl;
  }

  /**
   * @param bdUrl
   */
  public void setBdUrl(String bdUrl) {
    this.bdUrl = bdUrl;
  }

  /**
   * @return the bdDriverClassName
   */
  public String getBdDriverClassName() {
    return bdDriverClassName;
  }

  /**
   * @param bdDriverClassName the bdDriverClassName to set
   */
  public void setBdDriverClassName(String bdDriverClassName) {
    this.bdDriverClassName = bdDriverClassName;
  }

  /**
   * @return the bdDatabaseType
   */
  public String getBdDatabaseType() {
    return bdDatabaseType;
  }

  /**
   * @param bdDatabaseType the bdDatabaseType to set
   */
  public void setBdDatabaseType(String bdDatabaseType) {
    this.bdDatabaseType = bdDatabaseType;
  }

  public MongoDBConfig getMongodb() {
    return mongodb;
  }

  public void setMongodb(MongoDBConfig mongodb) {
    this.mongodb = mongodb;
  }
}
