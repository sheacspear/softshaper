package ru.softshaper.services.drools.contex;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;

public class RuleContext {

  private String mode = "start";

  private Data data;

  private MetaData metaData;
  
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public MetaData getMetaData() {
    return metaData;
  }

  public void setMetaData(MetaData metaData) {
    this.metaData = metaData;
  }

  @Override
  public String toString() {
    return "RuleContext [mode=" + mode + ", data=" + data + ", metaData=" + metaData + "]";
  }

}
