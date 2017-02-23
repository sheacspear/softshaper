package ru.softshaper.web.admin.bean.obj.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.softshaper.services.meta.MetaClass;
import ru.softshaper.services.meta.MetaField;
import ru.softshaper.view.viewsettings.ViewSetting;
import ru.softshaper.web.admin.bean.obj.IFieldView;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.obj.impl.FieldView;
import ru.softshaper.web.admin.bean.obj.impl.FullObjectView;
import ru.softshaper.web.admin.bean.obj.impl.TitleObjectView;
import ru.softshaper.web.admin.bean.objlist.IListObjectsView;

import java.util.*;

/**
 * FullObjectViewBuilder for create FullObjectView
 */
public class FullObjectViewBuilder {

  /**
   * key bissness object
   */
  private final String id;

  /**
   * contentCode bissness object
   */
  private final String contentCode;

  /**
   * title bissness object
   */
  private String title;

  /**
   *
   */
  private List<IFieldView> fields = Lists.newArrayList();

  /**
   *
   */
  private Map<String, ViewSetting> viewSettig = Maps.newHashMap();

  /**
   *
   */
  private Map<String, Object> values = Maps.newHashMap();

  /**
   *
   */
  private Map<String, Collection<IObjectView>> variants = Maps.newHashMap();

  /**
   */
  public FullObjectViewBuilder(String contentCode, String id) {
    this.contentCode = contentCode;
    this.id = id;
  }

  /**
   * @param title bissness object
   * @return this
   */
  public FullObjectViewBuilder setTitle(String title) {
    this.title = title;
    return this;
  }

  public <T> FullObjectViewBuilder addField(FieldView field) {
    this.fields.add(field);
    return this;
  }

  public FullObjectViewBuilder addField(MetaField metaField, ViewSetting fieldView, Object value) {
    MetaField backReferenceField = metaField.getBackReferenceField();
    MetaClass linkToMetaClass = metaField.getLinkToMetaClass();
    fields.add(new FieldView(metaField.getName(), metaField.getCode(), metaField.getType().getDefaultView(),
        linkToMetaClass != null ? linkToMetaClass.getCode() : null,
        backReferenceField != null ? backReferenceField.getCode() : null));
    viewSettig.put(metaField.getCode(), fieldView);
    values.put(metaField.getCode(), value);
    return this;
  }

  public <T> FullObjectViewBuilder addField(MetaField metaField, ViewSetting fieldView, T value,
                                            IListObjectsView variants) {
    addField(metaField, fieldView, value);
    List<IObjectView> variantsLocal = new ArrayList<>();
    if (variants != null) {
      variantsLocal.add(new TitleObjectView("null", null, "---"));
      variantsLocal.addAll(variants.getObjects());
      Collections.sort(variantsLocal,
          (o1, o2) -> (o1 != null && o1.getTitle() != null) ? o1.getTitle().compareTo(o2.getTitle()) : -1);
      this.variants.put(metaField.getCode(), variantsLocal);
    }
    return this;
  }

  /**
   * @return object View for bissness object
   */
  public FullObjectView build() {
    Collections.sort(fields, (field1, field2) -> {
      ViewSetting o1 = viewSettig.get(field1);
      ViewSetting o2 = viewSettig.get(field2);

      if ((o1 == null && o2 != null) || (o2 != null && o1.getNumber() < o2.getNumber())) {
        return -1;
      }
      if ((o2 == null && o1 != null) || (o1 != null && o2.getNumber() < o1.getNumber())) {
        return 1;
      }
      return 0;
    });
    return new FullObjectView(contentCode, id, title, fields, viewSettig, values, variants);
  }
}