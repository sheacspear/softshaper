package ru.zorb.services.report.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zorb.exceptions.NotImplementedException;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.report.IReport;
import ru.zorb.web.bean.objlist.ColumnView;
import ru.zorb.web.bean.objlist.ObjectRowView;
import ru.zorb.web.bean.objlist.TableObjectsView;
import ru.zorb.web.view.DataViewMapper;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Генерирует отчёт со всеми полями заданного контента в csv
 */
public class UniversalDynamicContentListReport implements IReport {

  public static final Logger log = LoggerFactory.getLogger(ReportImpl.class);

  public UniversalDynamicContentListReport() {
  }

  @Override
  public byte[] build(Map<String, Object> params) {
    try {
      TableObjectsView objectList = (TableObjectsView) params.get("objectList");
      params.remove("objectList");

      JRDataSource jasperDataSource = constructJasperDataSource(objectList);
      List<ColumnView> columnsView = objectList.getColumnsView();
      JasperDesign jasperDesign = constructJasperTemplate(columnsView);
      JasperPrint jasperPrint = fillReport(params, jasperDataSource, jasperDesign);

      ByteArrayOutputStream bos = printToCsv(jasperPrint);
      return bos.toByteArray();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Something goes wrong", e);
    }
  }

  private ByteArrayOutputStream printToCsv(JasperPrint jasperPrint) throws JRException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JRCsvExporter exporter = new JRCsvExporter();
    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    exporter.setExporterOutput(new SimpleHtmlExporterOutput(bos));
    exporter.exportReport();
    return bos;
  }

  private JasperPrint fillReport(Map<String, Object> params, JRDataSource jasperDataSource, JasperDesign jasperDesign) throws JRException {
    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    return JasperFillManager.fillReport(jasperReport, params, jasperDataSource);
  }

  private JasperDesign constructJasperTemplate(List<ColumnView> columnsView) throws JRException {
    JasperDesign jasperDesign = new JasperDesign();
    jasperDesign.setName("UniversalDynamicContentListReport");

    JRDesignBand detailBand = new JRDesignBand();
    detailBand.setHeight(50);
    JRDesignBand headerBand = new JRDesignBand();
    headerBand.setHeight(100);

    int x = 0;
    for (ColumnView columnView : columnsView) {
      // Create a Column Field
      JRDesignField field = new JRDesignField();
      field.setName(columnView.getKey());
      field.setValueClass(String.class);
      field.setDescription("test");
      jasperDesign.addField(field);

      // Add a Header Field to the headerBand
      // headerBand.setHeight(BAND_HEIGHT);

      JRDesignStaticText colHeaderField = new JRDesignStaticText();
      colHeaderField.setX(x * 120);
      colHeaderField.setY(2);
      colHeaderField.setWidth(120);
      colHeaderField.setHeight(20);
      colHeaderField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
      colHeaderField.setText(columnView.getName());
      colHeaderField.setBold(true);
      headerBand.addElement(colHeaderField);

      JRDesignTextField textField = new JRDesignTextField();
      textField.setX(x * 120);
      textField.setY(2);
      textField.setWidth(120);
      textField.setHeight(20);
      textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
      JRDesignExpression expression = new JRDesignExpression();
      expression.setValueClass(String.class);
      expression.setText("$F{" + columnView.getKey() + "}");
      textField.setExpression(expression);
      detailBand.addElement(textField);
      x++;
    }
    jasperDesign.setColumnHeader(headerBand);
    ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

    jasperDesign.preprocess();
    return jasperDesign;
  }

  private JRDataSource constructJasperDataSource(TableObjectsView objectList) {
    Collection<Map<String, ?>> jasperTable = Lists.newArrayList();
    List<ColumnView> columnsView = objectList.getColumnsView();
    List<ObjectRowView> objectsView = objectList.getObjectsView();
    objectsView.forEach(objectView -> {
      final Map<String, Object> dataMap = Maps.newHashMap();
      for (int i = 0; i < columnsView.size(); i++) {
        dataMap.put(columnsView.get(i).getKey(), objectView.getData().get(i));
      }
      jasperTable.add(dataMap);
    });
    return new JRMapCollectionDataSource(jasperTable);
  }
}
