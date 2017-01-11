package ru.softshaper.web.view.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaField;
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

  /**
   *
   */
  private Map<String, ViewSetting> map = new HashMap<>();

  /**
   * @return
   */
  public ViewSetting getView(MetaField field) {
    ViewSetting viewSetting = map.get(field.getId());
    if (viewSetting == null) {
      synchronized (this) {
        viewSetting = map.get(field.getId());
        if (viewSetting == null) {
          viewSetting = createDefault(field, 0, field.getType().getDefaultView());
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
    Map<String, ViewSetting> map2 = Maps.newHashMap();
    Iterable<ru.softshaper.storage.jooq.tables.pojos.FieldView> fieldsViews = fieldViewDao.findAll();
    fieldsViews.forEach(fieldViewBean -> map2.put(fieldViewBean.getFieldId().toString(), convert(fieldViewBean)));
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

  private ViewSetting createDefault(MetaField field, Integer number, FieldTypeView fieldTypeView) {
    ViewSetting fieldView2 = map.get(field.getId());
    if (fieldView2 == null) {
      ru.softshaper.storage.jooq.tables.pojos.FieldView fieldView = new ru.softshaper.storage.jooq.tables.pojos.FieldView();
      fieldView.setTypeviewcode(fieldTypeView.getCode());
      fieldView.setTitlefield(false);
      fieldView.setReadonly(false);
      fieldView.setRequired(false);
      fieldView.setNumber(number);
      fieldView.setTitle(field.getName());
      fieldView.setFieldId(Long.valueOf(field.getId()));
      fieldViewDao.insert(fieldView);
      map = null;
      init();
      return getView(field);
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
