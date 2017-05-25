package ru.softshaper.services.drools.provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import ru.softshaper.services.drools.bean.Data;

@Component
public class DataProvider {

  public Data getData(String file, String url) {

    URL website = null;
    try {
      website = new URL(url);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } // "https://www.dropbox.com/s/an5bhkz87aio061/test1.docx?dl=1"
    try (InputStream openStream = website.openStream()) {
      byte[] data2 = IOUtils.toByteArray(openStream);
      return new Data(file, data2);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  // public static void main(String... arg) {
  // new DataProvider().getData();
  // }
}
