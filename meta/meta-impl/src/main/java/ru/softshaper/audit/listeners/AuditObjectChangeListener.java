package ru.softshaper.audit.listeners;

import com.google.common.eventbus.Subscribe;
import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.events.ObjectDeleted;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.storage.jooq.tables.daos.AuditDao;
import ru.softshaper.storage.jooq.tables.pojos.Audit;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Sunchise on 18.05.2017.
 */
public class AuditObjectChangeListener {

  private AuditDao auditDao;

  public AuditObjectChangeListener(AuditDao auditDao) {
    this.auditDao = auditDao;
  }

  private String constructDescription(ObjectCreated info) {
    return String.format("Объект класса %1$s был создан с идентификатором %2$s\n" +
            "Состав полей:\n" +
            "%3$s",
        info.getMetaClass().getName(),
        info.getId(),
        constructFieldsDescription(info.getValues()));
  }

  private String constructDescription(ObjectUpdated info) {
    return String.format("Объект класса %1$s с идентификатором %2$s был изменён\n" +
            "Состав полей:\n" +
            "%3$s",
        info.getMetaClass().getName(),
        info.getId(),
        constructFieldsDescription(info.getValues()));
  }

  private String constructFieldsDescription(Map<MetaField, Object> values) {
    return values.entrySet()
        .stream()
        .map(entry -> entry.getKey().getName() + ": " + entry.getValue())
        .reduce((s, s2) -> s + "\n" + s2)
        .orElse("");
  }

  private String constructDescription(ObjectDeleted info) {
    return String.format("Объект класса %1$s с идентификатором %2$s был удалён.",
        info.getMetaClass().getName(),
        info.getId());
  }

  @Subscribe
  public void onObjectCreate(ObjectCreated objectCreated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("create object");
    audit.setDescription(constructDescription(objectCreated));
    audit.setUser(objectCreated.getUserLogin());
    audit.setObjectLink(objectCreated.getId() + "@" + objectCreated.getMetaClass().getCode());
    auditDao.insert(audit);
  }

  @Subscribe
  public void onObjectUpdate(ObjectUpdated objectUpdated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("update object");
    audit.setDescription(constructDescription(objectUpdated));
    audit.setUser(objectUpdated.getUserLogin());
    audit.setObjectLink(objectUpdated.getId() + "@" + objectUpdated.getMetaClass().getCode());
    auditDao.insert(audit);
  }

  @Subscribe
  public void onObjectDelete(ObjectDeleted objectDeleted) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("delete object");
    audit.setDescription(constructDescription(objectDeleted));
    audit.setUser(objectDeleted.getUserLogin());
    auditDao.insert(audit);
  }
}
