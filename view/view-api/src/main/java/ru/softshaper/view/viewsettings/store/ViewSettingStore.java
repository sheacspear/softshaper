package ru.softshaper.view.viewsettings.store;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.storage.jooq.tables.daos.FieldViewDao;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.view.viewsettings.impl.ViewSettingImpl;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище, которое возвращает представление поля по его параметрам (табица и колонка)
 */
@Component
public final class ViewSettingStore {

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
    return new ViewSettingImpl(fieldViewBean.getColumnContent(), fieldViewBean.getNumber(), fieldViewBean.getReadonly(),
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
}
