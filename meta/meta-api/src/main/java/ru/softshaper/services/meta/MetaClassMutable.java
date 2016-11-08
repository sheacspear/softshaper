package ru.softshaper.services.meta;

/**
 * мета класс ,меняемый<br/>
 * Created by Sunchise on 10.08.2016.
 */
public interface MetaClassMutable extends MetaClass{

  /**
   * @param field Поле мета класса
   */
  void addField(MetaField field);

}
