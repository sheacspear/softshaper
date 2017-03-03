package ru.softshaper.view.bean.obj.impl;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.view.bean.obj.IFieldView;

/**
 * View field for bissness object <br/>
 * Created by Sunchise on 24.05.2016.
 */
public class FieldView implements IFieldView {

  private final String title;

  private final String code;

  private final FieldTypeView type;

  private final String linkMetaClass;

  private final String backLinkAttr;

  public FieldView(String title, String code, FieldTypeView type, String linkMetaClass, String backLinkAttr) {
    super();
    this.title = title;
    this.code = code;
    this.type = type;
    this.linkMetaClass = linkMetaClass;
    this.backLinkAttr = backLinkAttr;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFieldView#getTitle()
   */
  @Override
  public String getTitle() {
    return title;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFieldView#getCode()
   */
  @Override
  public String getCode() {
    return code;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFieldView#getType()
   */
  @Override
  public FieldTypeView getType() {
    return type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFieldView#getLinkMetaClass()
   */
  @Override
  public String getLinkMetaClass() {
    return linkMetaClass;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.web.bean.obj.IFieldView#getBackLinkAttr()
   */
  @Override
  public String getBackLinkAttr() {
    return backLinkAttr;
  }
}
