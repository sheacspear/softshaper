package ru.softshaper.services.drools.bean;

public class MetaData {

  @Override
  public String toString() {
    return "MetaData [metaData=" + metaData + "]";
  }

  private String metaData;

  public MetaData(String metaData) {
    this.metaData = metaData;
  }

  public String getMetaData() {
    return metaData;
  }

  public void setMetaData(String metaData) {
    this.metaData = metaData;
  }

}
