package ru.softshaper.web.bean.obj;

import ru.softshaper.web.bean.FieldTypeView;
import ru.softshaper.web.view.bean.ViewSetting;

public class FieldObjectViewBuilder<T> {
  private final String title;
  private boolean required;
  private boolean readOnly;
  private int number;
  private boolean titleField;
  private final String code;
  private final T value;
  private SettingFieldView settings;
  private FieldTypeView type;

  public FieldObjectViewBuilder(String code, String title, T value) {
    this.value = value;
    this.title = title;
    this.code = code;
  }

  public FieldObjectViewBuilder<T> setRequired(boolean required) {
    this.required = required;
    return this;
  }

  public FieldObjectViewBuilder<T> setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    return this;
  }

  public FieldObjectViewBuilder<T> setNumber(int number) {
    this.number = number;
    return this;
  }

  public FieldObjectViewBuilder<T> setTitleField(boolean titleField) {
    this.titleField = titleField;
    return this;
  }

  public FieldObjectViewBuilder<T> setSettings(SettingFieldView settings) {
    this.settings = settings;
    return this;
  }

  public FieldObjectViewBuilder<T> setType(FieldTypeView type) {
    this.type = type;
    return this;
  }

  public FieldObjectViewBuilder<T> map(ViewSetting fieldView) {
    this.setNumber(fieldView.getNumber());
    this.setReadOnly(fieldView.getReadonly());
    this.setRequired(fieldView.getRequired());
    this.setType(fieldView.getTypeView());
    this.setTitleField(fieldView.isTitleField());
    return this;
  }

  public FieldObjectView<T> build() {
    return new FieldObjectView<T>(title, required, readOnly, number, titleField, code, value, settings, type);
  }
}