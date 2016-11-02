package ru.zorb.services.meta.bean;

import org.jooq.DSLContext;
import org.jooq.Record3;
import ru.zorb.services.meta.MetaClassMutable;
import ru.zorb.services.meta.staticcontent.StaticContentBase;
import ru.zorb.storage.jooq.tables.DynamicContent;
import ru.zorb.storage.jooq.tables.records.DynamicContentRecord;

/**
 * @author asheknew
 *
 */
public class MetaBuilder {

  private final String contentCode;
  /**
   *
   */
  private final String name;
  /**
   *
   */
  private final String table;

  protected DSLContext dslContext;

  /**
   * @param contentCode
   * @param name
   * @param table
   * @param staticContentBase TODO
   */
  public MetaBuilder(StaticContentBase staticContentBase, String contentCode, String name, String table, DSLContext dslContext) {
    super();
    this.contentCode = contentCode;
    this.name = name;
    this.table = table;
    this.dslContext = dslContext;
  }

  /**
   * @return
   */
  public MetaClassMutable build() {
    DynamicContent dcTable = DynamicContent.DYNAMIC_CONTENT;
    Record3<Long, Boolean, Boolean> res = dslContext
        .select(dcTable.ID, dcTable.CHECKOBJECTSECURITY, dcTable.CHECKSECURITY)
          .from(dcTable)
          .where(dcTable.CODE.equal(contentCode))
        .fetchOne();
    String id = null;
    Boolean objSec = null;
    Boolean classSec = null;
    if (res != null) {
      id = res.value1().toString();
      objSec = res.value2();
      classSec = res.value3();
    }
    if (id == null) {
      DynamicContentRecord dynamicContentRecord = dslContext.insertInto(dcTable).columns(dcTable.CODE, dcTable.FIXED).values(contentCode, true).returning(dcTable.ID).fetchOne();
      id = dynamicContentRecord.getValue(dcTable.ID).toString();
    }
    return new MetaClassMutableBean(id, contentCode, name, table, null, classSec != null ? classSec : false, objSec != null ? objSec : false);
  }
}