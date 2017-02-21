package ru.softshaper.view.viewsettings.impl;

import ru.softshaper.bean.meta.FieldTypeView;
import ru.softshaper.view.viewsettings.ViewSetting;

public final class ViewSettingImpl implements ViewSetting {

  private final String columnContent;
  private final Integer number;
  private final Boolean readonly;
  private final Boolean required;
  private final String title;
  private final Boolean titleField;
  private final Boolean tableField;
  private final FieldTypeView typeView;

  public ViewSettingImpl(String columnContent, Integer number, Boolean readonly, Boolean required, String title, Boolean titleField, Boolean tableField, FieldTypeView typeView) {
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
   * @see ru.softshaper.web.view.bean.ViewSetting#getColumnContent()
   */
  @Override
  public String getColumnContent() {
    return columnContent;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#getNumber()
   */
  @Override
  public Integer getNumber() {
    return number;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#getReadonly()
   */
  @Override
  public Boolean getReadonly() {
    return readonly;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#getRequired()
   */
  @Override
  public Boolean getRequired() {
    return required;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#getTitle()
   */
  @Override
  public String getTitle() {
    return title;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#isTitleField()
   */
  @Override
  public Boolean isTitleField() {
    return titleField;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#isTableField()
   */
  @Override
  public Boolean isTableField() {
    return tableField;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.web.view.bean.ViewSetting#getTypeView()
   */
  @Override
  public FieldTypeView getTypeView() {
    return typeView;
  }
}
