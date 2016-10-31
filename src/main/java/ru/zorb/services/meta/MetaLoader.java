package ru.zorb.services.meta;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Sunchise on 10.08.2016.
 */
public interface MetaLoader {

  /**
   * @return
   */
  Map<MetaClassMutable, ContentDataSource<?>> loadClasses();

  /**
   * @return
   */
  Collection<MetaFieldMutable> loadFields(Map<String, MetaClassMutable> clazzCode,Map<String, MetaClassMutable> clazzId);

}
