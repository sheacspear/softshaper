package ru.softshaper.web.bean.obj;

import ru.softshaper.web.bean.FieldTypeView;

/**
 * View field for bissness object <br/>
 * Created by Sunchise on 24.05.2016.
 */
public class FieldObjectView<T> {

  /**
  *
  */
  private final String code;

  /**
  *
  */
  private final int number;

  /**
  *
  */
  private final boolean readOnly;

  /**
  *
  */
  private final boolean required;

  /**
   * typeView field
   */
  private final SettingFieldView settings;

  /**
  *
  */
  private final String title;

  /**
  *
  */
  private final boolean titleField;

  /**
   *
   */
  private final FieldTypeView type;

  /**
   * value field
   */
  private final T value;

  FieldObjectView(String title, boolean required, boolean readOnly, int number, boolean titleField, String code, T value, SettingFieldView settings, FieldTypeView type) {
    super();
    this.title = title;
    this.required = required;
    this.readOnly = readOnly;
    this.number = number;
    this.titleField = titleField;
    this.code = code;
    this.value = value;
    this.settings = settings;
    this.type = type;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return the number
   */
  public int getNumber() {
    return number;
  }

  /**
   * @return the settings
   */
  public SettingFieldView getSettings() {
    return settings;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the type
   */
  public FieldTypeView getType() {
    return type;
  }

  /**
   * @return the value
   */
  public T getValue() {
    return value;
  }

  /**
   * @return the readOnly
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * @return the required
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * @return the titleField
   */
  public boolean isTitleField() {
    return titleField;
  }
}
