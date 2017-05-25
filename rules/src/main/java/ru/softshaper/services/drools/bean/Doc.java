package ru.softshaper.services.drools.bean;

public class Doc {

  private String name;
  private String id;

  public Doc(String name, String id) {
    super();
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Документ " + name + " с идентификатором " + id;
  }
}
