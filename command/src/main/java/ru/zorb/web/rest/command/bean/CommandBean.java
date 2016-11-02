package ru.zorb.web.rest.command.bean;

import java.util.HashMap;

public class CommandBean {

  private String id;

  private String type;

  private String title;

  private String descriprion;

  private HashMap<String,Object> object;

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the descriprion
   */
  public String getDescriprion() {
    return descriprion;
  }

  /**
   * @param descriprion the descriprion to set
   */
  public void setDescriprion(String descriprion) {
    this.descriprion = descriprion;
  }

  /**
   * @return the object
   */
  public HashMap<String,Object>  getObject() {
    return object;
  }

  /**
   * @param object the object to set
   */
  public void setObject(HashMap<String,Object>  object) {
    this.object = object;
  }

}
