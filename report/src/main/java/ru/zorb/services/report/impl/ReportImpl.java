package ru.zorb.services.report.impl;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import ru.zorb.services.report.IReport;

/**
 * @author ashek
 *
 */
public class ReportImpl implements IReport {
  /**
   *
   */
  public static final Logger log = LoggerFactory.getLogger(ReportImpl.class);

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.services.report.IReport#build(java.util.Map)
   */
  @Override
  public byte[] build(Map<String, Object> params) {
    try {
      Collection<Map<String, ?>> jasperTable = Lists.newArrayList();
      Map<String, Object> userMap = Maps.newHashMap();
      userMap.put("userName", "admin");
      userMap.put("role", "ROLE_ADMIN");
      jasperTable.add(userMap);
      userMap = Maps.newHashMap();
      userMap.put("userName", "testUser");
      userMap.put("role", "ROLE_ADMIN");
      jasperTable.add(userMap);
      userMap = Maps.newHashMap();
      userMap.put("userName", "testUser");
      userMap.put("role", "ROLE_USER");
      jasperTable.add(userMap);
      JRDataSource jasperDataSource = new JRMapCollectionDataSource(jasperTable);
      JasperReport jasperReport = JasperCompileManager
          .compileReport(getClass().getClassLoader().getResourceAsStream("report/Coffee.jrxml"));
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jasperDataSource);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      JasperExportManager.exportReportToPdfStream(jasperPrint, bos);
      return bos.toByteArray();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
