package ru.softshaper.services.drools.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.apache.poi.POIXMLPropertiesTextExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import ru.softshaper.services.drools.bean.Data;
import ru.softshaper.services.drools.bean.MetaData;

@Component
public class Parser {

  public MetaData parse(Data data) throws InvalidFormatException, IOException {
    if (data == null) {
      return null;
    }
    byte[] data2 = data.getData();
    if (data2 == null) {
      return null;
    }
    XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(new ByteArrayInputStream(data2)));
    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
    POIXMLPropertiesTextExtractor pOIXMLPropertiesTextExtractor = extractor.getMetadataTextExtractor();
    pOIXMLPropertiesTextExtractor.getCorePropertiesText();
    String keyWords = pOIXMLPropertiesTextExtractor.getCoreProperties().getKeywords();
    if (keyWords == null) {
      return null;
    }
    // xdoc.get

    return new MetaData(Arrays.asList(keyWords.split(",")));
  }

  public static void main(String... arg) throws IOException, InvalidFormatException {
    URL website = new URL("https://www.dropbox.com/s/an5bhkz87aio061/test1.docx?dl=1");
    byte[] data2 = IOUtils.toByteArray(website.openStream());
    new Parser().parse(new Data("", data2));
  }
}
