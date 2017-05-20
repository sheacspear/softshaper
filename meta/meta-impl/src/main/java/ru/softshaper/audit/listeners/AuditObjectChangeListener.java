package ru.softshaper.audit.listeners;

import java.sql.Timestamp;
import java.util.Calendar;

import com.google.common.eventbus.Subscribe;

import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.events.ObjectDeleted;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.storage.jooq.tables.daos.AuditDao;
import ru.softshaper.storage.jooq.tables.pojos.Audit;

/**
 * Created by Sunchise on 18.05.2017.
 */
public class AuditObjectChangeListener {

  private AuditDao auditDao;

  public AuditObjectChangeListener(AuditDao auditDao) {
    this.auditDao = auditDao;
  }

  private String constructDescription(ObjectCreated info) {
    return String.format("Object of class %1$s was created with id %2$s. Values %3$s", info.getMetaClass().getCode(), info.getId(),
        String.valueOf(info.getValues()));
  }

  private String constructDescription(ObjectUpdated info) {
    return String.format("Object of class %1$s with id %2$s was updated. Values %3$s", info.getMetaClass().getCode(), info.getId(),
        String.valueOf(info.getValues()));
  }

  private String constructDescription(ObjectDeleted info) {
    return String.format("Object of class %1$s with id %2$s was deleted.", info.getMetaClass().getCode(), info.getId());
  }

  @Subscribe
  public void onObjectCreate(ObjectCreated objectCreated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("create object");
    audit.setDescription(constructDescription(objectCreated));
    audit.setUser(objectCreated.getUserLogin());
    auditDao.insert(audit);
  }

  @Subscribe
  public void onObjectUpdate(ObjectUpdated objectUpdated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("update objectt");
    audit.setDescription(constructDescription(objectUpdated));
    audit.setUser(objectUpdated.getUserLogin());
    auditDao.insert(audit);
  }

  @Subscribe
  public void onObjectDelete(ObjectDeleted objectDeleted) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Audit audit = new Audit();
    audit.setDate(date);
    audit.setType("delete objectt");
    audit.setDescription(constructDescription(objectDeleted));
    audit.setUser(objectDeleted.getUserLogin());
    auditDao.insert(audit);
  }
}
