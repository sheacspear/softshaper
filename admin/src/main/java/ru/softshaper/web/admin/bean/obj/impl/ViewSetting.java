package ru.softshaper.web.admin.bean.obj.impl;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.web.admin.bean.obj.IViewSetting;

public class ViewSetting implements IViewSetting {

  private final String columnContent;
  private final Integer number;
  private final Boolean readonly;
  private final Boolean required;
  private final String title;
  private final Boolean titleField;
  private final Boolean tableField;
  private final FieldTypeView typeView;

  public ViewSetting(String columnContent, Integer number, Boolean readonly, Boolean required, String title, Boolean titleField, Boolean tableField, FieldTypeView typeView) {
    this.columnContent = columnContent;
    this.number = number;
    this.readonly = readonly;
    this.required = required;
    this.title = title;
    this.titleField = titleField;
    this.tableField = tableField;
    this.typeView = typeView;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getColumnContent()
   */
  @Override
  public String getColumnContent() {
    return columnContent;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getNumber()
   */
  @Override
  public Integer getNumber() {
    return number;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getReadonly()
   */
  @Override
  public Boolean getReadonly() {
    return readonly;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getRequired()
   */
  @Override
  public Boolean getRequired() {
    return required;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getTitle()
   */
  @Override
  public String getTitle() {
    return title;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#isTitleField()
   */
  @Override
  public Boolean isTitleField() {
    return titleField;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#isTableField()
   */
  @Override
  public Boolean isTableField() {
    return tableField;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.IViewSetting#getTypeView()
   */
  @Override
  public FieldTypeView getTypeView() {
    return typeView;
  }
}
