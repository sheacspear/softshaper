package ru.zorb.services.meta.staticcontent;

import com.google.common.base.Preconditions;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.meta.FieldType;
import ru.zorb.services.meta.MetaClassMutable;
import ru.zorb.services.meta.MetaFieldMutable;
import ru.zorb.services.meta.StaticContent;
import ru.zorb.services.meta.bean.FieldBuilder;
import ru.zorb.services.meta.bean.MetaBuilder;
import ru.zorb.services.meta.impl.loader.StaticContentLoader;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public abstract class StaticContentBase implements StaticContent {

  /**
   * DSLContext
   */
  @Autowired
  protected DSLContext dslContext;

  /**
   *
   */
  @Autowired
  private StaticContentLoader staticContentLoader;

  /**
   *
   */
  private final String code;
  /**
   *
   */
  private final String name;
  /**
   *
   */
  private final String table;

  private final Collection<FieldBuilder<?, ?>> fieldsBuilders = new ArrayList<FieldBuilder<?, ?>>();

  /**
   *
   */
  private final ContentDataSource<?> dataSource;

  /**
   * @param code
   * @param name
   * @param table
   * @param dataSource
   */
  public StaticContentBase(String code, String name, String table, ContentDataSource<?> dataSource) {
    super();
    this.code = code;
    this.name = name;
    this.table = table;
    this.dataSource = dataSource;
  }

  /**
   *
   */
  @PostConstruct
  private void init() {
    staticContentLoader.register(this);
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.services.meta.StaticContent#getContentDataSource()
   */
  @Override
  public ContentDataSource<?> getContentDataSource() {
    return dataSource;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the table
   */
  public String getTable() {
    return table;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.services.meta.StaticContent#getMetaClass()
   */
  @Override
  public MetaClassMutable getMetaClass() {
    return new MetaBuilder(this, getCode(), getName(), getTable(), dslContext).build();
  }

  /**
   * @return
   */
  private Collection<FieldBuilder<?, ?>> getFields() {
    Collection<FieldBuilder<?, ?>> clone = new ArrayList<FieldBuilder<?, ?>>(fieldsBuilders.size());
    for (FieldBuilder<?, ?> fieldsBuilder : fieldsBuilders) {
      clone.add((FieldBuilder<?, ?>) fieldsBuilder.clone());
    }
    return clone;
  }

  /*
   * (non-Javadoc)
   *
   * @see ru.zorb.services.meta.StaticContent#loadFields(java.util.Map)
   */
  @Override
  public Collection<MetaFieldMutable> loadFields(Map<String, MetaClassMutable> clazzCode) {
    Collection<MetaFieldMutable> result = new ArrayList<>();
    MetaClassMutable metaClass = clazzCode.get(getCode());
    Preconditions.checkNotNull(metaClass);
    Collection<FieldBuilder<?, ?>> filedsBuilder = this.getFields();
    if (filedsBuilder != null) {
      for (FieldBuilder<?, ?> fieldBuilder : filedsBuilder) {
        fieldBuilder.setClazzCode(clazzCode);
        fieldBuilder.setMetaClass(metaClass);
        fieldBuilder.setDslContext(dslContext);
        result.add(fieldBuilder.build());
      }
    }
    return result;
  }

  /**
   * @param column
   * @return
   */
  public <T extends Record, K> FieldBuilder<T, K> registerField(TableField<T, K> column,FieldType fieldType) {
    FieldBuilder<T, K> builder = new FieldBuilder<T, K>(column,fieldType);
    fieldsBuilders.add(builder);
    return builder;
  }

}
