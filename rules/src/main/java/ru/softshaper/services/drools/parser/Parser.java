package ru.softshaper.services.drools.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;

@Component
public class Parser {

  private static Map<String, Collection<String>> metaMap = Maps.newHashMap();

  static {
    metaMap.put("test1.docx", Arrays.asList("Test1", "Kanban"));
    metaMap.put("test2.docx", Arrays.asList("Test2", "Bims"));
    metaMap.put("test3.docx", Arrays.asList("Test3"));
    metaMap.put("test4.docx", Arrays.asList("Test4"));
    metaMap.put("test5.docx", Arrays.asList("Test5"));
  }

  public MetaData parse(Data data) {
    return new MetaData(metaMap.get(data.getFileName()));
  }
}
