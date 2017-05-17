package ru.softshaper.services.drools.parser;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;

public class Parser {

  public MetaData parse(Data data) {
    return new MetaData(data.toString());
  }

}
