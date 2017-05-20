package ru.softshaper.services.drools.saver;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;

public class Saver {

  public void save(Data data, MetaData metaData) {

    System.out.println("save data: " + data);
    System.out.println("save metaData: " + metaData);

  }

}
