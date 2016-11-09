package ru.softshaper.web.view.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.services.meta.MetaStorage;
import ru.softshaper.storage.jooq.tables.daos.FieldViewDao;
import ru.softshaper.web.view.bean.ViewSetting;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище, которое возвращает представление поля по его параметрам (табица и колонка)
 */
@Component
public class ViewSettingFactory {

  /**
   * FieldViewDao
   */
  @Autowired
  private FieldViewDao fieldViewDao;

  @Autowired
  private MetaStorage metaStorage;

  /**
   *
   */
  private Map<ContentCodeFieldPare, ViewSetting> map = new HashMap<>();

  /**
   * @param contentCode
   * @param fieldCode
   * @return
   */
  public ViewSetting getView(String contentCode, String fieldCode) {
    ViewSetting viewSetting = map.get(new ContentCodeFieldPare(contentCode, fieldCode));
    if (viewSetting == null) {
      synchronized (this) {
        viewSetting = map.get(new ContentCodeFieldPare(contentCode, fieldCode));
        if (viewSetting == null) {
          MetaClass metaClass = metaStorage.getMetaClass(contentCode);
          Preconditions.checkNotNull(metaClass);
          MetaField field = metaClass.getField(fieldCode);
          Preconditions.checkNotNull(field);
          viewSetting = createDefault(contentCode, fieldCode, field.getName(), 0, field.getType().getDefaultView());
        }
      }
    }
    return viewSetting;
  }

  /**
   *
   */
  @PostConstruct
  public synchronized void init() {
    Map<ContentCodeFieldPare, ViewSetting> map2 = Maps.newHashMap();
    Iterable<ru.softshaper.storage.jooq.tables.pojos.FieldView> fieldsViews = fieldViewDao.findAll();
    fieldsViews.forEach(fieldViewBean -> map2.put(new ContentCodeFieldPare(fieldViewBean.getTableContent(), fieldViewBean.getColumnContent()), convert(fieldViewBean)));
    map = map2;
  }

  /**
   * @param fieldViewBean
   * @return
   */
  private ViewSetting convert(ru.softshaper.storage.jooq.tables.pojos.FieldView fieldViewBean) {
    return new ViewSetting(fieldViewBean.getColumnContent(), fieldViewBean.getNumber(), fieldViewBean.getReadonly(),
        fieldViewBean.getRequired(), fieldViewBean.getTitle(), fieldViewBean.getTitlefield(),
        fieldViewBean.getTablefield(),
        FieldTypeView.byCode(fieldViewBean.getTypeviewcode()));
  }

  private ViewSetting createDefault(String metaClassCode, String fieldCode, String title, Integer number, FieldTypeView stringSingle) {
    ViewSetting fieldView2 = map.get(new ContentCodeFieldPare(metaClassCode, fieldCode));
    if (fieldView2 == null) {
      ru.softshaper.storage.jooq.tables.pojos.FieldView fieldView = new ru.softshaper.storage.jooq.tables.pojos.FieldView();
      fieldView.setColumnContent(fieldCode);
      fieldView.setTableContent(metaClassCode);
      fieldView.setTypeviewcode(stringSingle.getCode());
      fieldView.setTitlefield(false);
      fieldView.setReadonly(false);
      fieldView.setRequired(false);
      fieldView.setNumber(number);
      fieldView.setTitle(title);
      fieldViewDao.insert(fieldView);
      map = null;
      init();
      return getView(metaClassCode, fieldCode);
    } else {
      return fieldView2;
    }
  }

  /**
   * @author ashek
   *
   */
  private class ContentCodeFieldPare {
    /**
     *
     */
    private final String contentCode;
    /**
     *
     */
    private final String filedCode;

    /**
     * @param contentCode
     * @param filedCode
     */
    private ContentCodeFieldPare(String contentCode, String filedCode) {
      Preconditions.checkNotNull(contentCode, "Код мета-класса должен быть указан");
      Preconditions.checkNotNull(filedCode, "Код мета-поля должен быть указан");
      this.contentCode = contentCode;
      this.filedCode = filedCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      ContentCodeFieldPare that = (ContentCodeFieldPare) o;

      return contentCode.equals(that.contentCode) && filedCode.equals(that.filedCode);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      int result = contentCode.hashCode();
      result = 31 * result + filedCode.hashCode();
      return result;
    }
  }

}
