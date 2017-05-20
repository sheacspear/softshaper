package ru.softshaper.services.drools.provider;

import ru.softshaper.services.drools.bean.Data;

public class DataProvider {

  public Data getData() {
    return new Data("TestData");
  }

}
