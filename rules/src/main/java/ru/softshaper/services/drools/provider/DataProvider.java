package ru.softshaper.services.drools.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import ru.softshaper.services.drools.bean.Data;

@Component
public class DataProvider {

  private static AtomicInteger cnt = new AtomicInteger(0);

  public Data getData() {
    InputStream ip = null;
    try {
      int rand = cnt.incrementAndGet();
      int num = (rand % 4) + 1;
      String file = "test" + num + ".docx";
      ip = this.getClass().getClassLoader().getResourceAsStream("doc/" + file);
      return new Data(file, IOUtils.toByteArray(ip));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (ip != null) {
        try {
          ip.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public static void main(String... arg) {
    new DataProvider().getData();
  }
}
