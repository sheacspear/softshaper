package ru.softshaper.bean.meta;

import ru.softshaper.services.meta.*;

import javax.annotation.concurrent.Immutable;

/**
 * Поле мета класса
 *
 * @author asheknew
 *
 */
@Immutable
public class MetaFieldMutableBean extends MetaFieldBean implements MetaFieldMutable {
  /**
   *
   */
  private final MetaClassMutable ownerMutable;

  /**
   * @param id идентиикатор
   * @param owner класс владелец
   * @param name имя
   * @param code код
   * @param column колонка
   * @param type тип
   * @param linkToMetaClass ссылка на класс
   * @param backReferenceField поле обратной ссылки
   */
  public MetaFieldMutableBean(String id, MetaClassMutable owner, String name, String code, String column, FieldType type,
                              MetaClass linkToMetaClass, MetaField backReferenceField, String nxMTableName) {
    super(id, owner, name, code, column, type, linkToMetaClass, backReferenceField, nxMTableName);
    this.ownerMutable = owner;
  }

  /* (non-Javadoc)
   * @see ru.softshaper.services.meta.MetaFieldMutable#getOwnerMutable()
   */
  @Override
  public MetaClassMutable getOwnerMutable() {
    return ownerMutable;
  }
}
