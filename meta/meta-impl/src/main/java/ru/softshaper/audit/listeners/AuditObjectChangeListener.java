package ru.softshaper.audit.listeners;

import com.google.common.eventbus.Subscribe;
import org.jooq.DSLContext;
import ru.softshaper.datasource.events.ObjectCreated;
import ru.softshaper.datasource.events.ObjectDeleted;
import ru.softshaper.datasource.events.ObjectUpdated;
import ru.softshaper.storage.jooq.tables.Audit;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Sunchise on 18.05.2017.
 */
public class AuditObjectChangeListener {

  private final DSLContext dsl;

  public AuditObjectChangeListener(DSLContext dsl) {
    this.dsl = dsl;
  }

  private String constructDescription(ObjectCreated info) {
    return String.format("Object of class %1$s was created with id %2$s. Values %3$s",
        info.getMetaClass().getCode(),
        info.getId(),
        String.valueOf(info.getValues()));
  }
  private String constructDescription(ObjectUpdated info) {
    return String.format("Object of class %1$s with id %2$s was updated. Values %3$s",
        info.getMetaClass().getCode(),
        info.getId(),
        String.valueOf(info.getValues()));
  }
  private String constructDescription(ObjectDeleted info) {
    return String.format("Object of class %1$s with id %2$s was deleted.",
        info.getMetaClass().getCode(),
        info.getId());
  }

  @Subscribe
  public void onObjectCreate(ObjectCreated objectCreated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    dsl.insertInto(Audit.AUDIT)
        .columns(Audit.AUDIT.DATE, Audit.AUDIT.TYPE, Audit.AUDIT.DESCRIPTION, Audit.AUDIT.USER)
        .values(date, "create object", constructDescription(objectCreated), objectCreated.getUserLogin())
        .execute();
  }

  @Subscribe
  public void onObjectUpdate(ObjectUpdated objectUpdated) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    dsl.insertInto(Audit.AUDIT)
        .columns(Audit.AUDIT.DATE, Audit.AUDIT.TYPE, Audit.AUDIT.DESCRIPTION, Audit.AUDIT.USER)
        .values(date, "update object", constructDescription(objectUpdated), objectUpdated.getUserLogin())
        .execute();
  }

  @Subscribe
  public void onObjectDelete(ObjectDeleted objectDeleted) {
    Timestamp date = new Timestamp(Calendar.getInstance().getTimeInMillis());
    dsl.insertInto(Audit.AUDIT)
        .columns(Audit.AUDIT.DATE, Audit.AUDIT.TYPE, Audit.AUDIT.DESCRIPTION, Audit.AUDIT.USER)
        .values(date, "delete object", constructDescription(objectDeleted), objectDeleted.getUserLogin())
        .execute();
  }
}
