package ru.softshaper.audit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.services.utils.IResultUtil;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.services.utils.bean.impl.ResultUtil;
import ru.softshaper.storage.jooq.tables.daos.AuditDao;
import ru.softshaper.storage.jooq.tables.pojos.Audit;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShowAuditHistory implements IUtil {

  @Autowired
  private IUtilsEngine utilsEngine;
  @Autowired
  private AuditDao auditDao;

  @PostConstruct
  public void init() {
    utilsEngine.registerObjectUtil(this, "suitRules");
  }

  @Override
  public String getName() {
    return "Показать историю изменения объекта";
  }

  @Override
  public String getCode() {
    return "ShowAuditHistory";
  }

  @Override
  public StepUtil getNextStep(Map<String, Object> data) {
    return null;
  }

  @Override
  public IResultUtil execute(String metaClazz, String objId, Map<String, Object> data) {
    List<Audit> audits = auditDao.fetchByObjectLink(objId + "@" + metaClazz);
    if (audits.isEmpty()) {
      return new ResultUtil("Result", true, Collections.singletonList("Истории изменения объектов не найдено"));
    }
    List<String> messages = audits.stream()
        .sorted(Comparator.comparing(Audit::getDate))
        .map(audit -> audit.getDate() + "\n" + "Пользователь: " + audit.getUser() + "\n" + audit.getDescription())
        .collect(Collectors.toList());
    return new ResultUtil("Result", true, messages);
  }
}
