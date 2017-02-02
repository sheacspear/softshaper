package ru.softshaper.web.admin.bean.obj.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.softshaper.web.admin.bean.obj.IFieldView;
import ru.softshaper.web.admin.bean.obj.IFullObjectView;
import ru.softshaper.web.admin.bean.obj.IObjectView;
import ru.softshaper.web.admin.bean.obj.builder.FullObjectViewBuilder;
import ru.softshaper.web.admin.view.IViewSetting;

/**
 * View for bissness object</br>
 * Created by Sunchise on 23.05.2016.
 */
public class FullObjectView extends TitleObjectView implements IFullObjectView {

  /**
   * 
   */
  private final List<IFieldView> fields;

  /**
   * 
   */
  private final Map<String, IViewSetting> viewSettig;

  /**
   * 
   */
  private final Map<String, ?> values;

  /**
   * 
   */
  private final Map<String, Collection<IObjectView>> variants;

  /**
   * @param contentCode
   * @param id
   * @param title
   * @param fields
   * @param viewSettig
   * @param values
   * @param variants
   */
  public FullObjectView(String contentCode, String id, String title, List<IFieldView> fields,
      Map<String, IViewSetting> viewSettig, Map<String, ?> values, Map<String, Collection<IObjectView>> variants) {
    super(contentCode, id, title);
    this.fields = fields;
    this.viewSettig = viewSettig;
    this.values = values;
    this.variants = variants;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFullObjectView#getFields()
   */
  @Override
  public List<IFieldView> getFields() {
    return fields;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFullObjectView#getViewSettig()
   */
  @Override
  public Map<String, IViewSetting> getViewSettig() {
    return viewSettig;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFullObjectView#getValues()
   */
  @Override
  public Map<String, ?> getValues() {
    return values;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFullObjectView#getVariants()
   */
  @Override
  public Map<String, Collection<IObjectView>> getVariants() {
    return variants;
  }

  /**
   * Builder for create FullObjectView
   *
   * @param key key object
   * @return FullObjectViewBuilder
   */
  public static FullObjectViewBuilder newBuilder(String contentCode, String key) {
    return new FullObjectViewBuilder(contentCode, key);
  }
}
