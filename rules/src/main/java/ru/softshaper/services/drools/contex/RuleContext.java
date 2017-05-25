package ru.softshaper.services.drools.contex;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;
import ru.softshaper.services.utils.IUtil;

public class RuleContext {

  private String mode = "start";

  private Data data;

  private MetaData metaData;

  private final Multimap<String, String> msgs = LinkedListMultimap.create();

  public Multimap<String, String> getMsgs() {
    return msgs;
  }

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

  public void addMessage(String rule, String message) {
    msgs.put(rule, message);
  }

  @Override
  public String toString() {
    return "RuleContext [mode=" + mode + ", data=" + data + ", metaData=" + metaData + "]";
  }

}
