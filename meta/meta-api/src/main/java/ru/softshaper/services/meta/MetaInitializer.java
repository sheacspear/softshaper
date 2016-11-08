package ru.softshaper.services.meta;

/**
 * MetaInitializer Created by Sunchise on 10.08.2016.
 */
public interface MetaInitializer {

  /**
   * @param loader
   */
  void registerLoader(MetaLoader loader);

  /**
   *
   */
  void init();

  /**
   * @param meta
   */
  void init(MetaClass meta);

}
